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
 * An enumerated type of field-of-view (FOV) algorithms.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Illuminates
 */
enum FieldOfViewType {
  /**
   * No field-of-view algorithm is used; every tile is illuminated.
   */
  ALL,

  /**
   * Algorithm used by Rogue; the room the PlayerCharacter currently occupies is
   * illuminated.
   */
  CLASSIC,

  /**
   * Every tile inside of a circle centered on the PlayerCharacter is
   * illuminated.
   */
  SIMPLE_RADIUS,

  /**
   * Every point that is within line-of-sight of the PlayerCharacter is
   * illuminated.
   */
  SHADOWCASTING
}
