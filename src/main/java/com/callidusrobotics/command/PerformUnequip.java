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
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.object.EquipmentSlot;

@Performs(Command.UNEQUIP)
class PerformUnequip extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final EquipmentSlot slot = selectEquipment("Currently equipped items. Press ENTER to select and unequip or ESC to quit.", player);
    if (player.getEquippedItem(slot) != null) {
      if (player.canAddItemToInventory(player.getEquippedItem(slot))) {
        final Inventoriable item = player.getEquippedItem(slot);
        player.addItemToInventory(player.removeEquippedItem(slot));
        return new Message(Command.UNEQUIP, null, "You remove the " + item.getName() + ".", gameMediator.getConsoleColleague().getFontForeground(), null);
      } else {
        gameMediator.getConsoleColleague().printMessageLogEntry("You have no room in your inventory. Drop something first.");
      }
    }

    return new Message(Command.UNKNOWN, null, null, null, null);
  }
}
