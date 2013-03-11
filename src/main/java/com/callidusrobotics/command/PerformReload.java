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
import java.util.List;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;

@Performs(Command.RELOAD)
class PerformReload extends AbstractCommandPerformer {

  @Override
  public Message performDelegate(final Command command) {
    final Item weapon = (Item) player.getEquippedItem(EquipmentSlot.WEAPON);
    final Inventoriable ammoType = weapon == null ? null : weapon.getAmmunitionType();
    final Color foreground = gameMediator.getConsoleColleague().getFontForeground();

    if (ammoType == null || !weaponNeedsAmmo(weapon)) {
      player.setPosition(player.getPosition());
      return new Message(Command.UNKNOWN, null, "You don't need to reload your weapon.", foreground, null);
    }

    final List<Integer> indeces = player.getItemIndecesFromInventory(ammoType);
    if (indeces.isEmpty()) {
      return new Message(Command.RELOAD, null, "You are out of ammunition for your weapon.", foreground, null);
    }

    // Call weapon.reload until we run out of ammunition slots, or the weapon is fully reloaded, or we've reached the reload speed
    int quantity = 0;
    final int originalSpeed = weapon.getReloadSpeed();
    for (int i = 0; i < indeces.size() && weaponNeedsAmmo(weapon) && quantity < originalSpeed; i++) {
      // weapon.reload will cause the inventory indeces to shift when a slot is exhausted
      quantity += weapon.reload(player, indeces.get(i) - i);

      // weapon.reload() doesn't realize we may be crossing ammo slot boundaries and will call it again as part of the same action
      weapon.setReloadSpeed(originalSpeed - quantity);
    }
    // Reset the weapon now that we're done
    weapon.setReloadSpeed(originalSpeed);

    if (quantity == 1) {
      return new Message(Command.RELOAD, null, "You reload 1 round into the " + weapon.getName() + ".", foreground, null);
    } else {
      return new Message(Command.RELOAD, null, "You reload " + quantity + " rounds into the " + weapon.getName() + ".", foreground, null);
    }
  }

  private boolean weaponNeedsAmmo(final Item weapon) {
    if (weapon == null) {
      return false;
    }

    return weapon.getCurrentCapacity() < weapon.getMaximumCapacity();
  }
}
