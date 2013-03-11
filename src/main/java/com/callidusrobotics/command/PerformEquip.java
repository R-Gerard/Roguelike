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
import com.callidusrobotics.attribute.Equipable;
import com.callidusrobotics.object.EquipmentSlot;

@Performs(Command.EQUIP)
class PerformEquip extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final EquipmentSlot slot = selectEquipment("Currently equipped items. Press ENTER to select and equip or ESC to quit.", player);
    if (slot == null) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    player.sortInventory();

    final int index = selectItem("Currently held items. Press ENTER to select and equip or ESC to quit.", player);
    if (index != -1) {
      final Equipable item = player.getEquipmentFromInventory(index);
      if (!item.isEquipable()) {
        gameMediator.getConsoleColleague().printMessageLogEntry("That item is not equipable.");
      }

      if (item.isEquipable(slot)) {
        player.equipItemInInventoryToEquipmentSlot(index, slot);
        return new Message(Command.EQUIP, null, "You equip the " + item.getName() + ".", gameMediator.getConsoleColleague().getFontForeground(), null);
      } else if (item.isEquipable()) {
        gameMediator.getConsoleColleague().printMessageLogEntry("That item is not equipped that way.");
      }
    }

    return new Message(Command.UNKNOWN, null, null, null, null);
  }
}
