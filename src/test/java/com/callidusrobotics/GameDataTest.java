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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.Unicode;
import com.callidusrobotics.util.XmlMarshaller;

public class GameDataTest {
  final XmlMarshaller marshaller = new XmlMarshaller(GameData.class);
  GameData data;

  @Before
  public void before() throws Exception {
    data = new GameData();
  }

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final GameData data2 = (GameData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoGameData() throws Exception {
    data.loadingScreen = makeDemoLoadingScreen();
    data.splashScreen = makeDemoSplashScreen();
    data.introduction = makeDemoIntroduction();
    data.legalNotice = makeLegalNotice();
    data.instructions = makeInstructions();

    data.playerFile = "/data/demo/player.xml";
    data.startingAreaFile = "/data/demo/maps/demo1.xml";
    data.factionDataFile = "/data/demo/factionData.xml";
    data.itemListFiles = Arrays.asList("/data/demo/items/demo.xml");
    data.npcListFiles = Arrays.asList("/data/demo/npcs/demo.xml");

    data.splashForeground = TrueColor.LIME.toString();
    data.splashBackground = TrueColor.BLACK.toString();
    data.boxForeground = TrueColor.DIM_GRAY.toString();
    data.boxBackground = TrueColor.BLACK.toString();
    data.fontForeground = TrueColor.LAVENDER.toString();
    data.fontBackground = TrueColor.BLACK.toString();
    data.selectForeground = TrueColor.BLACK.toString();
    data.selectBackground = TrueColor.LAVENDER.toString();
  }

  private String makeDemoLoadingScreen() {
    return "\n Loading. Please wait...";
  }

  private String makeDemoSplashScreen() {
    return "\n                                   Game Title";
  }

  private String makeDemoIntroduction() {
    return "Story goes here";
  }

  private String makeLegalNotice() {
    final String legalNotice = "\nCopyright (C) 2013 Rusty Gerard\n\n" +
        "This program is free software: you can redistribute it and/or modify\n" +
        "it under the terms of the GNU General Public License as published by\n" +
        "the Free Software Foundation, either version 3 of the License, or\n" +
        "(at your option) any later version.\n\n" +
        "This program is distributed in the hope that it will be useful,\n" +
        "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
        "GNU General Public License for more details.\n\n" +
        "You should have received a copy of the GNU General Public License\n" +
        "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n";

    return legalNotice;
  }

  private String makeInstructions() {
    final String instructions = "\nCONTROLS\n" +
        "Arrow keys and numbers control movement. Press 5 to wait.\n\n" +

        "7 8 9     " + Unicode.ARROW_NORTHWEST + " " + Unicode.ARROW_NORTH + " " + Unicode.ARROW_NORTHEAST + "\n\n" +
        "4 5 6     " + Unicode.ARROW_WEST + "   " + Unicode.ARROW_EAST + "\n\n" +
        "1 2 3     " + Unicode.ARROW_SOUTHWEST + " " + Unicode.ARROW_SOUTH + " " + Unicode.ARROW_SOUTHEAST + "\n\n" +

        "COMMANDS\n" +
        //"< Ascend stairs\n" +
        //"> Descend stairs\n" +
        "g Grab an item\n" +
        "G Grab an item quickly\n" +
        "d Drop an item\n" +
        "D Drop an item quickly\n" +
        "i View inventory\n" +
        "u Use an item in your inventory\n" +
        "e Equip an item in your inventory\n" +
        "r Remove an equipped item and place it in your inventory\n" +
        "R Remove ammunition from an item in your inventory\n" +
        "l Load ammunition into a weapon\n" +
        "f Fire a projectile weapon\n" +
        "c Chat with an adjacent NPC\n" +
        "? Display this screen\n" +
        "Q Quit the game\n\n" +

        "SPACEBAR Toggles between display modes -or-\n" +
        "targets when aiming a projectile.\n\n" +

        "HOW TO KILL STUFF\n" +
        "Bump into a monster to attack it, or press (f)ire to aim a projectile\n" +
        "weapon at it.\n";

    return instructions;
  }
}
