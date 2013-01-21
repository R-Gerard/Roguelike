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

import com.callidusrobotics.attribute.Colorable;

/**
 * An mutable graphics object for displaying in a <code>Console</code>.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Console
 */
public class MutableConsoleGraphic extends ConsoleGraphic implements Colorable {
  public MutableConsoleGraphic(final char character, final Color foreground, final Color background) {
    super(character, foreground, background);
  }

  public MutableConsoleGraphic(final ConsoleGraphic other) {
    super(other);
  }

  public void setCharacter(final char character) {
    this.character = character;
  }

  @Override
  public void setForeground(final Color foreground) {
    this.foreground = foreground;
  }

  @Override
  public void setBackground(final Color background) {
    this.background = background;
  }
}
