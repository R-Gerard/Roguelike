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

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Equipable;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.attribute.Useable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.StatBlock;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleGraphic;

/**
 * Definition of in-game objects that can be stored in the inventory of other
 * in-game objects.
 *
 * @author Rusty
 * @since 0.0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class Item extends AbstractGameObject implements Inventoriable, Useable, Equipable {
  private final String identifier;
  private final char character;
  private int quantity, currentCapacity;
  private final InventoriableData inventoriableData;
  private final UseableData useableData;
  private final EquipableData equipableData;

  public Item(final String identifier, final char character, final ConsoleGraphic consoleGraphic, final int quantity, final InventoriableData inventoriableData, final UseableData useableData, final EquipableData equipableData) {
    super(consoleGraphic, inventoriableData.name, inventoriableData.description);

    this.identifier = identifier == null ? "" : identifier;
    this.character = character;
    this.quantity = inventoriableData.getMaxPerSlot() > 0 ? Math.min(inventoriableData.getMaxPerSlot(), Math.max(1, quantity)) : Math.max(1, quantity);
    this.currentCapacity = useableData.maximumCapacity;
    this.inventoriableData = inventoriableData;
    this.useableData = useableData;
    this.equipableData = equipableData;
  }

  public Item(final Item other) {
    super(other.consoleGraphic, other.name, other.description);

    this.identifier = other.identifier;
    this.character = other.character;
    this.quantity = other.quantity;
    this.currentCapacity = other.currentCapacity;
    this.inventoriableData = new InventoriableData(other.inventoriableData);
    this.useableData = new UseableData(other.useableData);
    this.equipableData = new EquipableData(other.equipableData);
  }

  @Override
  public Message use(final AbstractActor user, final DungeonLevel currentLevel) {
    if (useableData.disposable && quantity > 0) {
      // The user consumes the potion/food/whatever
      user.modifyStatBlock(useableData.getStatBlock());
      updateQuantity(user);
      return new Message(Command.USE, null, useableData.useMessage, getForeground(), null);
    }

    if (useableData.maximumCapacity > 0 && currentCapacity == 0) {
      return new Message(Command.USE, null, "The " + name + " is empty.", getForeground(), null);
    }

    // This item has no function just return the generic useMessage
    updateQuantity(user);
    return new Message(Command.USE, null, useableData.useMessage, getForeground(), null);
  }

  @Override
  public Message fire(final AbstractActor user, final DungeonLevel currentLevel) {
    if (useableData.maximumCapacity > 0 && currentCapacity == 0) {
      return new Message(Command.USE, null, "The " + name + " is empty.", getForeground(), null);
    }

    updateQuantity(user);
    return new Message(Command.USE, null, null, null, null);
  }

  protected void updateQuantity(final AbstractActor user) {
    if (useableData.disposable && quantity > 0) {
      final int index = getInventoryIndex(user);
      final EquipmentSlot slot = getEquipmentSlot(user);

      if (--quantity <= 0) {
        if (index > -1) {
          // Find this and remove it from the user's inventory
          user.removeItemFromInventory(index);
        } else if (slot != null) {
          user.removeEquippedItem(slot);
        }
      }
    } else if (useableData.maximumCapacity > 0 && currentCapacity > 0) {
      currentCapacity--;
    }
  }

  protected int getInventoryIndex(final AbstractActor user) {
    final List<Inventoriable> userInventory = user.getInventory();
    for (int i = 0; i < userInventory.size(); i++) {
      if (this == userInventory.get(i)) {
        return i;
      }
    }

    return -1;
  }

  protected EquipmentSlot getEquipmentSlot(final AbstractActor user) {
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      final Item equipedItem = (Item) user.getEquippedItem(slot);
      if (this == equipedItem) {
        return slot;
      }
    }

    return null;
  }

  @Override
  public boolean canAddItemToInventory(final Inventoriable item) {
    // Items can not have an inventory of other items
    return false;
  }

  @Override
  public int addItemToInventory(final Item item) {
    // Items can not have an inventory of other items
    throw new AssertionError("This does not have an inventory");
  }

  @Override
  public void merge(final Inventoriable other) {
    Validate.isTrue(isMergeable(), "This item can not be merged");

    if (other instanceof Item) {
      final Item otherItem = (Item) other;

      if (canMerge(otherItem)) {
        int numTransferred;
        if (getMaxPerSlot() > 0) {
          numTransferred = Math.min(getMaxPerSlot() - quantity, otherItem.quantity);
        } else {
          numTransferred = otherItem.quantity;
        }

        quantity += numTransferred;
        otherItem.quantity -= numTransferred;
      }
    }
  }

  @Override
  public boolean canMerge(final Inventoriable other) {
    if (other instanceof Item) {
      final Item otherItem = (Item) other;
      return isMergeable() && otherItem.isMergeable() && otherItem.name.equals(name) && otherItem.description.equals(description);
    }

    return false;
  }

  @Override
  public boolean isMergeable() {
    return inventoriableData.getMaxPerSlot() != 1;
  }

  @Override
  public int getMaxPerSlot() {
    return inventoriableData.getMaxPerSlot();
  }

  @Override
  public int getQuantity() {
    return quantity;
  }

  @Override
  public int getValue() {
    int value = inventoriableData.value * quantity;

    if (getCurrentCapacity() > 0 && getAmmunitionType() != null) {
      value += getCurrentCapacity() * getAmmunitionType().getValue();
    }

    return value;
  }

  @Override
  public void setQuantity(final int quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean isEquipable(final EquipmentSlot slot) {
    if (!isEquipable()) {
      return false;
    }

    return equipableData.getEquipmentSlots().contains(slot);
  }

  @Override
  public boolean isEquipable() {
    return !equipableData.getEquipmentSlots().isEmpty();
  }

  @Override
  public StatBlock getStatBlock() {
    if (isEquipable()) {
      return equipableData.getStatBlock();
    }

    if (isDisposable()) {
      return useableData.getStatBlock();
    }

    return new StatBlock(0, 0, 0, 0, 0, 0, 0);
  }

  @Override
  public boolean isDisposable() {
    return useableData.disposable;
  }

  @Override
  public boolean isLoadable() {
    return useableData.loadable;
  }

  @Override
  public int getCurrentCapacity() {
    if (isLoadable()) {
      return currentCapacity;
    }

    return -1;
  }

  @Override
  public void setCurrentCapacity(final int quantity) {
    Validate.isTrue(isLoadable(), getName() + " is not loadable.");
    Validate.inclusiveBetween(0, getMaximumCapacity(), quantity, "Invalid quantity (" + quantity + ") for " + getName() + ".");

    currentCapacity = quantity;
  }

  @Override
  public int getMaximumCapacity() {
    if (isLoadable()) {
      return useableData.maximumCapacity;
    }

    return -1;
  }

  @Override
  public int getRange() {
    return equipableData.range;
  }

  public int getEffectiveRange() {
    if (isLoadable()) {
      if (currentCapacity > 0) {
        return equipableData.range;
      }

      return -1;
    }

    if (isDisposable()) {
      if (quantity > 0) {
        return equipableData.range;
      }

      return -1;
    }

    return equipableData.range;
  }

  @Override
  public int getDamage() {
    return equipableData.damage;
  }

  public int getEffectiveDamage() {
    if (isLoadable()) {
      if (currentCapacity > 0) {
        return equipableData.damage;
      }

      return 1;
    }

    if (isDisposable()) {
      if (quantity > 0) {
        return equipableData.damage;
      }

      return 1;
    }

    return equipableData.damage;
  }

  @Override
  public Inventoriable getAmmunitionType() {
    return useableData.getAmmunitionType();
  }

  @Override
  public int getReloadSpeed() {
    return useableData.getReloadSpeed();
  }

  public void setReloadSpeed(final int reloadSpeed) {
    useableData.reloadSpeed = reloadSpeed;
  }

  @Override
  public int reload(final AbstractActor user, final int index) {
    final Item ammunition = (Item) user.getItemFromInventory(index);

    if (!isLoadable()) {
      return 0;
    }

    if (ammunition == null || !ammunition.equals(getAmmunitionType())) {
      return 0;
    }

    final int deficit = getMaximumCapacity() - getCurrentCapacity();
    final int available = Math.min(getReloadSpeed(), ammunition.getQuantity());
    final int amount = Math.min(deficit, available);

    currentCapacity += amount;
    ammunition.quantity -= amount;

    if (ammunition.quantity < 1) {
      user.removeItemFromInventory(index);
    }

    return amount;
  }

  @Override
  public void draw(final Console console) {
    console.print(getRow(), getCol(), getConsoleGraphic());
  }

  @Override
  public String getInventoryDescription() {
    final StringBuffer stringBuffer = new StringBuffer(64);

    stringBuffer.append(name);
    stringBuffer.append(" (");
    stringBuffer.append(character);
    stringBuffer.append("): ");
    stringBuffer.append(description);

    return stringBuffer.toString();
  }

  @Override
  public String toString() {
    final StringBuffer stringBuffer = new StringBuffer(64);

    stringBuffer.append(name);

    if (quantity > 1) {
      stringBuffer.append(" (");
      stringBuffer.append(quantity);
      stringBuffer.append(')');
    }

    if (useableData.maximumCapacity > 0) {
      stringBuffer.append(" [");
      stringBuffer.append(currentCapacity);
      stringBuffer.append('/');
      stringBuffer.append(useableData.maximumCapacity);
      stringBuffer.append(']');
    }

    return stringBuffer.toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Item) {
      final Item otherItem = (Item) other;

      return identifier.equals(otherItem.identifier);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return identifier.hashCode();
  }
}
