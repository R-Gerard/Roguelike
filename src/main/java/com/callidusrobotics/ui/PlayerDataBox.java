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

package com.callidusrobotics.ui;

import java.awt.Color;

import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.object.actor.StatBlock;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.util.TrueColor;

/**
 * Displays information about the PlayerCharacter on the Console.
 *
 * @author Rusty
 * @since 0.0.1
 * @see PlayerCharacter
 */
public class PlayerDataBox implements ConsoleTextBox {
  private final AbstractConsoleTextBox consoleTextBox;
  private final PlayerCharacter player;

  public PlayerDataBox(final PlayerCharacter player, final AbstractConsoleTextBox consoleTextBox) {
    this.player = player;
    this.consoleTextBox = consoleTextBox;
  }

  @Override
  public void draw(final Console console) {
    consoleTextBox.draw(console);

    if (player == null) {
      return;
    }

    Color hpColor;
    final StatBlock statBlock = player.getCurrentStatBlock();

    // TODO: Parameterize the percentages
    if (statBlock.getCurrentHp() * 100 / statBlock.getMaxHp() >= 90) {
      hpColor = TrueColor.LIME;
    } else if (statBlock.getCurrentHp() * 100 / statBlock.getMaxHp() >= 50) {
      hpColor = TrueColor.YELLOW;
    } else {
      hpColor = TrueColor.RED;
    }

    final int internalRow = consoleTextBox.getInternalRow();
    final int internalCol = consoleTextBox.getInternalCol() + 1;
    final int internalWidth = consoleTextBox.getInternalWidth();
    final Color foreground = consoleTextBox.getForeground();
    final Color background = consoleTextBox.getBackground();

    console.print(internalRow + 2, internalCol, " HP: ", foreground, background);
    console.print(internalRow + 2, internalCol + 5, Integer.toString(statBlock.getCurrentHp()), hpColor, background);
    console.print(internalRow + 2, internalCol + 5 + Integer.toString(statBlock.getCurrentHp()).length(), "/" + Integer.toString(statBlock.getMaxHp()), foreground, background);

    console.print(internalRow + 4, internalCol, " STR: " + statBlock.getStrength(), foreground, background);
    console.print(internalRow + 5, internalCol, " AGI: " + statBlock.getAgility(), foreground, background);
    console.print(internalRow + 6, internalCol, " INT: " + statBlock.getIntelligence(), foreground, background);

    console.print(internalRow + 8, internalCol, " DEFENSE: " + statBlock.getDefense(), foreground, background);
    console.print(internalRow + 9, internalCol, " SPEED:   " + statBlock.getSpeed(), foreground, background);

    final Inventoriable weapon = player.getEquippedItem(EquipmentSlot.WEAPON);
    final String weaponString = weapon == null ? "NO WEAPON EQUIPPED" : weapon.toString().substring(0, Math.min(internalWidth, weapon.toString().length()));
    console.print(internalRow + 11, internalCol + 1, EquipmentSlot.WEAPON + ":", foreground, background);
    console.print(internalRow + 11, internalCol + 1, weaponString, foreground, background);
  }

  @Override
  public Coordinate getPosition() {
    return consoleTextBox.getPosition();
  }

  @Override
  public void setPosition(final int row, final int col) {
    consoleTextBox.setPosition(row, col);
  }

  @Override
  public void setPosition(final Coordinate position) {
    consoleTextBox.setPosition(position);
  }

  @Override
  public int getRow() {
    return consoleTextBox.getRow();
  }

  @Override
  public int getCol() {
    return consoleTextBox.getCol();
  }

  @Override
  public int getHeight() {
    return consoleTextBox.getHeight();
  }

  @Override
  public int getWidth() {
    return consoleTextBox.getWidth();
  }
}
