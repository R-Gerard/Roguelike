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

package com.callidusrobotics.ui;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.command.CommandMapper;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.util.TrueColor;

/**
 * Displays text on the screen and waits for the user to press the appropriate
 * confirmation key.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class MessageBox implements ConsoleTextBox {
  protected final AbstractConsoleTextBox consoleTextBox;
  protected int lineLength;
  protected int maxLines;
  protected final List<BufferLine> lineBuffer;

  MessageBox(final AbstractConsoleTextBox consoleTextBox) {
    Validate.notNull(consoleTextBox);

    this.consoleTextBox = consoleTextBox;

    lineBuffer = new LinkedList<BufferLine>();
    lineLength = consoleTextBox.getInternalWidth() - 2;
    maxLines = consoleTextBox.getInternalHeight() - 2;

    Validate.isTrue(lineLength > 0, "Invalid width");
    Validate.isTrue(maxLines > 0, "Invalid height");
  }

  @Override
  public void draw(final Console console) {
    consoleTextBox.draw(console);

    // Blank out everything inside the box
    for (int r = consoleTextBox.getInternalRow() + 1; r < consoleTextBox.getInternalRow() + consoleTextBox.getInternalHeight() - 1; r++) {
      for (int c = consoleTextBox.getInternalCol() + 1; c < consoleTextBox.getInternalCol() + consoleTextBox.getInternalWidth() - 1; c++) {
        console.print(r, c, ' ', TrueColor.BLACK, TrueColor.BLACK);
      }
    }

    // Copy the messageBuffer into the console's buffer
    int row = consoleTextBox.getInternalRow() + 1;
    final int col = consoleTextBox.getInternalCol() + 1;
    for (final BufferLine line : lineBuffer) {
      console.print(row, col, line.line, line.foreground, line.background);
      row++;
    }
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void addLine(final String string, final Color foreground, final Color background, final boolean preformatted) {
    final List<String> tokens = getMessageTokens(string, preformatted);
    final StringBuffer stringBuffer = new StringBuffer(lineLength);

    // Append the string token-by-token, line-by-line to the end of the messagebuffer
    while (!tokens.isEmpty()) {
      stringBuffer.delete(0, stringBuffer.length());
      stringBuffer.append(tokens.remove(0));

      // The first word to be printed on the line is too long and needs to be hyphenated
      if (stringBuffer.length() > lineLength) {
        tokens.add(0, stringBuffer.substring(lineLength - 1, stringBuffer.length()));
        stringBuffer.delete(lineLength - 1, stringBuffer.length());
        stringBuffer.append('-');
      }

      // Continue to append tokens to the linebuffer one-by-one and append enough whitespace to fill the buffer
      while (stringBuffer.length() < lineLength) {
        stringBuffer.append(' ');

        final boolean tokenWillFit = !tokens.isEmpty() && stringBuffer.length() + tokens.get(0).length() <= lineLength;
        if (!preformatted && tokenWillFit) {
          stringBuffer.append(tokens.remove(0));
        }
      }

      lineBuffer.add(new BufferLine(stringBuffer.toString(), foreground, background));

      // Delete the oldest message line
      if (lineBuffer.size() > maxLines) {
        lineBuffer.remove(0);
      }
    }
  }

  protected List<String> getMessageTokens(final String string, final boolean preformatted) {
    final List<String> tokens = new LinkedList<String>(Arrays.asList(string.split(preformatted ? "\n" : "\\s+")));

    if (preformatted) {
      for (int i = 0; i < tokens.size(); i++) {
        String token = tokens.get(i);

        if (!token.isEmpty() && token.charAt(0) == ' ') {
          token = token.trim();
          token = StringUtils.leftPad(token, token.length() + 1);
          tokens.set(i, token);
        }
      }
    }

    return tokens;
  }

  public void pause(final Console console) {
    console.render();

    Command input = Command.UNKNOWN;
    while (input != Command.ESCAPE && input != Command.SELECT) {
      input = CommandMapper.getCommand(console.getKeyPress());
    }
  }

  @Override
  public int getWidth() {
    return consoleTextBox.getWidth();
  }

  @Override
  public int getHeight() {
    return consoleTextBox.getHeight();
  }

  @Override
  public Coordinate getPosition() {
    return consoleTextBox.getPosition();
  }

  @Override
  public void setPosition(final int row, final int col) {
    consoleTextBox.setPosition(row, col);
  }

  @Override
  public void setPosition(final Coordinate position) {
    consoleTextBox.setPosition(position);
  }

  @Override
  public int getRow() {
    return consoleTextBox.getRow();
  }

  @Override
  public int getCol() {
    return consoleTextBox.getCol();
  }
}
