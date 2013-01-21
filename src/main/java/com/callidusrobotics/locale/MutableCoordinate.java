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

package com.callidusrobotics.locale;

/**
 * Mutable representation of a 2D coordinate.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class MutableCoordinate extends Coordinate {
  public MutableCoordinate(final int row, final int col) {
    super(row, col);
  }

  public MutableCoordinate(final Coordinate other) {
    super(other);
  }

  public void setPosition(final Coordinate other) {
    this.row = other.row;
    this.col = other.col;
  }

  public void setPosition(final int row, final int col) {
    this.row = row;
    this.col = col;
  }
}
