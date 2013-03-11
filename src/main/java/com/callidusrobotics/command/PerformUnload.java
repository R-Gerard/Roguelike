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

import java.awt.Color;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.attribute.Useable;
import com.callidusrobotics.object.Item;

@Performs(Command.UNLOAD)
@SuppressWarnings("PMD.CyclomaticComplexity")
class PerformUnload extends AbstractCommandPerformer {

  @Override
  @SuppressWarnings("PMD.NPathComplexity")
  public Message performDelegate(final Command command) {
    player.sortInventory();

    final Color foreground = gameMediator.getConsoleColleague().getFontForeground();
    final int index = selectItem("Select an item to unload.", player);
    if (index < 0) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    if (!(player.getItemFromInventory(index) instanceof Useable)) {
      return new Message(Command.UNKNOWN, null, "You cannot unload that item.", foreground, null);
    }

    final Useable weapon = (Useable) player.getItemFromInventory(index);
    if (!weapon.isLoadable()) {
      return new Message(Command.UNKNOWN, null, "You cannot unload that item.", foreground, null);
    }

    if (weapon.getAmmunitionType() == null) {
      return new Message(Command.UNKNOWN, null, "You cannot unload that item.", foreground, null);
    }

    if (weapon.getCurrentCapacity() < 1) {
      return new Message(Command.UNKNOWN, null, "The " + weapon.getName() + " is empty.", foreground, null);
    }

    final Inventoriable ammo = new Item((Item) weapon.getAmmunitionType());
    ammo.setQuantity(1);
    if (!player.canAddItemToInventory(ammo)) {
      return new Message(Command.UNKNOWN, null, "There is no room in your inventory to add another item.", foreground, null);
    }

    ammo.setQuantity(weapon.getCurrentCapacity());
    if (!player.canAddItemToInventory(ammo)) {
      return new Message(Command.UNKNOWN, null, "There is not enough room in your inventory for that many.", foreground, null);
    }

    ((Item) weapon).setCurrentCapacity(0);
    final int quantity = ammo.getQuantity();
    player.addItemToInventory((Item) ammo);

    if (quantity == 1) {
      return new Message(Command.UNLOAD, null, "You unload 1 round from the " + weapon.getName() + ".", foreground, null);
    } else {
      return new Message(Command.UNLOAD, null, "You unload " + quantity + " rounds from the " + weapon.getName() + ".", foreground, null);
    }
  }
}
