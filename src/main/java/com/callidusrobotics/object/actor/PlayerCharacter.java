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

package com.callidusrobotics.object.actor;

import java.awt.Color;
import java.util.List;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.MutableConsoleGraphic;

/**
 * Maintains the internal state of the Actor controlled by the user.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class PlayerCharacter extends AbstractActor {
  protected boolean walkMode = true;
  protected AbstractReticle reticle;

  public PlayerCharacter(final Color color, final StatBlock statBlock, final Coordinate position) {
    super(new MutableConsoleGraphic('@', color, null), position, "Player", "You.", Size.MEDIUM, ActorFactionData.PLAYER_FACTION, statBlock);
  }

  @Override
  public boolean canAddItemToInventory(final Inventoriable item) {
    if (item == null) {
      return inventory.size() < getMaxInventorySlots();
    }

    // There is a slot available
    if (inventory.size() < getMaxInventorySlots()) {
      return true;
    }

    // There are no slots available, check if the item will fit in an occupied slot
    if (inventory.contains(item) && item.isMergeable()) {
      if (item.getMaxPerSlot() < 1) {
        return true;
      }

      int available = 0;
      final List<Integer> indeces = getItemIndecesFromInventory(item);
      for (final int i : indeces) {
        available += Math.max(0, item.getMaxPerSlot() - inventory.get(i).getQuantity());
      }

      return available >= item.getQuantity();
    }

    return false;
  }

  @Override
  public String getNameThirdPerson() {
    return "the player";
  }

  @Override
  public void draw(final Console console) {
    if (isInTargetingMode()) {
      if (walkMode) {
        reticle.hide();
      }

      reticle.draw(console);
    }

    console.print(getRow(), getCol(), consoleGraphic);
  }

  private int getAmmo() {
    final Item weapon = equipment.get(EquipmentSlot.WEAPON);
    if (weapon != null && weapon.getMaximumCapacity() > 0) {
      return weapon.getCurrentCapacity();
    }

    if (weapon != null && weapon.getQuantity() > 0) {
      return weapon.getQuantity();
    }

    return -1;
  }

  @Override
  public void setLeaveCorpse(final boolean leaveCorpse) {
    // The player must always leave a corpse
    this.leaveCorpse = true;
  }

  public void setWalkMode(final boolean walkMode) {
    this.walkMode = walkMode;
  }

  public boolean isInWalkMode() {
    return walkMode;
  }

  public Message createReticle(final DungeonLevel currentLevel, final Command command) {
    final Item weapon = equipment.get(EquipmentSlot.WEAPON);
    final int weaponRange = weapon == null ? -1 : weapon.getRange();
    if (weaponRange <= 0) {
      return new Message(Command.UNKNOWN, null, "Try equipping a ranged weapon.", getForeground(), null);
    }

    if (getAmmo() <= 0) {
      return new Message(Command.UNKNOWN, null, "Try reloading your weapon.", getForeground(), null);
    }

    walkMode = false;
    reticle = new TargetingReticle(currentLevel, position, weaponRange, getSize());
    return new Message(Command.UNKNOWN, null, null, null, null);
  }

  public AbstractReticle getReticle() {
    return reticle;
  }

  public int getMaxInventorySlots() {
    return getCurrentStatBlock().getStrength() + 5;
  }

  public boolean isInTargetingMode() {
    return reticle != null && reticle.isVisible();
  }

  @Override
  protected boolean isBarrier(final DungeonLevel currentLevel, final Coordinate position) {
    return !currentLevel.isVacantRelative(position);
  }
}
