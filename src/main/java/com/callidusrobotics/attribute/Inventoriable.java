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

import com.callidusrobotics.object.Item;

/**
 * Interface defining mutable objects with quantities.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Item
 */
public interface Inventoriable extends Named {
  /**
   * Specialized description method.
   *
   * @return Inventory description, never null
   */
  String getInventoryDescription();

  /**
   * Quantity accessor.
   *
   * @return The quantity
   */
  int getQuantity();

  /**
   * Quantity mutator.
   *
   * @param quantity
   *          The new quantity
   * @see #getMaxPerSlot()
   */
  void setQuantity(int quantity);

  /**
   * Value accessor.
   *
   * @return The monetary value
   */
  int getValue();

  /**
   * Mergeable accessor.
   *
   * @return The mergeability of this
   * @see #canMerge(Inventoriable)
   * @see #merge(Inventoriable)
   */
  boolean isMergeable();

  /**
   * Max quantity accessor. Returns the maximum quantity that can be stored
   * together. -1 indicates an unlimited number can be stored.
   *
   * @return The maximum quantity of this
   * @see #getQuantity()
   * @see #setQuantity(int)
   */
  int getMaxPerSlot();

  /**
   * Mergeable comparator.
   *
   * @param other
   *          The object to check compatibility with, nullable
   * @return The compatibility
   * @see #isMergeable()
   * @see #merge(Inventoriable)
   */
  boolean canMerge(Inventoriable other);

  /**
   * Merges another Inventoriable object into this if they are compatible and
   * mergeable. Adds the other's quantity to this object's quantity (subject to
   * maxPerSlot) and decrements the other's quantity.
   * <p>
   * If the other's quantity is zero the Item should be deleted.
   *
   * @param other
   *          The object to merge, nullable
   * @see #isMergeable()
   * @see #canMerge(Inventoriable)
   * @see #getMaxPerSlot()
   */
  void merge(Inventoriable other);
}
