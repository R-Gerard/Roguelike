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
import java.util.LinkedList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.ItemUtil;
import com.callidusrobotics.object.actor.ActorUtil;
import com.callidusrobotics.util.XmlMarshaller;

public class AreaMapDataTest {
  final XmlMarshaller marshaller = new XmlMarshaller(AreaMapData.class);
  AreaMapData data;

  @Before
  public void before() throws Exception {
    data = new AreaMapData();
  }

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final AreaMapData data2 = (AreaMapData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoArea1() throws Exception {
    data.name = "Demo Area 1";
    data.isDangerous = false;

    data.exits = Arrays.asList(
        new ExitData(Direction.NORTH, 'N', null, "/data/demo/dungeons/demo.xml"),
        new ExitData(Direction.SOUTH, 'S', "/data/demo/maps/demo2.xml", null));

    data.tiles = new LinkedList<TileData>();
    data.tiles.add(TileDataUtil.makeExit('N', Direction.NORTH));
    data.tiles.add(TileDataUtil.makeExit('S', Direction.SOUTH));
    data.tiles.add(TileDataUtil.makeWall('#'));
    data.tiles.add(TileDataUtil.makeInvisibleBarrier('_'));
    data.tiles.add(TileDataUtil.makeGrass('.'));
    data.tiles.add(TileDataUtil.makeTree('T'));
    data.tiles.add(TileDataUtil.makePath('='));

    data.npcs = Arrays.asList(ActorUtil.makeDemoTownsfolk().getId());

    data.map = "\n" +
        "#####N#####\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "T....=....T\n" +
        "TTTTTSTTTTT\n";

    data.width = data.map.split("\\n")[1].length();
  }

  @Test
  public void makeDemoArea2() throws Exception {
    data.name = "Demo Area 2";
    data.isDangerous = false;

    data.exits = Arrays.asList(new ExitData(Direction.NORTH, 'N', "/data/demo/maps/demo1.xml", null));

    data.tiles = new LinkedList<TileData>();
    data.tiles.add(TileDataUtil.makeExit('N', Direction.NORTH));
    data.tiles.add(TileDataUtil.makeWall('#'));
    data.tiles.add(TileDataUtil.makeInvisibleBarrier('_'));
    data.tiles.add(TileDataUtil.makeGrass('.'));
    data.tiles.add(TileDataUtil.makeTree('T'));
    data.tiles.add(TileDataUtil.makeChest('x',
        Arrays.asList((new GameObjectItemData(ItemUtil.makeHealingStone().getId(), 1, null, null))),
        null));

    data.map = "\n" +
        "TTTTTTTTTNTTTTTTTTT\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T.................T\n" +
        "T................xT\n" +
        "TTTTTTTTTTTTTTTTTTT\n";

    data.width = data.map.split("\\n")[1].length();
  }
}
