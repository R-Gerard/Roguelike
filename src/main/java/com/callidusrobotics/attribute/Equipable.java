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

package com.callidusrobotics.attribute;

import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.actor.StatBlock;

/**
 * Interface defining immutable objects that have an ongoing effect when worn or
 * wielded.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Item
 */
public interface Equipable extends Named {
  /**
   * Equipable accessor.
   *
   * @return The equipability of this
   */
  boolean isEquipable();

  /**
   * Equipable accessor.
   *
   * @param slot
   *          The desired slot to wear this
   * @return The Equipability of this in the specified slot
   */
  boolean isEquipable(EquipmentSlot slot);

  /**
   * Range accessor.
   *
   * @return The range of this when equipped
   */
  int getRange();

  /**
   * Damage accessor.
   *
   * @return The damage this inflicts when equipped
   */
  int getDamage();

  /**
   * StatBlock accessor.
   *
   * @return The StatBlock modifiers this bestows when equipped, never null
   */
  StatBlock getStatBlock();
}
