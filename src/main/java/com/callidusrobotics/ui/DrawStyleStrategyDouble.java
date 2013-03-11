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

import com.callidusrobotics.swing.Console;
import com.callidusrobotics.util.Unicode;

final class DrawStyleStrategyDouble implements DrawStyleStrategy {
  @Override
  public void draw(final Console console, final AbstractConsoleTextBox consoleTextBox) {
    consoleTextBox.draw(console,
        Unicode.LINE_DOUBLE_HORIZONTAL, Unicode.LINE_DOUBLE_VERTICAL,
        Unicode.LINE_DOUBLE_DOWN_AND_RIGHT, Unicode.LINE_DOUBLE_DOWN_AND_LEFT,
        Unicode.LINE_DOUBLE_UP_AND_RIGHT, Unicode.LINE_DOUBLE_UP_AND_LEFT,
        Unicode.LINE_DOUBLE_HORIZONTAL_AND_VERTICAL);
  }
}
