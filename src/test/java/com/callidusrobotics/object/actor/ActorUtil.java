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

import com.callidusrobotics.object.ItemUtil;
import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.object.actor.NonPlayerCharacterData.GameDisplayInfo;
import com.callidusrobotics.util.TrueColor;

public class ActorUtil {
  // Every town should have its own faction
  static final String DEMO_TOWNSFOLK_FACTION = "townsfolk";
  static final String DEMO_MONSTERS_FACTION = "monsters";

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
}
