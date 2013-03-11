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

  @Override
  public MutableCoordinate add(final Coordinate other) {
    // Create a new object in case the caller thinks this is an instance of Coordinate
    return new MutableCoordinate(super.add(other));
  }

  /**
   * Vector addition mutator.
   *
   * @param other
   *          The operand, nullable
   * @return this, for method chaining
   * @see #add(Coordinate)
   */
  public MutableCoordinate increment(final Coordinate other) {
    if (other != null) {
      this.row += other.row;
      this.col += other.col;
    }

    return this;
  }

  @Override
  public MutableCoordinate subtract(final Coordinate other) {
    // Create a new object in case the caller thinks this is an instance of Coordinate
    return new MutableCoordinate(super.subtract(other));
  }

  /**
   * Vector subtraction mutator.
   *
   * @param other
   *          The operand, nullable
   * @return this, for method chaining
   * @see #subtract(Coordinate)
   */
  public MutableCoordinate decrement(final Coordinate other) {
    if (other != null) {
      this.row -= other.row;
      this.col -= other.col;
    }

    return this;
  }

  @Override
  public MutableCoordinate multiply(final int scalar) {
    // Create a new object in case the caller thinks this is an instance of Coordinate
    return new MutableCoordinate(super.multiply(scalar));
  }

  /**
   * Scalar multiplication mutator.
   *
   * @param scalar
   *          The coefficient
   * @return this, for method chaining
   * @see #multiply(int)
   */
  public MutableCoordinate magnify(final int scalar) {
    this.row *= scalar;
    this.col *= scalar;

    return this;
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
