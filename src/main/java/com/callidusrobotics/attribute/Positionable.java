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

package com.callidusrobotics.attribute;

import com.callidusrobotics.locale.Coordinate;

/**
 * Interface defining mutable objects with dimensions and positions.
 *
 * @author Rusty
 * @since 0.0.1
 */
public interface Positionable {
  /**
   * Position accessor.
   *
   * @return The position of the object, never null
   */
  Coordinate getPosition();

  /**
   * Position mutator.
   *
   * @param row
   *          The vertical position of the object
   * @param col
   *          The horizontal position of the object
   */
  void setPosition(int row, int col);

  /**
   * Position mutator.
   *
   * @param position
   *          The position of the object, not null
   */
  void setPosition(Coordinate position);

  /**
   * Vertical position accessor.
   *
   * @return The vertical position of the object
   */
  int getRow();

  /**
   * Horizontal position accessor.
   *
   * @return The horizontal position of the object
   */
  int getCol();
}
