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
import com.callidusrobotics.locale.DungeonLevel;

/**
 * Defines behavior for a NonPlayerCharacter that is hostile to others and
 * attacks in melee. (Typically the player, but also other NonPlayerCharacters.)
 * A NonPlayerCharacter with this AiStrategy may be friendly to the player and
 * hostile to other NonPlayerCharacters.
 *
 * @author Rusty
 * @since 0.0.1
 */
@Behaves(Behavior.MELEE)
class AiStrategyMelee extends AbstractAiStrategy {

  private AbstractActor opponent = null;

  @Override
  public Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    if (opponent == null) {
      return new Message(Command.REST, null, null, null, null);
    }

    final int minDist = self.getPosition().distance2(opponent.getPosition());
    if (minDist <= 2) {
      return new Message(Command.ATTACKMELEE, Arrays.asList(opponent.getPosition()), null, null, null);
    }

    // Move towards the opponent
    final Command direction = getDirection(opponent.getPosition(), currentLevel);
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
    return base + multiplier * percentHp;
  }
}
