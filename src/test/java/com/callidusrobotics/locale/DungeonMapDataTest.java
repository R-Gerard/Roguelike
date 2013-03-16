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

package com.callidusrobotics.locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.callidusrobotics.locale.DungeonMapData.SpawnEntry;
import com.callidusrobotics.locale.DungeonMapData.SpawnList;
import com.callidusrobotics.object.actor.ActorUtil;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.XmlMarshaller;

public class DungeonMapDataTest {
  final XmlMarshaller marshaller = new XmlMarshaller(DungeonMapData.class);
  DungeonMapData data;

  @Before
  public void before() throws Exception {
    data = new DungeonMapData();
  }

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final DungeonMapData data2 = (DungeonMapData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoDungeon() throws Exception {
    final List<TileData> standardTiles = Arrays.asList(TileDataUtil.makeInvisibleBarrier('_'), TileDataUtil.makeFloor('.'), TileDataUtil.makeWall('#'));

    final List<SpawnList> npcs = Arrays.asList(
        new SpawnList(0, Arrays.asList(
            new SpawnEntry(100, ActorUtil.makeDemoMonster().getId()))));

    final NonEuclideanParameters neParams = new NonEuclideanParameters(10, 20, 300, 600, 100, false);
    final NpcPopulationParameters spawnParams = new NpcPopulationParameters(6, 2, 5, 0, 2);

    data = new DungeonMapData("Demo Dungeon", TrueColor.GRAY, DungeonType.NON_EUCLIDEAN, neParams, spawnParams, npcs, standardTiles, null);
  }

  @Test
  public void make7drlAnotherDimension() throws Exception {
    data.name = "Another Dimension";

    final List<TileData> standardTiles = Arrays.asList(TileDataUtil.makeInvisibleBarrier('_'), TileDataUtil.makeFloor('.'), TileDataUtil.makeWall('#'));

    // The first few levels are only easy monsters that drop items
    SpawnList level0 = new SpawnList(0, new ArrayList<SpawnEntry>());
    level0.npcs.addAll(Arrays.asList(
        new SpawnEntry(250, ActorUtil.make7drlRatThing().getId()),
        new SpawnEntry(25, ActorUtil.make7drlYithian().getId()),
        new SpawnEntry(50, ActorUtil.make7drlMiGo().getId()),
        new SpawnEntry(25, ActorUtil.make7drlElderThing().getId())));

    // Next, add tougher monsters that don't drop items
    SpawnList level5 = new SpawnList(5, new ArrayList<SpawnEntry>());
    level5.npcs.addAll(level0.npcs);
    level5.npcs.addAll(Arrays.asList(
        new SpawnEntry(25, ActorUtil.make7drlNightgaunt().getId()),
        new SpawnEntry(50, ActorUtil.make7drlLengSpider().getId())));

    // Add the rest of the NPCs
    SpawnList level10 = new SpawnList(10, new ArrayList<SpawnEntry>());
    level10.npcs.addAll(level5.npcs);
    level10.npcs.addAll(Arrays.asList(
        new SpawnEntry(10, ActorUtil.make7drlStarSpawn().getId()),
        new SpawnEntry(5, ActorUtil.make7drlDeputy().getId())));

    // Drop off the weakest monsters
    SpawnList level15 = new SpawnList(15, new ArrayList<SpawnEntry>());
    level15.npcs.addAll(level10.npcs);
    level15.npcs.remove(new SpawnEntry(1, ActorUtil.make7drlRatThing().getId()));
    level15.npcs.remove(new SpawnEntry(1, ActorUtil.make7drlYithian().getId()));
    level15.npcs.remove(new SpawnEntry(1, ActorUtil.make7drlMiGo().getId()));

    // Remove any allies in the deepest levels
    SpawnList level20 = new SpawnList(20, new ArrayList<SpawnEntry>());
    level20.npcs.addAll(level15.npcs);
    level20.npcs.remove(new SpawnEntry(1, ActorUtil.make7drlDeputy().getId()));

    final List<SpawnList> npcs = Arrays.asList(
        level0,
        level5,
        level10,
        level15,
        level20);

    final NonEuclideanParameters neParams = new NonEuclideanParameters(45, 55, 300, 600, 100, false);
    final NpcPopulationParameters spawnParams = new NpcPopulationParameters(6, 2, 5, 0, 2);

    final List<String> specialRoomFiles = Arrays.asList("/data/7DRL2013/maps/warlockLair.xml");

    data = new DungeonMapData("Another Dimension", TrueColor.GRAY, DungeonType.NON_EUCLIDEAN, neParams, spawnParams, npcs, standardTiles, specialRoomFiles);
  }
}
