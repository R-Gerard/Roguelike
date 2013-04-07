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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.ItemComparator;
import com.callidusrobotics.object.ItemValueComparator;

/**
 * Defines behavior for a NonPlayerCharacter that is able to equip items.
 *
 * @author Rusty
 * @since 0.0.3
 */
@Behaves(Behavior.EQUIPMENT_USER)
class AiStrategyEquipmentUser extends AbstractAiStrategy {
  private final Item[] optimalEquipment = new Item[EquipmentSlot.values().length];
  private final int[] inventoryIndeces = new int[EquipmentSlot.values().length];
  private final ItemComparator[] comparators = new ItemComparator[EquipmentSlot.values().length];

  private AbstractActor opponent;
  private boolean shouldPerformScan = true;

  static class WeaponComparator implements ItemComparator {
    AbstractActor self;

    public WeaponComparator(final AbstractActor self) {
      this.self = self;
    }

    @Override
    public int compare(final Item item1, final Item item2) {
      throw new UnsupportedOperationException("This method is not implemented");
    }

    @Override
    public int getRank(final Item item) {
      if (item == null) {
        return -1;
      }

      // Favor ranged weapons
      if (weaponIsUseableAtRange(item)) {
        return item.getRange() * item.getDamage();
      }

      // Melee weapon
      if (item.getRange() < 2) {
        return item.getDamage();
      }

      // Unloaded ranged weapon
      return 1;
    }

    public boolean weaponIsUseableAtRange(final Item weapon) {
      // Check if this is even a ranged weapon
      if (weapon == null || weapon.getRange() < 2) {
        return false;
      }

      // Check if we have ammo for it
      if (weapon.isLoadable()) {
        if (weapon.getCurrentCapacity() > 0) {
          return true;
        }

        if (!self.getItemIndecesFromInventory(weapon.getAmmunitionType()).isEmpty()) {
          return true;
        }
      }

      // If weapon is throwable
      if (!weapon.isLoadable() && weapon.getQuantity() > 0) {
        return true;
      }

      return false;
    }
  }

  public AiStrategyEquipmentUser() {
    super();

    // Assume that higher-valued items are better
    final ItemComparator valueComparator = new ItemValueComparator();
    for (int i = 0; i < comparators.length; i++) {
      comparators[i] = valueComparator;
    }

    // Use a custom comparator for evaluating weapons to account for things like ammo availability
    comparators[EquipmentSlot.WEAPON.ordinal()] = new WeaponComparator(self);
  }

  @Override
  public void setSelf(final NonPlayerCharacter self) {
    super.setSelf(self);

    final WeaponComparator comparator = (WeaponComparator) comparators[EquipmentSlot.WEAPON.ordinal()];
    comparator.self = self;
  }

  @Override
  public void init() {
    super.init();

    shouldPerformScan = true;
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      setOptimalItem(slot, null, -1);
    }
  }

  @Override
  protected Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    final Item currentWeapon = getOptimalItem(EquipmentSlot.WEAPON);

    // Check if weapon is readied
    if (currentWeapon != null) {
      if (shouldSlotBeEquipped(EquipmentSlot.WEAPON)) {
        shouldPerformScan = true;
        return equipOrDeEquipItem(Command.EQUIP, EquipmentSlot.WEAPON);
      }

      if (shouldReloadWeapon(currentWeapon)) {
        shouldPerformScan = true;
        return reloadWeapon();
      }
    }

    if (opponent == null && self.getEquippedItem(EquipmentSlot.WEAPON) != null) {
      shouldPerformScan = true;
      return equipOrDeEquipItem(Command.UNEQUIP, EquipmentSlot.WEAPON);
    }

    // Equip other items
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      if (shouldSlotBeEquipped(slot)) {
        shouldPerformScan = true;
        return equipOrDeEquipItem(Command.EQUIP, slot);
      }
    }

    return new Message(Command.REST, null, null, null, null);
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    opponent = getNearestOpponent(factionData, currentLevel);
    scanInventory();

    // Check if any items need to be equipped
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      if (shouldSlotBeEquipped(slot)) {
        return max;
      }
    }

    if (shouldReloadWeapon(getOptimalItem(EquipmentSlot.WEAPON))) {
      return max;
    }

    // Remove weapons if there are no dangers
    if (opponent == null && self.getEquippedItem(EquipmentSlot.WEAPON) != null) {
      return max;
    }

    return min;
  }

  protected Item getOptimalItem(final EquipmentSlot slot) {
    return optimalEquipment[slot.ordinal()];
  }

  protected int getInventoryIndex(final EquipmentSlot slot) {
    return inventoryIndeces[slot.ordinal()];
  }

  protected void setOptimalItem(final EquipmentSlot slot, final Item item, final int index) {
    optimalEquipment[slot.ordinal()] = item;
    inventoryIndeces[slot.ordinal()] = index;
  }

  protected boolean shouldReloadWeapon(final Item weapon) {
    if (weapon == null || !weapon.isLoadable()) {
      return false;
    }

    final boolean needsReloading = weapon.getMaximumCapacity() - weapon.getCurrentCapacity() >= weapon.getReloadSpeed();
    final boolean canReload = !self.getItemIndecesFromInventory(weapon.getAmmunitionType()).isEmpty();

    return needsReloading && canReload;
  }

  protected boolean shouldSlotBeEquipped(final EquipmentSlot slot) {
    if (slot == EquipmentSlot.WEAPON && opponent == null) {
      return false;
    }

    // Test if the best item is already equipped to the chosen slot
    final Item bestItem = getOptimalItem(slot);
    return bestItem != null && !bestItem.equals(self.getEquippedItem(slot));
  }

  protected void scanInventory() {
    // Scan every equipment slot and check if there is an item in the inventory that is better than what is currently equipped
    if (shouldPerformScan) {
      shouldPerformScan = false;
      for (final EquipmentSlot slot : EquipmentSlot.values()) {
        selectBestItemForSlot(slot);
      }
    }
  }

  protected Item selectBestItemForSlot(final EquipmentSlot slot) {
    final ItemComparator comparator = comparators[slot.ordinal()];
    Item bestItem = (Item) self.getEquippedItem(slot);
    int maxValue = comparator.getRank(bestItem);
    int maxIndex = -1;

    int index = 0;
    for (final Inventoriable inventoriable : self.getInventory()) {
      final Item item = (Item) inventoriable;
      final int value = comparator.getRank(item);
      if (item.isEquipable(slot) && value > maxValue) {
        bestItem = item;
        maxValue = value;
        maxIndex = index;
      }

      index++;
    }

    setOptimalItem(slot, bestItem, maxIndex);
    return bestItem;
  }

  private Message equipOrDeEquipItem(final Command action, final EquipmentSlot slot) {
    // Don't call addItemToInventory(oldItem) until we are done equipping
    // (If we remove an equipped item and immediately re-insert it into the inventory it might shift our desired item index)
    Item oldItem = null;
    if (self.getEquippedItem(slot) != null) {
      oldItem = self.removeEquippedItem(slot);
    }

    if (action == Command.UNEQUIP && oldItem != null) {
      self.addItemToInventory(oldItem);
      final String description = StringUtils.isBlank(slot.getDescription()) ? oldItem.getName().toLowerCase() : slot.getDescription();
      return new Message(Command.UNEQUIP, null, StringUtils.capitalize(self.getNameThirdPerson()) + " unequipped a " + description + ".", self.getForeground(), null);
    }

    final int currentItemIndex = getInventoryIndex(slot);
    final boolean equipped = self.equipItemInInventoryToEquipmentSlot(currentItemIndex, slot);
    if (equipped) {
      if (oldItem != null) {
        self.addItemToInventory(oldItem);
      }
      final String description = StringUtils.isBlank(slot.getDescription()) ? self.getEquippedItem(slot).getName().toLowerCase() : slot.getDescription();
      return new Message(Command.EQUIP, null, StringUtils.capitalize(self.getNameThirdPerson()) + " equipped a " + description + ".", self.getForeground(), null);
    }

    return new Message(Command.REST, null, null, null, null);
  }

  private Message reloadWeapon() {
    final Item currentWeapon = getOptimalItem(EquipmentSlot.WEAPON);
    final List<Integer> indeces = self.getItemIndecesFromInventory(currentWeapon.getAmmunitionType());
    final int index = indeces.isEmpty() ? -1 : indeces.get(0);
    if (index != -1) {
      final int quantity = currentWeapon.reload(self, index);
      if (quantity == 1) {
        return new Message(Command.RELOAD, null, StringUtils.capitalize(self.getNameThirdPerson()) + " reloads 1 round into its weapon.", self.getForeground(), null);
      } else {
        return new Message(Command.RELOAD, null, StringUtils.capitalize(self.getNameThirdPerson()) + " reloads " + quantity + " rounds into its weapon.", self.getForeground(), null);
      }
    }

    return new Message(Command.REST, null, null, null, null);
  }
}
