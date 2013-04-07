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

import java.util.List;
import java.util.Set;

import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.PlayerCharacter;

/**
 * Interface defining implementation of a field-of-view algorithm to illuminate
 * Tile objects around the PlayerCharacter. Classes that implement this
 * interface should be annotated with <code>Illuminates</code>.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Illuminates
 */
public interface FieldOfViewStrategy {
  /**
   * Clears the internal state of this when the PlayerCharacter changes levels.
   */
  void playerExitLevel();

  /**
   * Illuminates the DungeonLevel with respect to the position of the
   * PlayerCharacter using the specified field-of-view algorithm.
   *
   * @param player
   *          The PlayerCharacter, nullable
   * @param currentLevel
   *          The DungeonLevel the player occupies, not null
   * @return List of coordinates that have <i>changed</i> illumination since the
   *         last call to this
   */
  List<Coordinate> illuminate(PlayerCharacter player, DungeonLevel currentLevel);

  /**
   * Computes the set of tiles that are illuminated for an actor.
   *
   * @param actor
   *          The AbstractActor, nullable
   * @param currentLevel
   *          The DungeonLevel that the actor occupies, not null
   * @return List of coordinates that are illuminated
   */
  Set<Coordinate> getIlluminated(AbstractActor actor, DungeonLevel currentLevel);
}
