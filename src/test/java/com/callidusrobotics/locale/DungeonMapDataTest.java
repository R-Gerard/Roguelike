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
}
