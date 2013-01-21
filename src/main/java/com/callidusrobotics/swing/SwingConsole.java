/**
 * Copyright (C) 2013 Rusty Gerard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.callidusrobotics.swing;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.util.TrueColor;

/**
 * Implementation of <code>Console</code> using Swing to render an image in a
 * window.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Graphics
 * @see Image
 */
@SuppressWarnings("PMD.AvoidUsingVolatile")
final class SwingConsole extends AbstractConsole {
  // 1000 milliseconds / 30 frames per second = 33 ms delay
  private static final int REFRESH_RATE = 33;

  private Graphics graphicsRenderer;
  private Image imageBuffer;

  private JFrame frame;
  private JLabel label;
  private ImageIcon icon;

  private int charWidth, charHeight;
  private int charVOffset, charHOffset;

  private long lastUpdate = 0;

  private volatile KeyEvent lastKeyPressed = null;
  private volatile KeyEvent lastKeyTyped = null;

  SwingConsole(final Font font, final int rows, final int columns, final int animationSpeed) {
    super(rows, columns);

    Validate.notNull(font);
    this.animationSpeed = Math.max(REFRESH_RATE, animationSpeed);
    initConsole(font, rows, columns);
  }

  private void initConsole(final Font font, final int rows, final int cols) {
    frame = new JFrame();
    icon = new ImageIcon();
    label = new JLabel(icon);

    // Disallow focusing on frame elements
    frame.setFocusTraversalKeysEnabled(false);

    // Compute font metrics
    final DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    frame.setVisible(true);
    imageBuffer = frame.createImage(displayMode.getWidth(), displayMode.getHeight());
    graphicsRenderer = imageBuffer.getGraphics();
    graphicsRenderer.setFont(font);

    final FontMetrics fontMetrics = graphicsRenderer.getFontMetrics(font);

    charHeight = fontMetrics.getHeight();
    charWidth = fontMetrics.getHeight();
    charHOffset = 0;
    charVOffset = fontMetrics.getAscent();

    // Compute console dimensions and initialize graphics renderer
    final int width = charWidth * cols;
    final int height = charHeight * rows;

    imageBuffer = frame.createImage(width, height);
    graphicsRenderer = imageBuffer.getGraphics();
    graphicsRenderer.setFont(font);

    render();

    label.setBackground(TrueColor.MAGENTA);
    label.setBounds(0, 0, width, height);
    label.setVisible(true);

    frame.setBounds(0, 0, width, height);
    frame.setBackground(TrueColor.MAGENTA);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(label);
    frame.setCursor(null);
    frame.pack();

    frame.setLocationRelativeTo(null);
    frame.setResizable(false);

    frame.addKeyListener(this);
  }

  @Override
  public void setTitle(final String title) {
    frame.setTitle(title);
  }

  @Override
  public void render() {
    if (refreshSet.size() == 0) {
      return;
    }

    while (System.currentTimeMillis() - lastUpdate < REFRESH_RATE) {
      Thread.yield();
    }

    final Iterator<Integer> iterator = refreshSet.iterator();
    while (iterator.hasNext()) {
      final int index = iterator.next();

      // index = r * cols + c
      final int row = index / getWidth();
      final int col = index % getWidth();

      final int hpos = col * charWidth;
      final int vpos = row * charHeight;

      graphicsRenderer.setColor(consoleBuffer[row][col].background);
      graphicsRenderer.fillRect(hpos, vpos, charWidth, charHeight);
      graphicsRenderer.setColor(consoleBuffer[row][col].foreground);
      graphicsRenderer.drawString(String.valueOf(consoleBuffer[row][col].character), hpos + charHOffset, vpos + charVOffset);
    }
    refreshSet.clear();

    if (cursorIsVisible) {
      final int hpos = cursorCol * charWidth;
      final int vpos = cursorRow * charHeight;

      graphicsRenderer.setColor(cursor.foreground);
      graphicsRenderer.drawString(String.valueOf(cursor.character), hpos + charHOffset, vpos + charVOffset);
    }

    graphicsRenderer.drawImage(imageBuffer, 0, 0, null);
    icon.setImage(imageBuffer);
    label.setVisible(false);
    label.setVisible(true);

    lastUpdate = System.currentTimeMillis();
  }

  @Override
  @SuppressWarnings("PMD.NullAssignment")
  public KeyEvent getKeyPress() {
    lastKeyPressed = null;
    while (lastKeyPressed == null) {
      Thread.yield();
    }

    return lastKeyPressed;
  }

  @SuppressWarnings("PMD.NullAssignment")
  private KeyEvent getKeyTyped() {
    lastKeyTyped = null;
    while (lastKeyTyped == null) {
      Thread.yield();
    }

    return lastKeyTyped;
  }

  @Override
  public String getline(final int maxLen, final Color foreground, final Color background) {
    final StringBuffer stringBuffer = new StringBuffer(maxLen);

    // TODO: Make the cursor blink
    boolean keepPolling = true;
    while (keepPolling) {
      if (stringBuffer.length() < maxLen) {
        showCursor();
      } else {
        hideCursor();
      }
      render();

      final char input = getKeyTyped().getKeyChar();
      switch (input) {
        case KeyEvent.VK_ENTER:
          keepPolling = false;
          break;

        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_BACK_SPACE:
          if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            moveCursor(cursorRow, cursorCol - 1);
            print(cursorRow, cursorCol, ' ', background, background);
          }
          break;

        case KeyEvent.CHAR_UNDEFINED:
        case KeyEvent.VK_ESCAPE:
          // ignore these characters
          break;

        default:
          if (stringBuffer.length() < maxLen) {
            stringBuffer.append(input);
            print(cursorRow, cursorCol, input, foreground, background);
            moveCursor(cursorRow, cursorCol + 1);
          }
      }
    }

    hideCursor();
    render();

    return stringBuffer.toString();
  }

  @Override
  public void exit() {
    frame.dispose();
  }

  @Override
  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public synchronized void keyPressed(final KeyEvent keyEvent) {
    lastKeyPressed = keyEvent;
  }

  @Override
  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public synchronized void keyTyped(final KeyEvent keyEvent) {
    lastKeyTyped = keyEvent;
  }

  @Override
  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public synchronized void keyReleased(final KeyEvent keyEvent) { /* do nothing */ }
}
