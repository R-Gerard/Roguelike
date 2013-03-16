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

  public static ItemData makeJournal() {
    final String text = "\n" +
        "She is the Lord of the Wood, even to the end of time and the gifts of the\n" +
        "men of Leng. So from the wells of night to the gulfs of space, and from\n" +
        "the gulfs of space to the wells of night, ever the praises of Great\n" +
        "Cthulhu, of Tsathoggua, and of Him Who is not to be Named. Ever Their\n" +
        "praises, and abundance to the Black Goat of the Woods. Ia! Shub-Niggurath!\n" +
        "The Goat with a Thousand Young! Ia! Shub-Niggurath! The Black Goat of the\n" +
        "Woods with a Thousand Young!\n\n\n" +

        "And it has come to pass that the Lord of the Woods, being accompanied by\n" +
        "seven and nine, down the onyx steps bringing tributes to Him in the Gulf,\n" +
        "Azathoth, He of Whom Thou has taught us marvels innumerable. On the wings\n" +
        "of night out beyond space, out beyond the Abyss, to That whereof Yuggoth\n" +
        "is the youngest child, rolling alone in black aether at the rim.\n\n\n" +

        "Go out among men and find the ways thereof, that He in the Gulf may know.\n" +
        "To Nyarlathotep, Mighty Messenger, must all things be told. And He shall\n" +
        "put on the semblance of men, the waxen mask and the robe that hides, and\n" +
        "come down from the world of Seven Suns to mock us.\n\n\n" +

        "Nyarlathotep, Great Messenger, bringer of strange joy to Yuggoth through\n" +
        "the void, Father of the Million Favoured Ones, Stalker among the night.";

    return new ItemData("old_journal",
        '"', TrueColor.SADDLE_BROWN,
        new InventoriableData("Old Journal", "An old, leatherbound journal.", 1, 0),
        new UseableData(false, false, -1, null, text, null, null),
        null);
  }

  public static ItemData makeNecronomicon() {
    return new ItemData("necronomicon",
        '"', TrueColor.BLACK,
        new InventoriableData("The Necronomicon", "A massive tome bound in leather with two iron clasps.", 1, 0),
        new UseableData(false, false, -1, null, "To read this tome of forbidden lore would surely lead to madness.", null, null),
        null);
  }

  public static ItemData makeCavalrySaber() {
    final int baseDamage = 10;
    return new ItemData("cavalry_saber",
        '(', TrueColor.SILVER,
        new InventoriableData("Cavalry Saber", "Model 1860 Light Cavalry Saber.", 1, 1000),
        new UseableData(false, false, -1, null, "You rattle the saber in a flagrant display of power.", null, null),
        new EquipableData(0, baseDamage, null, Arrays.asList(EquipmentSlot.WEAPON)));
  }

  public static ItemData makeColtPeacemaker() {
    final int range = 16;
    final int baseDamage = 70;
    return new ItemData("colt_peacemaker",
        '}', TrueColor.SILVER,
        new InventoriableData("Colt Peacemaker", "Colt model 1873 Single Action Army revolver.\n\nCaliber: .45 Colt", 1, 2500),
        new UseableData(false, true, 6, 1, "KABOOM!", "45_colt_ammo", null),
        new EquipableData(range, baseDamage, null, Arrays.asList(EquipmentSlot.WEAPON)));
  }

  public static ItemData make45ColtAmmo() {
    return new ItemData("45_colt_ammo",
        '/', TrueColor.GOLD,
        new InventoriableData(".45 Colt Cartridge", "A low velocity, heavy caliber bullet.", -1, 25),
        new UseableData(false, false, -1, null, "Try loading this in a firearm.", null, null),
        null);
  }

  public static ItemData makePowderOfIbnGhazi() {
    return new ItemData("powder_of_ibn_ghazi",
        '!', TrueColor.PURPLE,
        new InventoriableData("Powder of Ibn-Ghazi", "A magical powder of great potency.", -1, 1000),
        new UseableData(true, false, -1, null, "Suddenly you realize all is not as it seems.", null, new StatBlock(0, 0, 0, 0, 2, 0, 0)),
        new EquipableData(2, 1000, null, Arrays.asList(EquipmentSlot.WEAPON)));
  }

  public static ItemData makePallidMask() {
    return new ItemData("pallid_mask",
        '[', TrueColor.ANTIQUE_WHITE,
        new InventoriableData("Pallid Mask", "A smooth and featureless mask with faint, vermiculated markings.", 1, 10000),
        new UseableData(false, false, -1, null, "Studying the intricate patterns you become hypnotized momentarily.", null, null),
        new EquipableData(0, 0, new StatBlock(5, 0, 0, 5, 0, 5, 0), Arrays.asList(EquipmentSlot.HEAD)));
  }

  public static ItemData makeElderSignAmulet() {
    return new ItemData("elder_sign_amulet",
        '[', TrueColor.BLUE,
        new InventoriableData("Elder Sign Amulet", "An amulet in the shape of a warped, five-pointed star with a flaming eye in the center.", 1, 5000),
        new UseableData(false, false, -1, null, "The eye gazes back at you.", null, null),
        new EquipableData(0, 15, new StatBlock(5, 0, 5, 0, 0, 5, 0), Arrays.asList(EquipmentSlot.NECK, EquipmentSlot.WEAPON)));
  }
}
