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

  @Test
  public void make7drlMiskatonicUniversity() throws Exception {
    data.name = "Miskatonic University";
    data.isDangerous = false;

    data.exits = Arrays.asList(new ExitData(Direction.NORTH, 'N', null, "/data/7DRL2013/dungeons/anotherDimension.xml"));

    data.tiles = new LinkedList<TileData>();
    data.tiles.add(TileDataUtil.makeExit('N', Direction.NORTH));
    data.tiles.add(TileDataUtil.makeWall('#'));
    data.tiles.add(TileDataUtil.makeInvisibleBarrier('_'));
    data.tiles.add(TileDataUtil.makeSteps('^'));
    data.tiles.add(TileDataUtil.makeFountain('F'));
    data.tiles.add(TileDataUtil.makeGrass('.'));
    data.tiles.add(TileDataUtil.makeTree('T'));
    data.tiles.add(TileDataUtil.makePath('='));

    data.npcs = new LinkedList<String>();
    data.npcs.add(ActorUtil.make7drlProfessorAtwood().getId());
    data.npcs.add(ActorUtil.make7drlReporter().getId());
    data.npcs.add(ActorUtil.make7drlSheriff().getId());
    data.npcs.add(ActorUtil.make7drlStudent().getId());
    data.npcs.add(ActorUtil.make7drlStudent().getId());

    data.map = "\n" +
        "########################N########################\n" +
        "T....................^##^##^....................T\n" +
        ".....................^^^^^^^.....................\n" +
        "T.....................^^^^^.....................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T..........^^^.........===.........^^^..........T\n" +
        "...........^F^=====================^F^...........\n" +
        "T..........^^^.........===.........^^^..........T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        "........................=........................\n" +
        "T.......................=.......................T\n" +
        ".T.T.T.T.T.T.T.T.T.T.T..=..T.T.T.T.T.T.T.T.T.T.T.\n";

    data.width = data.map.split("\\n")[1].length();
  }

  @Test
  public void make7drlWarlockLair() throws Exception {
    data.name = "Lair of the Warlock";
    data.isDangerous = true;

    data.tiles = new LinkedList<TileData>();
    data.tiles.add(TileDataUtil.makeExit('N', Direction.NORTH));
    data.tiles.add(TileDataUtil.makeInvisibleBarrier('_'));
    data.tiles.add(TileDataUtil.makeLavaRock('.'));
    data.tiles.add(TileDataUtil.makeLava('~'));
    data.tiles.add(TileDataUtil.makeStalagmite('^'));

    data.exits = Arrays.asList(new ExitData(Direction.NORTH, 'N', null, null));
    data.npcs = Arrays.asList(ActorUtil.make7drlWarlock().getId());

    data.map = "\n" +
        "_____________~~~~^^^^N^^~~~~~______________\n" +
        "____________~~~~^^^....^^^^~~~~____________\n" +
        "____________~~~~^^^^.....^^^~~~~___________\n" +
        "___________~~~~~^^^.......^^^~~~~__________\n" +
        "___________~~~~~^^^.......^^^^~~~__________\n" +
        "____________~~~~^^^.......^^^^~~~__________\n" +
        "____________~~~~^^^.......^^^~~~___________\n" +
        "_____________~~~~^^^...^..^^~~~____________\n" +
        "_____________~~~~~^^^^^^^.^~~~_____________\n" +
        "______________~~~~~~^^^..^~~~______________\n" +
        "______________~~~~~~^^..^~~~_______________\n" +
        "____________~~~~~~^^^..~~~~________________\n" +
        "__________~~~~~~~~~~.^.^~~~________________\n" +
        "________~~~~~~^^^^^.^^.^~~~~_______________\n" +
        "______~~~~~^^^^^^^^^..^^~~~~~______________\n" +
        "_____~~~~~^^........^..^^^^~~~_____________\n" +
        "____~~~~~~^............^^^^^~~~____________\n" +
        "____~~~~~^^............^^^^^^~~~~__________\n" +
        "____~~~~~^................^^^^~~~~_________\n" +
        "____~~~~~^......^.........^^^^~~~~_________\n" +
        "____~~~~~^^.....^.........^.^^^~~~~________\n" +
        "____~~~~~^^^^....^..........^^^~~~~________\n" +
        "_____~~~~~^^.....^...........^^~~~~________\n" +
        "______~~~~~^^....^...........^~~~~~________\n" +
        "_______~~~~~^^^^~~~~~~~~~~~~~^~~~~_________\n" +
        "________~~~~~~~~~~~~~~~~~~~~^^~~~__________\n" +
        "__________~~~~~~~~~~~~~~~~~~^^~~___________\n" +
        "____________~~~~~~~~~~~~~~~~~~~____________\n" +
        "___________________________________________\n";

    data.width = data.map.split("\\n")[1].length();
  }
}
