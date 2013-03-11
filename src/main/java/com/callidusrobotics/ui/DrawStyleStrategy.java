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

/**
 * Interface defining a drawing style for ConsoleTextBox objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see ConsoleTextBox
 */
interface DrawStyleStrategy {
  /**
   * Delegate method to draw the box in a Console.
   *
   * @param console
   *          The console to draw in, not null
   * @param consoleTextBox
   *          The box to draw, not null
   * @see AbstractConsoleTextBox#draw(Console)
   */
  void draw(Console console, AbstractConsoleTextBox consoleTextBox);
}
