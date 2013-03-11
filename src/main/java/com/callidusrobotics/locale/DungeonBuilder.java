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

import java.io.IOException;
import java.util.List;

/**
 * Interface for ADTs that construct dungeons.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonMapData
 */
public interface DungeonBuilder {
  /**
   * De-serializes a dungeon.
   *
   * @param serializedDungeon
   *          The serialized dungeon String, not null
   * @return The entrance of the dungeon, never null
   */
  DungeonLevel fromString(final String serializedDungeon);

  /**
   * Generates a random serialized dungeon.
   *
   * @return The serialized dungeon String, never null
   */
  String buildRandomDungeonString();

  /**
   * DungeonMapData mutator.
   *
   * @param dungeonMapData
   *          The parameters used to construct a random dungeon.
   * @throws IOException
   *           If the DungeonMapData references a resource that can not be
   *           marshalled
   */
  void setDungeonMapData(DungeonMapData dungeonMapData) throws IOException;

  /**
   * DungeonMapData accessor.
   *
   * @return The DungeonMapData, never null
   */
  DungeonMapData getDungeonMapData();

  /**
   * DungeonLevel accessor.
   *
   * @return The list of DungeonLevel objects in the dungeon, never null. The
   *         first DungeonLevel in the list is the entrance.
   */
  List<DungeonLevel> getLevels();
}
