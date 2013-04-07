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

import java.util.Arrays;

import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleFactory;

/**
 * Defines behavior for a NonPlayerCharacter that is hostile to others and
 * attacks with ranged weapons. (Typically the player, but also other
 * NonPlayerCharacters.) A NonPlayerCharacter with this AiStrategy may be
 * friendly to the player and hostile to other NonPlayerCharacters.
 * <p>
 * This AiStrategy is usually paired with {@link AiStrategyEquipmentUser} to
 * equip weapons and reload them when they are exhausted.
 *
 * @author Rusty
 * @since 0.0.3
 */
@Behaves(Behavior.RANGED)
public class AiStrategyRanged extends AbstractAiStrategy {
  private Item currentWeapon = null;
  private AbstractActor opponent = null;

  @Override
  protected Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    if (opponent == null || currentWeapon == null) {
      return new Message(Command.REST, null, null, null, null);
    }

    // TODO: Check if opponent is too close and move away

    // If currentWeapon is not null that means it is ready to fire
    final TargetingReticle reticle = new TargetingReticle(currentLevel, self.getPosition(), currentWeapon.getRange(), self.getSize());
    reticle.setPosition(opponent.getPosition());
    if (reticle.isInLineOfSight()) {
      return fireWeapon(currentLevel, reticle);
    }

    // Opponent is out of range
    final Command direction = getDirection(opponent.getPosition(), currentLevel);
    return self.processCommand(currentLevel, direction);
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    // TODO: Get strongest opponent in range instead of nearest opponent
    opponent = getNearestOpponent(factionData, currentLevel);
    if (opponent == null) {
      return min;
    }

    currentWeapon = getWeapon();
    if (currentWeapon == null) {
      return min;
    }

    final int percentHp = getPercentHpRemaining();
    return base + multiplier * percentHp;
  }

  /**
   * Checks if the NPC has a weapon equipped, and it is ranged, and it is
   * loaded. Returns null if any of these conditions are not met.
   *
   * @param self
   *          The NPC attempting to use a ranged weapon
   * @return The NPC's equipped weapon or null
   */
  private Item getWeapon() {
    final Item weapon = (Item) self.getEquippedItem(EquipmentSlot.WEAPON);
    if (weapon == null) {
      return null;
    }

    if (weapon.getRange() < 2) {
      return null;
    }

    if (weapon.isLoadable() && weapon.getCurrentCapacity() == 0) {
      return null;
    }

    return weapon;
  }

  private Message fireWeapon(final DungeonLevel currentLevel, final TargetingReticle reticle) {
    if (reticle.isInLineOfSight()) {
      animate(currentLevel, reticle);
      return new Message(Command.ATTACKRANGED, Arrays.asList(opponent.getPosition()), null, null, null);
    }

    return new Message(Command.REST, null, null, null, null);
  }

  private void animate(final DungeonLevel currentLevel, final TargetingReticle reticle) {
    final Console console = ConsoleFactory.getInstance();
    final long animationSpeed = console.getAnimationSpeed();
    final boolean selfVisible = currentLevel.getTileRelative(self.getPosition()).isVisible();
    final boolean opponentVisible = currentLevel.getTileRelative(opponent.getPosition()).isVisible();

    for (final NonPlayerCharacter npc : currentLevel.getNonPlayerCharacters()) {
      if (currentLevel.getTileRelative(npc.getPosition()).isVisible()) {
        npc.draw(console);
      }
    }

    if (currentLevel.getPlayer() != null) {
      currentLevel.getPlayer().draw(console);
    }

    if (selfVisible || opponentVisible) {
      reticle.draw(console);
      console.render();

      final long start = System.currentTimeMillis();
      while (System.currentTimeMillis() - start < animationSpeed) {
        Thread.yield();
      }

      reticle.hide();
      reticle.draw(console);
    }

    console.render();
  }
}
