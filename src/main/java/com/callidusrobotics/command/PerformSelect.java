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

package com.callidusrobotics.command;

import com.callidusrobotics.Message;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.Relationship;

@Performs({ Command.SELECT, Command.FIRE })
class PerformSelect extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final DungeonLevel currentLevel = gameMediator.getCurrentLevel();

    if (player.isInWalkMode() && command == Command.FIRE) {
      return player.createReticle(currentLevel, command);
    }

    player.setWalkMode(true);
    if (!player.isInTargetingMode()) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    final Message message = player.getReticle().processCommand(currentLevel, command);
    if (message.getAction() == Command.ATTACKRANGED) {
      processAttacks(currentLevel, message);
    }

    return message;
  }

  private void processAttacks(final DungeonLevel currentLevel, final Message message) {
    int count = 0;
    for (final Coordinate target : message.getTargets()) {
      final AbstractActor actor = currentLevel.getAbstractActorAtPosition(target);
      if (actor == null) {
        count++;
        continue;
      }

      // Only consider the first target to be the intended target; others are incidental (so allies don't turn hostile due to accidental friendly fire)
      if (count++ == 0) {
        gameMediator.getActorFactionData().setRelationship(player.getFaction(), actor.getFaction(), Relationship.ENEMY);
      }

      gameMediator.processAttack(player, actor, message.getAction());
    }
  }
}
