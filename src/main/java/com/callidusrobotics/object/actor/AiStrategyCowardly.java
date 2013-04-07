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

import java.util.List;

import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;

/**
 * Defines behavior for a NonPlayerCharacter that will flee from opponents.
 *
 * @author Rusty
 * @since 0.0.3
 */
@Behaves(Behavior.COWARDLY)
class AiStrategyCowardly extends AbstractAiStrategy {
  private AbstractActor opponent = null;

  @Override
  protected Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    if (opponent == null) {
      return new Message(Command.REST, null, null, null, null);
    }

    // We have reached an exit
    if (currentLevel.getTileRelative(self.getPosition()).getExit() != null) {
      final Direction exit = currentLevel.getTileRelative(self.getPosition()).getExit();
      final DungeonLevel neighbor = currentLevel.getNeighbor(exit);

      // Make sure there is actually room for another NPC
      if (neighbor.getRandomCoordinate() != null) {
        currentLevel.getNonPlayerCharacters().remove(self);
        neighbor.getNonPlayerCharacters().add(self);
      }

      return new Message(exit.toCommand(), null, null, null, null);
    }

    // Run away from the opponent
    final Coordinate destination = getDestination(currentLevel, opponent);
    final Command direction = getDirection(destination, currentLevel);
    return self.processCommand(currentLevel, direction);
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    opponent = getNearestOpponent(factionData, currentLevel);
    if (opponent == null) {
      return min;
    }

    // TODO: Factor in relative strength of opponent?
    final int percentHp = getPercentHpRemaining();
    return base + multiplier * (100 - percentHp);
  }

  private Coordinate getDestination(final DungeonLevel currentLevel, final AbstractActor opponent) {
    final Coordinate exit = getNearestExit(currentLevel);
    if (exit == null) {
      return runAway(currentLevel, opponent);
    }

    if (opponent.getPosition().isBetween(self.getPosition(), exit)) {
      return runAway(currentLevel, opponent);
    }

    return exit;
  }

  private Coordinate runAway(final DungeonLevel currentLevel, final AbstractActor opponent) {
    // Move away from the opponent
    // Compute a vector from the opponent to us; don't bother to normalize it
    final Coordinate origin = currentLevel.getPosition().add(new Coordinate(currentLevel.getHeight() / 2, currentLevel.getWidth() / 2));
    if (opponent.getPosition().equals(origin)) {
      return self.getPosition().subtract(opponent.getPosition());
    }

    return self.getPosition().subtract(opponent.getPosition()).add(origin);
  }

  private Coordinate getNearestExit(final DungeonLevel currentLevel) {
    // Move to nearest exit
    final List<Tile> exits = currentLevel.getExits();

    Coordinate nearestExit = null;
    int minDistance = Integer.MAX_VALUE;

    for (final Tile exit : exits) {
      if (exit == null) {
        continue;
      }

      final int dist2 = self.getPosition().distance2(exit.getPosition());
      if (dist2 < minDistance) {
        minDistance = dist2;
        nearestExit = exit.getPosition();
      }
    }

    return nearestExit;
  }
}
