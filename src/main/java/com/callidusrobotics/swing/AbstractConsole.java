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
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.util.TrueColor;

/**
 * Base class for <code>Console</code> implementations.
 *
 * @author Rusty
 * @since 0.0.1
 */
abstract class AbstractConsole implements KeyListener, Console {
  protected MutableConsoleGraphic[][] consoleBuffer;
  protected MutableConsoleGraphic cursor;
  protected int cursorRow = 0, cursorCol = 0;
  protected Set<Integer> refreshSet;
  protected boolean cursorIsVisible = false;
  protected int animationSpeed = 1000;

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  AbstractConsole(final int rows, final int columns) {
    Validate.isTrue(rows > 0, "Invalid height");
    Validate.isTrue(columns > 0, "Invalid width");

    cursor = new MutableConsoleGraphic('_', TrueColor.GRAY, null);

    consoleBuffer = new MutableConsoleGraphic[rows][columns];
    refreshSet = new HashSet<Integer>(rows * columns);

    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        consoleBuffer[r][c] = new MutableConsoleGraphic(' ', TrueColor.GRAY, TrueColor.BLACK);
        refreshSet.add(r * getWidth() + c);
      }
    }
  }

  @Override
  public final int getWidth() {
    return consoleBuffer[0].length;
  }

  @Override
  public final int getHeight() {
    return consoleBuffer.length;
  }

  @Override
  public final void print(final int row, final int col, final ConsoleGraphic consoleGraphic) {
    print(row, col, consoleGraphic.character, consoleGraphic.foreground, consoleGraphic.background);
  }

  @Override
  @SuppressWarnings("PMD.AvoidReassigningParameters")
  public final void print(final int row, final int col, final char character, Color foreground, Color background) {
    if (foreground == null) {
      foreground = consoleBuffer[row][col].foreground;
    }

    if (background == null) {
      background = consoleBuffer[row][col].background;
    }

    // Do a manual copy here rather than use the copy constructor
    consoleBuffer[row][col].character = character;
    consoleBuffer[row][col].foreground = foreground;
    consoleBuffer[row][col].background = background;

    refreshSet.add(row * getWidth() + col);
  }

  @Override
  public final void print(final int row, final int col, final String string, final Color foreground, final Color background) {
    for (int i = 0; i < string.length(); i++) {
      print(row, col + i, string.charAt(i), foreground, background);
    }
  }

  @Override
  public final void clear() {
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        print(r, c, ' ', TrueColor.GRAY, TrueColor.BLACK);
      }
    }
  }

  @Override
  public void showCursor() {
    refreshSet.add(cursorRow * getWidth() + cursorCol);
    cursorIsVisible = true;
  }

  @Override
  public void hideCursor() {
    refreshSet.add(cursorRow * getWidth() + cursorCol);
    cursorIsVisible = false;
  }

  @Override
  public void moveCursor(final int row, final int col) {
    refreshSet.add(cursorRow * getWidth() + cursorCol);
    refreshSet.add(row * getWidth() + col);

    cursorRow = row;
    cursorCol = col;
  }

  @Override
  public void setCursor(final int row, final int col, final ConsoleGraphic cursor) {
    refreshSet.add(cursorRow * getWidth() + cursorCol);
    refreshSet.add(row * getWidth() + col);

    cursorRow = row;
    cursorCol = col;

    this.cursor.background = cursor.background;
    this.cursor.foreground = cursor.foreground;
    this.cursor.character = cursor.character;
  }

  @Override
  public int getAnimationSpeed() {
    return animationSpeed;
  }

  @Override
  public void setAnimationSpeed(final int animationSpeed) {
    this.animationSpeed = animationSpeed;
  }
}
