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

@Performs(Command.TOGGLE)
class PerformToggle extends AbstractCommandPerformer {

  @Override
  public void before() {
    // The default before() behavior kicks the player back into walking mode; we don't want to do that (in case the player is in search or target mode)
    player.setPosition(player.getPosition());
  }

  @Override
  public Message performDelegate(final Command command) {
    if (player.isInTargetingMode()) {
      return player.getReticle().processCommand(gameMediator.getCurrentLevel(), command);
    } else {
      gameMediator.getConsoleColleague().nextDisplayMode();
      return new Message(Command.UNKNOWN, null, null, null, null);
    }
  }
}
