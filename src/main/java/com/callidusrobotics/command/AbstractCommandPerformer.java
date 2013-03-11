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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.GameMediator;
import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.AbstractGameObject;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.PlayerCharacter;

/**
 * Abstract class defining common methods for <code>CommandPerformer</code>
 * objects.
 *
 * @author Rusty
 * @since 0.0.1
 */
abstract class AbstractCommandPerformer implements CommandPerformer {
  protected GameMediator gameMediator;
  protected PlayerCharacter player;
  private Message message;

  @Override
  public final Message perform(final Command command) {
    before();
    message = performDelegate(command);
    after();

    return message;
  }

  public void setGameMediator(final GameMediator gameMediator) {
    this.gameMediator = gameMediator;
    player = gameMediator.getPlayer();

    Validate.notNull(player);
  }

  /**
   * Method to run before each call to {@link CommandPerformer#perform(Command)}
   * .
   */
  protected void before() {
    player.setPosition(player.getPosition());

    // Most commands will reset the player back to walk mode
    player.setWalkMode(true);
    if (player.isInTargetingMode()) {
      final DungeonLevel currentLevel = gameMediator.getCurrentLevel();
      player.getReticle().processCommand(currentLevel, Command.ESCAPE);
    }
  }

  /**
   * Method to run after each call to {@link CommandPerformer#perform(Command)}.
   */
  protected void after() {
    Validate.notNull(message);
  }

  /**
   * Entry-point for derived classes.
   *
   * @param command
   *          The <code>Command</code> to perform, not null
   * @return <code>Message</code> describing the perform result
   * @see #perform(Command)
   */
  protected abstract Message performDelegate(final Command command);

  /**
   * Prompts the user to select an item from an inventory.
   *
   * @param message
   *          The instructions on how the selection is performed, not null
   * @param source
   *          The owner of the inventory to select from, not null
   * @return The inventory index selected
   */
  protected int selectItem(final String message, final AbstractGameObject source) {
    Validate.notNull(message);
    Validate.notBlank(message);

    final List<String> items = new LinkedList<String>();
    for (final Inventoriable item : source.getInventory()) {
      items.add(item.toString());
    }

    return gameMediator.getConsoleColleague().select(message, items, true, false);
  }

  /**
   * Prompts the user to select an <code>EquipmentSlot</code> from an equipment
   * list.
   *
   * @param message
   *          The instructions on how the selection is performed, not null
   * @param source
   *          The owner of the equipment to select from, not null
   * @return The <code>EquipmentSlot</code> selected, nullable
   */
  protected EquipmentSlot selectEquipment(final String message, final AbstractActor source) {
    int padding = 0;
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      if (slot.toString().length() > padding) {
        padding = slot.toString().length();
      }
    }

    final List<String> items = new LinkedList<String>();
    for (final EquipmentSlot slot : EquipmentSlot.values()) {
      if (source.getEquippedItem(slot) == null) {
        items.add(StringUtils.rightPad(slot.toString(), padding + 1) + ": EMPTY");
      } else {
        items.add(StringUtils.rightPad(slot.toString(), padding + 1) + ":" + source.getEquippedItem(slot).toString());
      }
    }

    final int index = gameMediator.getConsoleColleague().select(message, items, true, true);
    if (index != -1) {
      return EquipmentSlot.values()[index];
    }

    return null;
  }

  /**
   * Transfers <code>Inventoriable</code> items from one inventory list to
   * another. Prompts the user for the <code>Item</code> to transfer and the
   * quantity to transfer if the quantity is not obvious.
   *
   * @param command
   *          The <code>Command</code> that triggered this request (e.g.
   *          {@link Command#GRABALL} or {@link Command#GRAB_PROMPT}), not null
   * @param message
   *          The instructions on how the selection is performed, not null
   * @param transferAll
   *          If true, automatically transfers the maximum allowable amount,
   *          otherwise prompts the user for the quantity to transfer if the
   *          available quantity is greater than 1
   * @param source
   *          The owner of the inventory to select from, not null
   * @param destination
   *          The owner of the inventory to transfer to, not null
   * @return <code>Message</code> describing the result of the user's selection,
   *         never null
   */
  protected Message transferInventoryItem(final Command command, final String message, final boolean transferAll, final AbstractGameObject source, final AbstractGameObject destination) {
    final int index = selectItem(message, source);
    if (index == -1) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    final Color foreground = gameMediator.getConsoleColleague().getFontForeground();
    final Inventoriable temp = new Item((Item) source.getItemFromInventory(index));
    final int maxVal = temp.getQuantity();
    temp.setQuantity(1);

    if (!destination.canAddItemToInventory(temp)) {
      return new Message(Command.UNKNOWN, null, "There is no room to put that there.", foreground, null);
    }

    final int quantity = transferAll || maxVal == 1 ? maxVal : Math.min(Math.max(gameMediator.getConsoleColleague().getQuantity(maxVal, 4), 0), maxVal);
    if (quantity < 1) {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }

    temp.setQuantity(quantity);
    if (destination.canAddItemToInventory(temp)) {
      destination.transferItemToInventory(source, index, quantity);
      return new Message(command, null, quantity + ": " + temp.getName(), foreground, null);
    } else {
      return new Message(Command.UNKNOWN, null, "There is not enough room to put that many there.", foreground, null);
    }
  }
}
