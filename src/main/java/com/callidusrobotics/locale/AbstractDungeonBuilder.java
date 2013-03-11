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
import java.util.Collections;
import java.util.List;

abstract class AbstractDungeonBuilder implements DungeonBuilder {
  protected DungeonMapData dungeonMapData = null;
  protected List<DungeonLevel> levels = Collections.emptyList();

  @Override
  public DungeonMapData getDungeonMapData() {
    return dungeonMapData;
  }

  @Override
  public void setDungeonMapData(final DungeonMapData dungeonMapData) throws IOException {
    this.dungeonMapData = dungeonMapData;
  }

  @Override
  public List<DungeonLevel> getLevels() {
    return Collections.unmodifiableList(levels);
  }

  /**
   * Computes the absolute Coordinate of an exit tile inside of a DungeonLevel.
   *
   * @param dungeonLevel
   *          The dungeonLevel to place the exit Tile in, not null
   * @param direction
   *          The direction of the exit, not null
   * @see DungeonLevel#setTileAbsolute(Coordinate, Tile)
   */
  protected abstract void createExit(final DungeonLevel dungeonLevel, final Direction direction);
}
