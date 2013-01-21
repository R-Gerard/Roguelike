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
 * Immutable representation of a 2D coordinate.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class Coordinate {
  protected static final int MAX_DIM = (int) Math.sqrt(Integer.MAX_VALUE);
  protected int row, col;

  public Coordinate(final int row, final int col) {
    this.row = row;
    this.col = col;
  }

  public Coordinate(final Coordinate other) {
    this.row = other.row;
    this.col = other.col;
  }

  /**
   * Computes squared Euclidean distance from other point.
   *
   * @param other
   *          The other point, nullable
   * @return The squared Euclidean distance
   */
  public int distance2(final Coordinate other) {
    if (other == null) {
      return 0;
    }

    final int deltaR = row - other.row;
    final int deltaC = col - other.col;

    return deltaR * deltaR + deltaC * deltaC;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return "[" + row + "," + col + "]";
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Coordinate) {
      final Coordinate otherCoordinate = (Coordinate) other;

      return otherCoordinate.row == row && otherCoordinate.col == col;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return row * MAX_DIM + col;
  }
}
