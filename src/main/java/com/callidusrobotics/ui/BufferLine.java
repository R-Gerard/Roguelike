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

/**
 * POJO for storing Strings with color info suitable for printing in the
 * Console.
 *
 * @author Rusty
 * @since 0.0.1
 */
class BufferLine {
  public String line;
  public Color foreground, background;

  BufferLine(final String line, final Color foreground, final Color background) {
    Validate.notNull(line);

    this.line = line;
    this.foreground = foreground;
    this.background = background;
  }
}
