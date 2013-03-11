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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.command.CommandMapper;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.MutableCoordinate;
import com.callidusrobotics.object.AbstractGameObject;
import com.callidusrobotics.object.EquipableData;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.InventoriableData;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.ItemInventoryComparator;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.object.UseableData;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.Identifier;

@SuppressWarnings("PMD.TooManyMethods")
public abstract class AbstractActor extends AbstractGameObject implements Actor {
  public static final List<String> WND_DESCRIPTIONS = Collections.unmodifiableList(Arrays.asList("nearly dead", "severely wounded", "wounded", "grazed", "unhurt"));

  protected MutableCoordinate lastPosition;
  protected StatBlock statBlock;
  protected boolean leaveCorpse = true;
  protected String faction;
  protected final Map<EquipmentSlot, Item> equipment;
  protected List<String> woundDescriptions = WND_DESCRIPTIONS;

  /**
   * Constructor for meta objects that do not interact with the world, e.g.
   * Reticles.
   */
  protected AbstractActor(final ConsoleGraphic consoleGraphic, final Coordinate position, final String name, final String description) {
    super(consoleGraphic, name, description);

    this.equipment = Collections.emptyMap();
    this.lastPosition = new MutableCoordinate(position);
    this.position = new MutableCoordinate(position);
  }

  /**
   * Standard constructor for tangible objects, e.g. PlayerCharacter,
   * NonPlayerCharacter.
   */
  protected AbstractActor(final ConsoleGraphic consoleGraphic, final Coordinate position, final String name, final String description, final Size size, final String faction, final StatBlock statBlock) {
    super(consoleGraphic, position, name, description, size);

    lastPosition = new MutableCoordinate(position);
    this.faction = faction;
    this.statBlock = new StatBlock(statBlock);
    this.equipment = new HashMap<EquipmentSlot, Item>();
  }

  public String getNameThirdPerson() {
    if (isProperNoun()) {
      return getName();
    }

    return "the " + StringUtils.lowerCase(getName());
  }

  public final boolean hasMoved() {
    return !position.equals(lastPosition);
  }

  public void setWoundDescriptions(final List<String> woundDescriptions) {
    if (woundDescriptions == null || woundDescriptions.size() < WND_DESCRIPTIONS.size()) {
      return;
    }

    this.woundDescriptions = new ArrayList<String>(woundDescriptions.size());
    this.woundDescriptions.addAll(woundDescriptions);
  }

  public void setLeaveCorpse(final boolean leaveCorpse) {
    this.leaveCorpse = leaveCorpse;
  }

  @Override
  public String getDescription() {
    final StringBuffer description = new StringBuffer(64);

    // If this is a unique character, add the name
    if (isProperNoun()) {
      description.append(getName());
      description.append(". ");
    }

    // Append the static description of this
    description.append(super.getDescription());
    if (!StringUtils.isBlank(description)) {
      description.append(' ');
    }

    // Append the wound info
    final StatBlock stats = getCurrentStatBlock();
    int healthPercent = stats.currentHp * 100 / stats.maxHp;
    healthPercent = Math.min(100, Math.max(0, healthPercent));
    final String woundLevel = woundDescriptions.get(healthPercent / 25);
    if (!StringUtils.isBlank(woundLevel)) {
      description.append("It looks ");
      description.append(woundLevel);
      description.append('.');
    }

    // Append equipment info
    final Inventoriable weapon = getEquippedItem(EquipmentSlot.WEAPON);
    if (weapon != null) {
      description.append(" It's weilding a ");
      description.append(weapon.getName());
      description.append('.');
    }

    // Append size info
    if (size != null) {
      description.append(" (Size: ");
      description.append(size.toString().toLowerCase());
      description.append(')');
    }

    return description.toString();
  }

  @Override
  public String getFaction() {
    return faction;
  }

  @Override
  public void setPosition(final int row, final int col) {
    lastPosition.setPosition(position);
    position.setPosition(row, col);
  }

  @Override
  public void setPosition(final Coordinate position) {
    setPosition(position.getRow(), position.getCol());
  }

  @Override
  public void draw(final Console console) {
    console.print(getRow(), getCol(), getConsoleGraphic());
  }

  public Coordinate getLastPosition() {
    return lastPosition;
  }

  public StatBlock getCurrentStatBlock() {
    StatBlock currentStats = new StatBlock(statBlock);

    for (final Item item : equipment.values()) {
      if (item.getStatBlock() != null) {
        currentStats = currentStats.combine(item.getStatBlock());
      }
    }

    if (currentStats.currentHp > currentStats.maxHp) {
      currentStats.currentHp = currentStats.maxHp;
    }

    statBlock.currentHp = currentStats.currentHp;
    return currentStats;
  }

  public void modifyStatBlock(final StatBlock statModifiers) {
    statBlock = statBlock.combine(statModifiers);
    final StatBlock modifiedStats = getCurrentStatBlock();

    if (statBlock.currentHp > modifiedStats.maxHp) {
      statBlock.currentHp = modifiedStats.maxHp;
    }
  }

  public void setCurrentHp(final int currentHp) {
    final StatBlock currentStats = getCurrentStatBlock();
    if (currentStats.maxHp < currentHp) {
      statBlock.setCurrentHp(currentStats.maxHp);
    } else {
      statBlock.setCurrentHp(currentHp);
    }
  }

  public Item removeEquippedItem(final EquipmentSlot slot) {
    return equipment.remove(slot);
  }

  public Inventoriable getEquippedItem(final EquipmentSlot slot) {
    return equipment.get(slot);
  }

  public boolean equipItemInInventoryToEquipmentSlot(final int index, final EquipmentSlot slot) {
    final Item item = inventory.get(index);

    if (item.isEquipable(slot)) {
      inventory.remove(index);

      if (equipment.containsKey(slot)) {
        addItemToInventory(equipment.get(slot));
      }

      equipment.put(slot, item);
    }

    return item.isEquipable(slot);
  }

  @Override
  public List<Item> removeAllItemsFromInventory() {
    addItemsToInventory(new ArrayList<Item>(equipment.values()));
    equipment.clear();

    return super.removeAllItemsFromInventory();
  }

  protected boolean canMove(final DungeonLevel currentLevel, final Direction direction) {
    if (direction == null) {
      return false;
    }

    final Coordinate desiredPosition = direction.toCoordinate().increment(position);
    final boolean insideMap = currentLevel.checkCoordinatesRelative(desiredPosition);
    final boolean barrier = isBarrier(currentLevel, desiredPosition);
    final boolean onExitTile = currentLevel.getTileRelative(position).getExit() != null;

    return (insideMap && !barrier) || (!insideMap && onExitTile);
  }

  protected abstract boolean isBarrier(final DungeonLevel currentLevel, final Coordinate position);

  protected boolean move(final DungeonLevel currentLevel, final Command command) {
    if (command == null) {
      return false;
    }

    // Update the last position so hasMoved() will be accurate
    setPosition(position);

    final Direction desiredDirection = Direction.fromCommand(command);
    if (desiredDirection == null || !canMove(currentLevel, desiredDirection)) {
      return false;
    }

    final Coordinate desiredPosition = desiredPosition(command);
    final boolean isInsideMap = currentLevel.checkCoordinatesRelative(desiredPosition);
    if (isInsideMap) {
      // Simple movement inside the room with no collision
      setPosition(desiredPosition);
    } else {
      // Player is trying to back out of the room he just entered -- make the GameMediator think we just stepped on the exit tile
      final MutableCoordinate offset = desiredDirection.toCoordinate();
      setPosition(position.add(offset.multiply(-1)));
      setPosition(position.add(offset));
    }

    return true;
  }

  public MutableCoordinate desiredPosition(final Command command) {
    Validate.notNull(command);

    final Direction direction = Direction.fromCommand(command);
    if (direction == null) {
      return new MutableCoordinate(position);
    } else {
      return direction.toCoordinate().increment(position);
    }
  }

  @Override
  public Message processCommand(final DungeonLevel currentLevel, final Command command) {
    Validate.notNull(currentLevel);
    Validate.notNull(command);

    // Clear the last position so that hasMoved() is up to date
    setPosition(getPosition());

    final boolean moved = move(currentLevel, command);
    if (moved) {
      return new Message(command, null, null, null, null);
    }

    // The PlayerCharacter probably delegated something to us
    if (CommandMapper.NO_UPDATE_CMD_SET.contains(command) || command == Command.ESCAPE) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    return new Message(Command.REST, null, null, null, null);
  }

  public void sortInventory() {
    Collections.sort(inventory, new ItemInventoryComparator());
  }

  public Item makeCorpse() {
    if (!leaveCorpse) {
      return null;
    }

    // TODO: Refactor this to use the item factory?
    String name, description;
    if (isProperNoun()) {
      name = this.name + "'s";
      description = "The remains of " + this.name + ".";
    } else {
      name = this.name;
      description = "The remains of a " + this.name + ".";
    }

    final String identifier = Identifier.string2identifier(this.name + "_corpse");
    return new Item(identifier, '%', new ConsoleGraphic('%', getForeground(), getBackground()), 1,
        new InventoriableData(name + " Corpse", description, 1, 0),
        new UseableData(false, false, -1, null, null, null, null),
        new EquipableData(0, 0, null, null));
  }
}
