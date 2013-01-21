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

import java.awt.Color;

/**
 * Interface defining immutable objects with colors.
 *
 * @author Rusty
 * @since 0.0.1
 */
public interface Colored {
  /**
   * Foreground accessor.
   *
   * @return The foreground color of the object, never null
   */
  Color getForeground();

  /**
   * Background accessor.
   *
   * @return The background color of the object, or null
   */
  Color getBackground();
}
