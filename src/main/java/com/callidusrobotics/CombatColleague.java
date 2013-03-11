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

package com.callidusrobotics;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.EquipmentSlot;
import com.callidusrobotics.object.Item;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.StatBlock;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.TrueColor;

public final class CombatColleague {

  private final static Random RANDOM = new Random();

  private CombatColleague() {
    throw new NotInstantiableError();
  }

  public static Message attack(final AbstractActor attacker, final AbstractActor defender, final Command command, final DungeonLevel currentLevel) {
    // Quick and dirty attack system
    final int toHitRoll = 1 + RANDOM.nextInt(20);
    final StatBlock attackerStats = attacker.getCurrentStatBlock();
    final StatBlock defenderStats = defender.getCurrentStatBlock();
    final String attackerName = StringUtils.capitalize(attacker.getName());
    final String defenderName = defender.getName();
    Message message = new Message(command, Arrays.asList(defender.getPosition()), attackerName + " misses " + defenderName + ".", attacker.getForeground(), TrueColor.BLACK);

    final int damageMean = getDamage(attacker, command);
    final int damageStdev = 1 + damageMean / 4;
    final int damageRoll = (int) (RANDOM.nextGaussian() * damageStdev) + damageMean;

    if (toHitRoll == 20) {
      // Automatic hit
      defender.setCurrentHp(defenderStats.getCurrentHp() - damageRoll);
      message = new Message(command, Arrays.asList(defender.getPosition()), attackerName + " critically strikes " + defenderName + ".", TrueColor.BLACK, TrueColor.RED);
    } else if (toHitRoll != 1 && toHitRoll + attackerStats.getAgility() > defenderStats.getAgility()) {
      // Chance to hit
      final int damage = Math.max(0, damageRoll - defenderStats.getDefense());
      defender.setCurrentHp(defenderStats.getCurrentHp() - damage);

      if (damage > 0) {
        message = new Message(command, Arrays.asList(defender.getPosition()), attackerName + " strikes " + defenderName + ".", attacker.getForeground(), TrueColor.BLACK);
      } else {
        message = new Message(command, Arrays.asList(defender.getPosition()), attackerName + " lands a glancing blow against " + defenderName + ".", attacker.getForeground(), TrueColor.BLACK);
      }
    }

    final Item weapon = getWeapon(attacker);
    if (weapon != null) {
      weapon.fire(attacker, currentLevel);
    }

    return message;
  }

  private static int getDamage(final AbstractActor actor, final Command command) {
    final Item weapon = getWeapon(actor);
    if (weapon == null) {
      return actor.getCurrentStatBlock().getStrength() + 1;
    }

    if (command == Command.ATTACKMELEE) {
      // Melee attack with ranged weapon
      if (weapon.getEffectiveRange() > 1) {
        return actor.getCurrentStatBlock().getStrength() + 1;
      }

      // Melee attack with melee weapon
      return actor.getCurrentStatBlock().getStrength() + weapon.getEffectiveDamage();
    }

    // Ranged attack with ranged weapon
    return weapon.getEffectiveDamage();
  }

  private static Item getWeapon(final AbstractActor actor) {
    final Inventoriable weapon = actor.getEquippedItem(EquipmentSlot.WEAPON);
    if (weapon instanceof Item) {
      return (Item) weapon;
    }

    return null;
  }
}
