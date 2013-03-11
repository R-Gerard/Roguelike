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

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.attribute.Categorized;
import com.callidusrobotics.attribute.Colorable;
import com.callidusrobotics.attribute.Drawable;
import com.callidusrobotics.attribute.Equipable;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.attribute.Named;
import com.callidusrobotics.attribute.Positionable;
import com.callidusrobotics.attribute.Useable;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.MutableCoordinate;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.swing.MutableConsoleGraphic;
import com.callidusrobotics.util.Identifier;

/**
 * The superclass of all in-game objects.
 *
 * @author Rusty
 * @since 0.0.1
 */
public abstract class AbstractGameObject implements Drawable, Positionable, Colorable, Named, Categorized {
  protected MutableConsoleGraphic consoleGraphic;
  protected MutableCoordinate position = null;
  protected String name, description;
  protected boolean properNoun = false;
  protected Size size = null;
  protected List<Item> inventory = new LinkedList<Item>();

  protected AbstractGameObject(final ConsoleGraphic consoleGraphic, final String name, final String description) {
    Validate.notNull(consoleGraphic);
    Validate.notNull(name);
    Validate.notNull(description);

    this.consoleGraphic = new MutableConsoleGraphic(consoleGraphic);
    this.position = new MutableCoordinate(-1, -1);
    this.name = name;
    this.description = description;
  }

  protected AbstractGameObject(final ConsoleGraphic consoleGraphic, final Coordinate position, final String name, final String description) {
    this(consoleGraphic, name, description);
    Validate.notNull(position);

    this.position = new MutableCoordinate(position);
  }

  protected AbstractGameObject(final ConsoleGraphic consoleGraphic, final Coordinate position, final String name, final String description, final Size size) {
    this(consoleGraphic, position, name, description);

    this.size = size;
  }

  @Override
  public Size getSize() {
    if (size == null) {
      return Size.MICROSCOPIC;
    }

    return size;
  }

  @Override
  public void setSize(final Size size) {
    this.size = size;
  }

  /**
   * ConsoleGraphic accessor.
   *
   * @return An immutable reference to the ConsoleGraphic attribute
   */
  public ConsoleGraphic getConsoleGraphic() {
    return consoleGraphic;
  }

  /**
   * ConsoleGraphic mutator.
   *
   * @param consoleGraphic
   *          The new appearance to use for this
   */
  public final void setConsoleGraphic(final ConsoleGraphic consoleGraphic) {
    Validate.notNull(consoleGraphic);
    this.consoleGraphic = new MutableConsoleGraphic(consoleGraphic);
  }

  @Override
  public Color getForeground() {
    return consoleGraphic.getForeground();
  }

  @Override
  public Color getBackground() {
    return consoleGraphic.getBackground();
  }

  @Override
  public void setForeground(final Color foreground) {
    consoleGraphic.setForeground(foreground);
  }

  @Override
  public void setBackground(final Color background) {
    consoleGraphic.setBackground(background);
  }

  @Override
  public Coordinate getPosition() {
    return position;
  }

  @Override
  public void setPosition(final int row, final int col) {
    position.setPosition(row, col);
  }

  @Override
  public void setPosition(final Coordinate position) {
    this.position.setPosition(position);
  }

  @Override
  public int getRow() {
    return position.getRow();
  }

  @Override
  public int getCol() {
    return position.getCol();
  }

  @Override
  public String getId() {
    // This implies that every display-name is unique
    return Identifier.string2identifier(name);
  }

  @Override
  public String getName() {
    if (StringUtils.isBlank(name)) {
      return "";
    }

    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  /**
   * ProperNoun accessor. Dictates the appropriate use of {@link #getName()}
   * when forming English sentences.
   *
   * @return Whether the name of this is a proper or common noun.
   * @see #getName()
   */
  public boolean isProperNoun() {
    return properNoun;
  }

  /**
   * ProperNoun mutator.
   *
   * @param properNoun
   *          If true, the name of this is proper, otherwise common
   * @see #getName()
   */
  public void setIsProperNoun(final boolean properNoun) {
    this.properNoun = properNoun;
  }

  @Override
  public String getDescription() {
    if (StringUtils.isBlank(description)) {
      return "";
    }

    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return name;
  }

  @SuppressWarnings("unchecked")
  public List<Inventoriable> getInventory() {
    return Collections.unmodifiableList((List<Inventoriable>) (List<?>) inventory);
  }

  public int getInventoryValue() {
    int total = 0;
    for (final Item item : inventory) {
      total += item.getValue();
    }

    return total;
  }

  public abstract boolean canAddItemToInventory(Inventoriable item);

  public void transferItemToInventory(final AbstractGameObject other, final int index, final int quantity) {
    Item item = (Item) other.getItemFromInventory(index);

    if (item.getQuantity() > quantity) {
      item.setQuantity(item.getQuantity() - quantity);
      item = new Item(item);
      item.setQuantity(quantity);
    } else {
      other.removeItemFromInventory(index);
    }

    addItemToInventory(item);
  }

  /**
   * Adds an item to the inventory.
   *
   * @param item
   *          The item to insert, nullable
   * @return The index of the item in the inventory
   */
  public int addItemToInventory(final Item item) {
    if (item == null) {
      return -1;
    }

    int index = 0;
    boolean merged = false;
    if (item.isMergeable()) {
      for (final Item other : inventory) {
        if (other.canMerge(item)) {
          other.merge(item);
          // The item may have been partially merged (quantity1 + quantity2 > maxPerSlot)
          if (item.getQuantity() == 0) {
            merged = true;
            return index;
          }
        }
        index++;
      }
    }

    // We assume canAddItemToInventory has been called
    if (!merged) {
      inventory.add(0, item);
      return 0;
    }

    return -1;
  }

  /**
   * Convenience method to add a collection of Item objects to this inventory.
   *
   * @param items
   *          The items to add, nullable
   * @see #addItemToInventory(Item)
   */
  public void addItemsToInventory(final List<Item> items) {
    if (items == null) {
      return;
    }

    for (final Item item : items) {
      addItemToInventory(item);
    }
  }

  /**
   * Returns a list of indeces corresponding to Item objects that match the
   * parameter in this object's Inventory.
   *
   * @param other
   *          The type of Item to search for in the Inventory
   * @return A list of indeces, never null
   */
  public List<Integer> getItemIndecesFromInventory(final Inventoriable other) {
    if (other == null) {
      return Collections.emptyList();
    }

    final List<Integer> indeces = new LinkedList<Integer>();
    for (int i = 0; i < inventory.size(); i++) {
      if (inventory.get(i).equals(other)) {
        indeces.add(i);
      }
    }

    return indeces;
  }

  public Inventoriable getItemFromInventory(final int index) {
    return inventory.get(index);
  }

  public Equipable getEquipmentFromInventory(final int index) {
    return inventory.get(index);
  }

  public Useable getToolFromInventory(final int index) {
    return inventory.get(index);
  }

  public Item removeItemFromInventory(final int index) {
    return inventory.remove(index);
  }

  public List<Item> removeAllItemsFromInventory() {
    final List<Item> stuff = new LinkedList<Item>();
    stuff.addAll(inventory);
    inventory.clear();
    return stuff;
  }
}
