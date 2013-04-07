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

package com.callidusrobotics.object.actor;

import java.util.Arrays;

import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;

/**
 * Allows the user to examine in-game objects.
 *
 * @author Rusty
 * @since 0.0.3
 * @see com.callidusrobotics.object.AbstractGameObject
 */
public class SearchReticle extends AbstractReticle {

  public SearchReticle(final DungeonLevel dungeonLevel, final Coordinate startPosition) {
    super(dungeonLevel, startPosition);

    if (!dungeonLevel.getNonPlayerCharacters().isEmpty()) {
      final Coordinate targetPosition = selectTarget(dungeonLevel, targetIndex);
      setPosition(targetPosition);
    }
  }

  @Override
  public Message processCommand(final DungeonLevel currentLevel, final Command command) {
    super.processCommand(currentLevel, command);

    if (command == Command.SELECT || command == Command.EXAMINE) {
      final AbstractActor actor = currentLevel.getAbstractActorAtPosition(getPosition());
      if (actor != null) {
        return new Message(Command.EXAMINE, Arrays.asList(getPosition()), actor.getDescription(), getForeground(), null);
      }

      // Should we get a reference to the player and check if we are examining it? Or is it better to examine the ground at the player's feet?
      final Tile currentTile = currentLevel.getTileRelative(getPosition());
      return new Message(Command.EXAMINE, Arrays.asList(getPosition()), currentTile.getDescription(), getForeground(), null);
    }

    if (command == Command.TOGGLE && !currentLevel.getNonPlayerCharacters().isEmpty()) {
      targetIndex = (targetIndex + 1) % currentLevel.getNonPlayerCharacters().size();
      final Coordinate targetPosition = selectTarget(currentLevel, targetIndex);
      setPosition(targetPosition);
    }

    // Return UNKNOWN because moving the reticle should not update the game state
    return new Message(Command.UNKNOWN, null, null, null, null);
  }

  @Override
  protected boolean isBarrier(final DungeonLevel currentLevel, final Coordinate position) {
    if (!currentLevel.checkCoordinatesRelative(position)) {
      return true;
    }

    // We want the reticle to be able to examine anything that was once visible
    return !currentLevel.getTileRelative(position).isExplored();
  }
}
