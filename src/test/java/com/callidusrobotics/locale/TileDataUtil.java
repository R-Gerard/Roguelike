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

import java.util.List;

import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.Unicode;

public class TileDataUtil {

  public static TileData makeExit(final char identifier, final Direction direction) {
    final DungeonMapData dungeonMapData = new DungeonMapData();

    return new TileData(identifier, dungeonMapData.makeExitTile(direction));
  }

  public static TileData makeWall(final char identifier) {
    return new TileData(identifier, Tile.makeDefaultWall());
  }

  public static TileData makeInvisibleBarrier(final char identifier) {
    return new TileData(identifier, Tile.makeDefaultInvisibleBarrier());
  }

  public static TileData makeFloor(final char identifier) {
    return new TileData(identifier, Tile.makeDefaultFloor());
  }

  public static TileData makeGrass(final char identifier) {
    return new TileData(identifier, '.', TrueColor.FOREST_GREEN, null, false, null, "Grass", "Patch of green grass.");
  }

  public static TileData makeTree(final char identifier) {
    return new TileData(identifier, 'T', TrueColor.FOREST_GREEN, null, true, null, "Tree", "A tree.");
  }

  public static TileData makePath(final char identifier) {
    return new TileData(identifier, '.', TrueColor.SADDLE_BROWN, null, false, null, "Path", "Gravel path.");
  }

  public static TileData makeSteps(final char identifier) {
    return new TileData(identifier, '.', TrueColor.GRAY, null, false, null, "Steps", "Stone steps.");
  }

  public static TileData makeChest(final char identifier, final List<GameObjectItemData> fixedItems, final List<GameObjectItemData> randomItems) {
    final TileData tileData = new TileData(identifier, Unicode.SYMBOL_MISC_BALLOT_BOX_WITH_X, TrueColor.SADDLE_BROWN, null, false, true, "Chest", "A large wooden box.");
    tileData.fixedItems = fixedItems;
    tileData.randomItems = randomItems;

    return tileData;
  }

  public static TileData makeFountain(final char identifier) {
    return new TileData(identifier, '&', TrueColor.BLUE, null, true, null, "Fountain", "A fountain.");
  }

  public static TileData makeStalagmite(final char identifier) {
    return new TileData(identifier, '^', TrueColor.GRAY, null, true, null, "Stalagmite", "A stalagmite.");
  }

  public static TileData makeLavaRock(final char identifier) {
    return new TileData(identifier, '.', TrueColor.GRAY, null, false, null, "Floor", "Barren volcanic rock.");
  }

  public static TileData makeLava(final char identifier) {
    return new TileData(identifier, '~', TrueColor.RED, null, true, null, "Lava", "Bubbling lava.");
  }
}
