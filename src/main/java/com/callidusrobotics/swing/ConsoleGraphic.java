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

import com.callidusrobotics.attribute.Colored;

/**
 * An immutable graphics object for displaying in a <code>Console</code>.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Console
 */
public class ConsoleGraphic implements Colored {
  protected char character;
  protected Color foreground;
  protected Color background;

  public ConsoleGraphic(final char character, final Color foreground, final Color background) {
    this.character = character;
    this.foreground = foreground;
    this.background = background;
  }

  public ConsoleGraphic(final ConsoleGraphic other) {
    this.character = other.character;
    this.foreground = other.foreground;
    this.background = other.background;
  }

  public final char getCharacter() {
    return character;
  }

  @Override
  public final Color getForeground() {
    return foreground;
  }

  @Override
  public final Color getBackground() {
    return background;
  }
}
