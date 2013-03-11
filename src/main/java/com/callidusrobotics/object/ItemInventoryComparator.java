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

package com.callidusrobotics.object;

/**
 * Sorts Item objects for organizing the inventory of the PlayerCharacter.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class ItemInventoryComparator extends AbstractItemComparator {
  private static final long serialVersionUID = 963522703371280918L;

  @Override
  public int getRank(final Item item) {
    if (item == null) {
      return Integer.MIN_VALUE;
    }

    // Put reloadable weapons at the top of the inventory
    if (item.isEquipable(EquipmentSlot.WEAPON) && item.getMaximumCapacity() > 0) {
      return 0;
    }

    // Put single-use and melee weapons below reloadable weapons
    if (item.isEquipable(EquipmentSlot.WEAPON)) {
      return 1;
    }

    // Put other equipment below weapons
    if (item.isEquipable()) {
      return 2;
    }

    // Put disposable items at the bottom (so the player can up-arrow to jump to them quickly)
    if (item.isDisposable()) {
      return 5;
    }

    // If it's not equippable but also not mergeable chances are the player cares about it more than other stuff
    if (!item.isMergeable()) {
      return 4;
    }

    // Everything else (e.g. ammo) goes in the middle
    return 3;
  }

  @SuppressWarnings("PMD.CompareObjectsWithEquals")
  @Override
  public int compare(final Item item1, final Item item2) {
    final int comparison = super.compare(item1, item2);

    // Items are of differing rank
    if (comparison != 0) {
      return comparison;
    }

    // Items are identical; don't bother to do a string comparison
    if (item1 == item2) {
      return 0;
    }

    // Items are of the same rank, now sort them alphabetically
    return item1.getName().compareTo(item2.getName());
  }
}
