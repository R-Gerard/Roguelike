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
import com.callidusrobotics.attribute.Useable;

@Performs(Command.USE)
class PerformUse extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    player.sortInventory();

    final int index = selectItem("Currently held items. Press ENTER to select and use or ESC to quit.", player);
    if (index != -1) {
      final Useable item = player.getToolFromInventory(index);
      final Message message = item.use(player, gameMediator.getCurrentLevel());

      // If the message details contain preformatted text, splash it on the screen
      if (message.getAction() != Command.UNKNOWN && message.getDetails().contains("\n")) {
        gameMediator.getConsoleColleague().splash(message.getDetails());

        // Return an empty message so that we don't display the text again later
        return new Message(message.getAction(), null, null, null, null);
      }

      return message;
    }

    return new Message(Command.UNKNOWN, null, null, null, null);
  }
}
