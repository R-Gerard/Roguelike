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

  /**
   * Vector addition operator.
   *
   * @param other
   *          The operand, nullable
   * @return The vector sum of this and the operand, never null
   */
  public Coordinate add(final Coordinate other) {
    final Coordinate temp = new Coordinate(this);
    if (other != null) {
      temp.row += other.row;
      temp.col += other.col;
    }

    return temp;
  }

  /**
   * Vector subtraction operator.
   *
   * @param other
   *          The operand, nullable
   * @return The vector difference of this and the operand, never null
   */
  public Coordinate subtract(final Coordinate other) {
    final Coordinate temp = new Coordinate(this);
    if (other != null) {
      temp.row -= other.row;
      temp.col -= other.col;
    }

    return temp;
  }

  /**
   * Scalar multiplication operator.
   *
   * @param scalar
   *          The coefficient
   * @return The vector product of this and the coefficient
   */
  public Coordinate multiply(final int scalar) {
    final Coordinate temp = new Coordinate(this);
    temp.row *= scalar;
    temp.col *= scalar;

    return temp;
  }

  /**
   * 2D vector cross product operator.
   *
   * @param other
   *          The operand, nullable
   * @return The scalar cross product of this and the operand
   */
  public int crossProduct(final Coordinate other) {
    if (other == null) {
      return 0;
    }

    // Ux*Vy - Uy*Vx
    return row * other.col - col * other.row;
  }

  /**
   * 2D vector dot product operator.
   *
   * @param other
   *          The operand, nullable
   * @return The scalar dot product of this and the operand
   */
  public int dotProduct(final Coordinate other) {
    if (other == null) {
      return 0;
    }

    // Ux*Vx + Uy*Vy
    return row * other.row + col * other.col;
  }

  /**
   * Tests if this is collinear with and lies between two other points.
   *
   * @param a
   *          The first line endpoint, not null
   * @param b
   *          The second line endpoint, not null
   * @return True if this is between
   */
  @SuppressWarnings("PMD.ShortVariable")
  public boolean isBetween(final Coordinate a, final Coordinate b) {
    if (a == null || b == null) {
      return false;
    }

    // Points a, b, and c are collinear if the cross-product (b-a)x(c-a) == 0
    final Coordinate a2b = b.subtract(a);
    final Coordinate a2self = this.subtract(a);
    final int crossProduct = a2b.crossProduct(a2self);

    if (crossProduct != 0) {
      return false;
    }

    // Let P denote the dot-product of (b-a)*(c-a)
    // Point c is between a and b if P > 0 && P < dist^2(a, b)
    final int dotProduct = a2b.dotProduct(a2self);
    return dotProduct > 0 && dotProduct < a.distance2(b);
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
