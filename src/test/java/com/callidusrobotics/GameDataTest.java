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

  @Test
  public void make7drlGameData() throws Exception {
    data.loadingScreen = make7drlLoadingScreen();
    data.splashScreen = make7drlSplashScreen();
    data.introduction = make7drlIntroduction();
    data.legalNotice = makeLegalNotice();
    data.instructions = makeInstructions();

    data.playerFile = "/data/7DRL2013/player.xml";
    data.startingAreaFile = "/data/7DRL2013/maps/miskatonicUniversity.xml";
    data.factionDataFile = "/data/7DRL2013/factionData.xml";

    data.itemListFiles = Arrays.asList(
        "/data/7DRL2013/items/armor.xml",
        "/data/7DRL2013/items/books.xml",
        "/data/7DRL2013/items/miscellany.xml",
        "/data/7DRL2013/items/weapons.xml");

    data.npcListFiles = Arrays.asList(
        "/data/7DRL2013/npcs/townsfolk.xml",
        "/data/7DRL2013/npcs/monsters.xml");

    data.splashForeground = TrueColor.BLUE.toString();
    data.splashBackground = TrueColor.BLACK.toString();
    data.boxForeground = TrueColor.DIM_GRAY.toString();
    data.boxBackground = TrueColor.BLACK.toString();
    data.fontForeground = TrueColor.LAVENDER.toString();
    data.fontBackground = TrueColor.BLACK.toString();
    data.selectForeground = TrueColor.BLACK.toString();
    data.selectBackground = TrueColor.LAVENDER.toString();
  }

  private String make7drlLoadingScreen() {
    return "\n Loading. Please wait...";
  }

  private String make7drlSplashScreen() {
    final String splashScreen = "\n" +
        "                                                                               \n" +
        "  *                             #     #                            ####  ##    \n" +
        "   *                 ***        ##   ##           ##               ##  # ##    \n" +
        "   **               *****       ### ###       ##  ##               ##  # ##    \n" +
        "    ***             ******      ####### ## # #### #####  ###   ### ####  ##    \n" +
        "    *****          *** ****     ## # ##  # #  ##  ##  # ## ## ##   ## #  ##    \n" +
        "     ******       ****   ***    ##   ##  # #  ##  ##  # ## ##   ## ##  # ##    \n" +
        "     *** ****     **      ***   ##   ##  ###   ## ##  #  ###  ###  ##  # ##### \n" +
        "      **   ***** ***        **            #                                    \n" +
        "       **     *****          **          #                                     \n" +
        "       ***        *****       **                  **                           \n" +
        "        ** *   **      *********        **********                             \n" +
        "         ** *  *       *       **  ************                                \n" +
        "          *   **        *        *       ****                                  \n" +
        "          **  *       * *   **** **    ****                                    \n" +
        "           *  *    **   **     *  *   ***                                      \n" +
        "             *   **    ****   *     ***                                        \n" +
        "             *  ***    ***  **    ***                                          \n" +
        "             * *  ***** ***      **                                            \n" +
        "            **  *              **   ***                                        \n" +
        "            **   **          ***     **                                        \n" +
        "            **    **        **       ***                                       \n" +
        "            **     **     ***         **                                       \n" +
        "           ***      ***  **           ***                                      \n" +
        "           **        *****             **                                      \n" +
        "           **         ****       *   * **                                      \n" +
        "           *            ***  *  ***   * **                                     \n" +
        "           *        **   ******    *    **                                     \n" +
        "           *       ***     ******    * ****                                    \n" +
        "           *      ***       ***** *      **                                    \n" +
        "           **    ***          *****      **                                    \n" +
        "           **   ***             ***** ******                                   \n" +
        "          ***  ***                **********                                   \n" +
        "          *** ***                    *******                                   \n" +
        "          *******                       *****                                  \n" +
        "           *****                                                               \n" +
        "           ****                                                                \n" +
        "           ***                                                                 \n" +
        "           **                                                                  \n" +
        "           **                                                                  \n" +
        "                                                                               \n" +
        "                                                                               \n" +
        "                                                                               \n" +
        "                                                                               \n" +
        "                                                                               \n" +
        "                                                                               \n" +
        "                                                Copyright (c) 2013 Rusty Gerard\n";

    return splashScreen;
  }


  private String make7drlIntroduction() {
    final String introduction = "\n" +
        "It all started in the autumn of 1925 in the sleepy city of Arkham,\n" +
        "Massachusetts. The men had set off to work, students were headed to class\n" +
        "at Miskatonic University, and you thought it was going to be another\n" +
        "routine patrol. You even brought bread crumbs to feed the birds. Of\n" +
        "course, that was until strange happenings started cropping up all over.\n\n\n" +

        "Farmer Akeley called the station to report that he had seen some sort of\n" +
        "\"Pinkish things that were about five feet long, with crustacous bodies\"\n" +
        "out on his property. It was dismissed out of hand until more reports came\n" +
        "from all over the city, even the Mayor. That was when the the Sheriff\n" +
        "rounded up all able-bodied men and women to investigate the situation.\n\n\n" +

        "At first no one knew from where these mysterious arcane powers were\n" +
        "coming from but all of the signs lead you to the University District.\n" +
        "It was then that you realized the powers were emanating from the very\n" +
        "campus itself.\n\n\n" +

        "Upon arriving onto the scene you notice a man running in a blind panic\n" +
        "from the main building. Selecting you out of the crowd at random, he\n" +
        "grabs you by the arm and frantically spouts nonsense about some book\n" +
        "called the \"Necronomicon:\"\n\n\n" +

        "\"It was stolen from the archives last night! It must be recovered! Only\n" +
        "then will the chaos stop and the University be set back to its original\n" +
        "state!\" Explaining that he is the University Library's archivist, the\n" +
        "old man pushes a mysterious object into your hands and implores you to\n" +
        "enter the main building at once.\n\n\n" +

        "Armed only with your service revolver and a hand-written journal filled\n" +
        "with arcane lore that you do not comprehend, are you brave enough to do\n" +
        "battle with the horrors that lurk within the non-Euclidean labyrinth to\n" +
        "recover the Necronomicon and bring the chaos to an end in Arkham?\n";

    return introduction;
  }
}
