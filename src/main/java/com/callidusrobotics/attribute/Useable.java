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

import com.callidusrobotics.Message;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.StatBlock;

/**
 * Interface defining mutable objects that perform a routine when used.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Item
 */
public interface Useable extends Named {
  /**
   * Use the Item and cause some effect.
   *
   * @param user
   *          The one using this, not null
   * @param currentLevel
   *          The DungeonLevel the user occupies, not null
   * @return Message describing the effect that occurred, never null
   */
  Message use(AbstractActor user, DungeonLevel currentLevel);

  /**
   * Use the Item as a ranged attack weapon.
   *
   * @param user
   *          The one using this, not null
   * @param currentLevel
   *          The DungeonLevel the user occupies, not null
   * @return Message describing the effect that occurred, never null
   */
  Message fire(AbstractActor user, DungeonLevel currentLevel);

  /**
   * Disposability accessor.
   *
   * @return If this is exhausted after use
   */
  boolean isDisposable();

  /**
   * Loadability accessor.
   *
   * @return If the Item can be reloaded after use
   */
  boolean isLoadable();

  /**
   * Current capacity accessor. Returns -1 if this is not reloadable.
   *
   * @return The current capacity
   * @see #isLoadable()
   * @see #getMaximumCapacity()
   */
  int getCurrentCapacity();

  /**
   * Current capacity mutator.
   *
   * @param quantity
   *          The new capacity
   * @see #isLoadable()
   * @see #getMaximumCapacity()
   * @throws IllegalArgumentException
   *           if quantity is invalid
   */
  void setCurrentCapacity(int quantity);

  /**
   * Maximum capacity accessor. Returns -1 if this is not reloadable.
   *
   * @return The maximum capacity
   * @see #isLoadable()
   * @see #getCurrentCapacity()
   */
  int getMaximumCapacity();

  /**
   * Ammunition type accessor. Returns null if this is not reloadable.
   *
   * @return An instance of the type of ammunition this uses, or null
   * @see #use(AbstractActor, DungeonLevel)
   * @see #isLoadable()
   * @see #reload(AbstractActor, int)
   */
  Inventoriable getAmmunitionType();

  /**
   * Ammunition speed accessor. Returns the number of rounds loaded after each
   * successful call to {@link #reload(AbstractActor, int)}. The default reload
   * speed is 1.
   *
   * @return The maximum number of rounds loaded by one call to
   *         {@link #reload(AbstractActor, int)}
   * @see #isLoadable()
   * @see #reload(AbstractActor, int)
   */
  int getReloadSpeed();

  /**
   * Reload this with the ammunition provided. Decrements the ammunition's
   * quantity by an amount specified by {@link #getReloadSpeed()} and removes
   * the ammunition from the user's inventory if it is exhausted.
   *
   * @param user
   *          The one reloading this, not null
   * @param inventoryIndex
   *          The location in the user's inventory that has the ammunition
   * @return The quantity that was transferred from the inventory to this
   * @see #isLoadable()
   * @see #getCurrentCapacity()
   * @see #getMaximumCapacity()
   * @see #getAmmunitionType()
   * @see #getReloadSpeed()
   */
  int reload(AbstractActor user, int inventoryIndex);

  /**
   * StatBlock accessor.
   *
   * @return The StatBlock modifiers this bestows when used, never null
   */
  StatBlock getStatBlock();
}
