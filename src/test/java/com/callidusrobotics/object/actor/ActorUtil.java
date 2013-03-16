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
import java.util.List;

import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.object.ItemUtil;
import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.object.actor.NonPlayerCharacterData.GameDisplayInfo;
import com.callidusrobotics.util.TrueColor;

public class ActorUtil {
  // Every town should have its own faction
  static final String DEMO_TOWNSFOLK_FACTION = "townsfolk";
  static final String DEMO_MONSTERS_FACTION = "monsters";

  static final String _7DRL_DEPUTIES_FACTION = "deputies";
  static final String _7DRL_TOWNSFOLK_FACTION = "townsfolk";
  static final String _7DRL_MONSTERS_FACTION = "monsters";

  private static AiStrategy makeAiStrategy(final Behavior behavior, final int min, final int max, final int base, final int multiplier) {
    return AiStrategyFactory.makeAiStrategy(behavior, null, min, max, base, multiplier);
  }

  public static NonPlayerCharacterData makeDemoTownsfolk() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "Hi",
        "Nice weather today, isn't it?",
        "Hello", "How are you?");

    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeMoney().getId(), 10, null, 90));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makePocketKnife().getId(), 1, 50, null));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.PINK, "Townsfolk", false, "A generic person.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "townsfolk", displayInfo, DEMO_TOWNSFOLK_FACTION,
        new StatBlock(10, 10, 1, 1, 1, 1, 50),
        null, null, aiStrategies);

    npcData.setMessages(messages);
    npcData.setFixedItems(fixedItems);
    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData makeDemoMonster() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final List<GameObjectItemData> randomItems = Arrays.asList(new GameObjectItemData(ItemUtil.makeHealingStone().getId(), 1, 10, null));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('M', TrueColor.GREEN_YELLOW, "Monster", false, "A generic monster.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "monster", displayInfo, DEMO_MONSTERS_FACTION,
        new StatBlock(8, 8, 3, 3, 3, 3, 75),
        null, null, aiStrategies);

    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlSheriff() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "Keep calm in front of the civilians.",
        "Stay on guard.",
        "Keep an eye out for more of those creatures.",
        "Did you remember to equip your sidearm?");

    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeMoney().getId(), 10, null, 10),
        new GameObjectItemData(ItemUtil.makeColtPeacemaker().getId(), 1, null, null),
        new GameObjectItemData(ItemUtil.make45ColtAmmo().getId(), 45, null, 10));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeCavalrySaber().getId(), 1, 50, null));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.DARK_BLUE, "Sheriff", false, "The local sheriff.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "sheriff", displayInfo, _7DRL_TOWNSFOLK_FACTION,
        new StatBlock(35, 35, 15, 15, 15, 10, 50),
        null, null, aiStrategies);

    npcData.setMessages(messages);
    npcData.setFixedItems(fixedItems);
    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlReporter() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "What a scoop! This is the story of the century.",
        "Can I interview you?",
        "Have you seen a creature?",
        "Care to make a statement?",
        "If I can get a photograph of one of them I'm sure to get a Pulitzer!");

    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeMoney().getId(), 1, null, 5));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.DARK_GOLDENROD, "Reporter", false, "A reporter for the Miskatonic Herald.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "reporter", displayInfo, _7DRL_TOWNSFOLK_FACTION,
        new StatBlock(20, 20, 10, 5, 5, 5, 50),
        null, null, aiStrategies);

    npcData.setMessages(messages);
    npcData.setFixedItems(fixedItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlStudent() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "Hi.",
        "Hello.",
        "I have an exam today. I hope my professor is okay.",
        "Maybe I should go back to the dorms.",
        "Are we safe here?",
        "I keep hearing strange noises coming from inside.");

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.PINK, "Student", false, "A student of Miskatonic University.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "student", displayInfo, _7DRL_TOWNSFOLK_FACTION,
        new StatBlock(10, 10, 1, 1, 1, 1, 50),
        null, null, aiStrategies);

    npcData.setMessages(messages);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlProfessorAtwood() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "I believe a portal to another world has opened here.",
        "The campus has become warped. It's like a maze in there.",
        "I wandered aimlessly until I found my way outside.");

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.GOLD, "Professor Atwood", true, "A professor of Miskatonic University.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "professor_atwood", displayInfo, _7DRL_TOWNSFOLK_FACTION,
        new StatBlock(10, 10, 1, 1, 1, 1, 50),
        new Coordinate(2, 23),
        null, aiStrategies);

    npcData.setMessages(messages);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlDeputy() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.WANDERING, 1, 1, 1, 0));

    final List<String> messages = Arrays.asList(
        "I'd be careful if I were you. There are monsters everywhere!",
        "Do you know the way out?",
        "I'm lost.",
        "Got any ammo?");

    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeMoney().getId(), 1, null, 5),
        new GameObjectItemData(ItemUtil.makeServiceRevolver().getId(), 1, null, null));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.make38Ammo().getId(), 10, 25, 10));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.STEEL_BLUE, "Deputy", false, "A deputy of Arkham.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "deputy", displayInfo, _7DRL_DEPUTIES_FACTION,
        new StatBlock(25, 25, 10, 10, 10, 10, 50),
        null, null, aiStrategies);

    npcData.setMessages(messages);
    npcData.setFixedItems(fixedItems);
    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlRatThing() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('r', TrueColor.SADDLE_BROWN, "Rat Thing", false, "A creature no bigger than a house cat with the face of a man and the body of a rat.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "rat_thing", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(5, 5, 2, 30, 2, 2, 90),
        null, null, aiStrategies);

    // Rat Things will hoard anything and everything they can find, especially if it's shiny!
    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makePallidMask().getId(), 1, 1, null),
        new GameObjectItemData(ItemUtil.makeElderSignAmulet().getId(), 1, 1, null),
        new GameObjectItemData(ItemUtil.makeHealingStone().getId(), 1, 5, null),
        new GameObjectItemData(ItemUtil.makePowderOfIbnGhazi().getId(), 1, 5, 4),
        new GameObjectItemData(ItemUtil.makeCavalrySaber().getId(), 1, 5, null),
        new GameObjectItemData(ItemUtil.makeColtPeacemaker().getId(), 1, 5, null),
        new GameObjectItemData(ItemUtil.makeServiceRevolver().getId(), 1, 5, null),
        new GameObjectItemData(ItemUtil.make45ColtAmmo().getId(), 10, 10, 10),
        new GameObjectItemData(ItemUtil.make38Ammo().getId(), 10, 20, 10),
        new GameObjectItemData(ItemUtil.makeMoney().getId(), 1, 20, 10));

    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlElderThing() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makePowderOfIbnGhazi().getId(), 5, 25, 10));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('e', TrueColor.CYAN, "Elder Thing", false, "A man-sized, winged creature with vegetable and animal characteristics.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "elder_thing", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(50, 50, 20, 10, 30, 0, 50),
        null, null, aiStrategies);

    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlYithian() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 90, 0, 1));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeHealingStone().getId(), 2, 25, 3));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('Y', TrueColor.YELLOW, "Yithian", false, "A 20-foot tall rugose cone topped by a pair of clawed appendages, a long trumpet, and a yellow orb on a stalk.", Size.GIGANTIC, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "yithian",
        displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(50, 50, 20, 10, 30, 5, 50),
        null, null, aiStrategies);

    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlLengSpider() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('S', TrueColor.PURPLE, "Leng Spider", false, "A bloated, 6000-pound man-eating spider.", Size.LARGE, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "leng_spider", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(50, 50, 20, 10, 5, 5, 75),
        null, null, aiStrategies);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlNightgaunt() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('n', TrueColor.BLUE, "Nightgaunt", false, "A thin, black, faceless, winged creature with a vaguely human-shaped body and barbed tail.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "nightgaunt", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(25, 25, 20, 20, 10, 5, 75),
        null, null, aiStrategies);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlMiGo() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final List<GameObjectItemData> randomItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makePowderOfIbnGhazi().getId(), 2, 10, 3));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('m', TrueColor.LIGHT_SALMON, "Mi-go", false, "A pinkish, fungoid, crustacean-like entity the size of a man, with a pyramid of fleshy rings and antennae for a head.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "mi-go", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(25, 25, 20, 10, 30, 5, 50),
        null, null, aiStrategies);

    npcData.setRandomItems(randomItems);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlStarSpawn() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 100, 0, 1));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('C', TrueColor.GREEN, "Star Spawn", false, "A 30-foot tall green, scaly monster of vaguely anthropoid outline, with an octopus-like head, prodigious claws, and vestigal wings.", Size.GIGANTIC, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "star_spawn", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(250, 250, 20, 10, 10, 15, 75),
        null, null, aiStrategies);

    return npcData;
  }

  public static NonPlayerCharacterData make7drlWarlock() {
    final List<AiStrategy> aiStrategies = Arrays.asList(
        makeAiStrategy(Behavior.MELEE, 0, 90, 0, 1));

    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeNecronomicon().getId(), 1, null, null),
        new GameObjectItemData(ItemUtil.makeHealingStone().getId(), 5, null, null));

    final GameDisplayInfo displayInfo = new GameDisplayInfo('t', TrueColor.CRIMSON, "Warlock", false, "A powerful cultist.", Size.MEDIUM, null);

    final NonPlayerCharacterData npcData = new NonPlayerCharacterData(
        "warlock", displayInfo, _7DRL_MONSTERS_FACTION,
        new StatBlock(2500, 2500, 20, 20, 20, 50, 100),
        null, null, aiStrategies);

    npcData.setFixedItems(fixedItems);

    return npcData;
  }
}
