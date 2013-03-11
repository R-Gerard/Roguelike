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

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.command.CommandMapper;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.swing.Console;

/**
 * Displays a list of items and allows the user to scroll through them. Uses
 * pagination if the list of items exceeds the number of printable rows.
 *
 * @author Rusty
 * @since 0.0.1
 */
public final class PaginatedMenuBox extends MessageBox {
  private final String message;
  private int currentIndex = 0;
  private final Color selectForeground, selectBackground;

  PaginatedMenuBox(final AbstractConsoleTextBox consoleTextBox, final String message, final Color selectForeground, final Color selectBackground) throws IllegalArgumentException {
    super(consoleTextBox);

    maxLines = this.consoleTextBox.getInternalHeight() - 4;
    lineLength = this.consoleTextBox.getInternalWidth() - 2;

    Validate.isTrue(maxLines > 0, "Invalid height");
    Validate.isTrue(lineLength > 0, "Invalid width");

    if (message.length() > this.consoleTextBox.getInternalWidth() - 2) {
      this.message = message.substring(0, this.consoleTextBox.getInternalWidth() - 2);
    } else {
      this.message = message;
    }

    this.selectForeground = selectForeground;
    this.selectBackground = selectBackground;
  }

  @Override
  public void addLine(final String text, final Color foreground, final Color background, final boolean preformatted) {
    Validate.notNull(text);

    final int endIndex = Math.min(lineLength, text.length());
    lineBuffer.add(new BufferLine(text.substring(0, endIndex), foreground, background));
  }

  @Override
  public void draw(final Console console) {
    consoleTextBox.draw(console);
    console.print(consoleTextBox.getInternalRow() + 1, consoleTextBox.getInternalCol() + 1, message, consoleTextBox.getForeground(), consoleTextBox.getBackground());
    printMessages(console);
  }

  public int selectIndex(final Console console, final boolean canCancel) {
    Command input = Command.UNKNOWN;

    while (input != Command.SELECT) {
      draw(console);
      console.render();
      input = CommandMapper.getCommand(console.getKeyPress());

      if (input == Command.ESCAPE && canCancel) {
        return -1;
      }

      if (input == Command.SOUTH) {
        currentIndex++;

        if (currentIndex > lineBuffer.size() - 1) {
          currentIndex = 0;
        }
      }

      if (input == Command.NORTH) {
        currentIndex--;

        if (currentIndex < 0) {
          currentIndex = lineBuffer.size() - 1;
        }
      }
    }

    if (lineBuffer.isEmpty()) {
      return -1;
    }

    return currentIndex;
  }

  public String selectValue(final Console console, final boolean canCancel) {
    final int index = selectIndex(console, canCancel);

    if (index != -1) {
      return lineBuffer.get(index).line;
    }

    return null;
  }

  private void printMessages(final Console console) {
    final int row = consoleTextBox.getInternalRow() + 3;
    final int col = consoleTextBox.getInternalCol() + 1;

    if (lineBuffer.isEmpty()) {
      return;
    }

    // TODO: This has a nice effect when the menuBuffer size is divisible by the pageSize, but can look wonky otherwise
    final int startIndex = currentIndex >= maxLines ? Math.max(currentIndex - maxLines, lineBuffer.size() - maxLines) : 0;
    final int nEntries = Math.min(maxLines, lineBuffer.size());

    // Copy the messageBuffer into the console's buffer
    final StringBuffer stringBuffer = new StringBuffer(lineLength);
    for (int r = startIndex; r < startIndex + nEntries; r++) {
      final BufferLine line = lineBuffer.get(r);
      stringBuffer.delete(0, stringBuffer.length());
      stringBuffer.append(line.line, 0, Math.min(lineLength, line.line.length()));
      for (int c = line.line.length(); c < lineLength; c++) {
        stringBuffer.append(' ');
      }

      if (r == currentIndex) {
        console.print(row + (r - startIndex) % maxLines, col, stringBuffer.toString(), selectForeground, selectBackground);
      } else {
        console.print(row + (r - startIndex) % maxLines, col, stringBuffer.toString(), line.foreground, line.background);
      }
    }
  }
}
