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
import java.util.List;
import java.util.Map;

import com.callidusrobotics.Message;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.actor.AbstractActor;

@Performs(Command.CHAT)
class PerformChat extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final DungeonLevel currentLevel = gameMediator.getCurrentLevel();
    final Map<Integer, List<AbstractActor>> actors = currentLevel.getAbstractActorsRankedByDistance(player.getPosition(), true);
    actors.remove(0);
    if (actors.keySet().contains(1) || actors.keySet().contains(2)) {
      // TODO: Check to see if there is more than one NPC that is adjacent to the player; if so then prompt the player for a direction
      final AbstractActor actor = actors.get(actors.keySet().iterator().next()).get(0);
      final Message message = actor.processCommand(currentLevel, Command.CHAT);
      if (message.getAction() == Command.CHAT) {
        gameMediator.getConsoleColleague().printMessageLogEntry(message.getDetails(), message.getForeground(), message.getBackground());
      }

      return new Message(Command.CHAT, Arrays.asList(actor.getPosition()), null, null, null);
    }

    return new Message(Command.UNKNOWN, null, "There is no one to talk to.", gameMediator.getConsoleColleague().getFontForeground(), null);
  }
}
