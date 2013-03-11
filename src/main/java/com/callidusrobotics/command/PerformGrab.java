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
import com.callidusrobotics.locale.Tile;

@Performs({ Command.GRABALL, Command.GRAB_PROMPT })
class PerformGrab extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final boolean grabAll = command.toString().contains("ALL");
    final String message = (grabAll ? "Pick up all of " : "Pick up ") + "something. Press ENTER to select or ESC to quit.";
    final Tile currentTile = gameMediator.getCurrentLevel().getTileRelative(player.getPosition());

    final Message result = transferInventoryItem(command, message, grabAll, currentTile, player);
    if (result.getAction() == command) {
      return new Message(command, null, "You picked up " + result.getDetails() + ".", gameMediator.getConsoleColleague().getFontForeground(), null);
    }

    return result;
  }
}
