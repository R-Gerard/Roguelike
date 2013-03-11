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

import java.util.Arrays;

import com.callidusrobotics.Message;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.ActorFactionData;
import com.callidusrobotics.object.actor.Relationship;

@Performs({ Command.ASCEND, Command.DESCEND, Command.NORTH, Command.NORTHEAST, Command.EAST, Command.SOUTHEAST, Command.SOUTH, Command.SOUTHWEST, Command.WEST, Command.NORTHWEST, Command.REST })
class PerformMove extends AbstractCommandPerformer {

  @Override
  protected void before() {
    // The default before() behavior kicks the player back into walking mode; we don't want to do that (in case the player is in search or target mode)
    player.setPosition(player.getPosition());
  }

  @Override
  public Message performDelegate(final Command command) {
    final DungeonLevel currentLevel = gameMediator.getCurrentLevel();

    if (player.isInWalkMode() && command == Command.REST) {
      return new Message(Command.REST, null, null, null, null);
    }

    if (player.isInTargetingMode()) {
      return player.getReticle().processCommand(currentLevel, command);
    }

    final Coordinate desiredPosition = player.desiredPosition(command);
    if (!desiredPosition.equals(player.getPosition()) && currentLevel.getAbstractActorAtPosition(desiredPosition) != null) {
      return processAttack(command);
    }

    final Message message = player.processCommand(currentLevel, command);
    processMove(command);
    return message;
  }

  private Message processAttack(final Command command) {
    final DungeonLevel currentLevel = gameMediator.getCurrentLevel();
    final ActorFactionData actorFactionData = gameMediator.getActorFactionData();
    final Coordinate position = player.desiredPosition(command);
    final AbstractActor actor = currentLevel.getAbstractActorAtPosition(position);

    if (actor != null) {
      boolean confirmAttack = true;
      if (!actorFactionData.areEnemies(actor.getFaction(), player.getFaction())) {
        confirmAttack = gameMediator.getConsoleColleague().confirm("Really attack " + actor.getNameThirdPerson() + "?");
      }

      if (confirmAttack) {
        actorFactionData.setRelationship(actor.getFaction(), player.getFaction(), Relationship.ENEMY);
        gameMediator.processAttack(player, actor, Command.ATTACKMELEE);
        return new Message(Command.ATTACKMELEE, Arrays.asList(position), null, null, null);
      }
    }

    return new Message(Command.REST, null, null, null, null);
  }

  private void processMove(final Command command) {
    final DungeonLevel currentLevel = gameMediator.getCurrentLevel();
    final Tile currentTile = currentLevel.getTileRelative(player.getPosition());
    final Direction exitDirection = currentTile.getExit();

    // Nothing to do because we have already called player.processCommand()
    if (currentTile.getExit() == null) {
      return;
    }

    // If the player has stepped onto one of the exit tiles
    if (player.hasMoved() && exitDirection != Direction.UP && exitDirection != Direction.DOWN) {
      gameMediator.playerEnterLevel();
    }

    // If the player is standing on a staircase and issued the corresponding command
    if (command == Command.ASCEND && exitDirection == Direction.UP || command == Command.DESCEND && exitDirection == Direction.DOWN) {
      gameMediator.playerEnterLevel();
    }
  }
}
