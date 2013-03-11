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

import java.util.Arrays;

import com.callidusrobotics.object.actor.StatBlock;
import com.callidusrobotics.util.TrueColor;

public class ItemUtil {
  public static ItemData makeBreadcrumb() {
    return new ItemData("breadcrumb",
        '*', TrueColor.BLANCHED_ALMOND,
        new InventoriableData("Breadcrumb", "A crumb of bread for marking your way.", -1, 0),
        new UseableData(false, false, -1, null, "If only there were some birds to feed...", null, null),
        null);
  }

  public static ItemData makeMoney() {
    // If we value a money object ('$') at 100, then everything is measured in pennies and small items like bullets can have a nonzero value
    return new ItemData("money",
        '$', TrueColor.GREEN,
        new InventoriableData("Money", "Fat stacks of cash.", -1, 100),
        new UseableData(false, false, -1, null, "Maybe you can buy something with this.", null, null),
        null);
  }

  public static ItemData makePocketKnife() {
    final int baseDamage = 2;
    return new ItemData("pocket_knife",
        '(', TrueColor.SILVER,
        new InventoriableData("Pocket Knife", "A small knife.", 1, 200),
        new UseableData(false, false, -1, null, "Ouch! The blade is sharp.", null, null),
        new EquipableData(0, baseDamage, null, Arrays.asList(EquipmentSlot.WEAPON)));
  }

  public static ItemData makeServiceRevolver() {
    final int range = 8;
    final int baseDamage = 20;
    return new ItemData("service_revolver",
        '}', TrueColor.SILVER,
        new InventoriableData("Service Revolver", "Smith & Wesson model 1899 Military & Police revolver.\n\nCaliber: .38 Special", 1, 1000),
        new UseableData(false, true, 6, 6, "BANG!", "38_special_ammo", null),
        new EquipableData(range, baseDamage, null, Arrays.asList(EquipmentSlot.WEAPON)));
  }

  public static ItemData make38Ammo() {
    return new ItemData("38_special_ammo",
        '/', TrueColor.GOLD,
        new InventoriableData(".38 Special Cartridge", "A low velocity, medium caliber bullet.", -1, 5),
        new UseableData(false, false, -1, null, "Try loading this in a firearm.", null, null),
        null);
  }

  public static ItemData makeHealingStone() {
    return new ItemData("healing_stone",
        '*', TrueColor.RED,
        new InventoriableData("Healing Stone", "A magical gem that regenerates flesh.", -1, 1000),
        new UseableData(true, false, -1, null, "The stone glows for a moment before disappearing.", null, new StatBlock(0, 10, 0, 0, 0, 0, 0)),
        null);
  }
}
