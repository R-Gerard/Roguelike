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

/**
 * Interface defining mutable objects with dimensions.
 *
 * @author Rusty
 * @since 0.0.1
 */
public interface Resizable extends Dimensional {
  /**
   * Height mutator.
   *
   * @param rows
   *          The height of the object
   */
  void setHeight(int rows);

  /**
   * Width mutator.
   *
   * @param cols
   *          The width of the object
   */
  void setWidth(int cols);
}
