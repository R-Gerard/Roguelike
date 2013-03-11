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

import com.callidusrobotics.object.Size;

/**
 * Interface defining mutable objects in a size category.
 *
 * @author Rusty
 * @since 0.0.1
 */
public interface Categorized {
  /**
   * Size accessor.
   *
   * @return The size of this, never null
   */
  Size getSize();

  /**
   * Size mutator.
   * <p>
   * A null size is interpreted as {@link Size#MICROSCOPIC}.
   *
   * @param size
   *          The size of this, nullable
   */
  void setSize(Size size);
}
