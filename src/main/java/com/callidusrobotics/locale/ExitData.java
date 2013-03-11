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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * POJO for marshalling and unmarshalling DungeonLevel neighbors.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonLevel#getNeighbor(Direction)
 */
@XmlRootElement
final class ExitData {
  @XmlAttribute(required = true) String direction;
  @XmlAttribute(required = true) String tileId;
  @XmlAttribute(required = false) String areaMapFile;
  @XmlAttribute(required = false) String dungeonMapFile;

  private transient boolean loaded = false;
  private transient DungeonLevel dungeonLevel = null;

  ExitData() { /* Required by JAXB */ }

  ExitData(final Direction direction, final char tileId, final String areaMapFile, final String dungeonMapFile) {
    // It's OK for both the areaMapFile and the dungeonMapFile to be null (this means the level will be an orphan until it's claimed by a sibling)
    Validate.isTrue(!(areaMapFile != null && dungeonMapFile != null), "Can't include both areaMapFile AND dungeonMapFile");

    this.direction = direction.toString();
    this.tileId = Character.toString(tileId);
    this.areaMapFile = areaMapFile;
    this.dungeonMapFile = dungeonMapFile;
  }

  ExitData(final Direction direction, final DungeonLevel dungeonLevel) {
    this.direction = direction.toString();
    this.tileId = " ";
    this.dungeonLevel = dungeonLevel;
    this.loaded = true;
  }

  boolean isLoaded() {
    return loaded;
  }

  String getAreaMapFile() {
    return areaMapFile;
  }

  String getDungeonMapFile() {
    return dungeonMapFile;
  }

  Direction getDirection() {
    return Direction.valueOf(direction);
  }

  char getTileId() {
    return tileId.charAt(0);
  }

  DungeonLevel load(final int depth) {
    if (loaded) {
      return dungeonLevel;
    }

    loaded = true;

    if (!StringUtils.isBlank(areaMapFile)) {
      try {
        final AreaMapData areaMapData = AreaMapData.fromFile(areaMapFile);

        dungeonLevel = areaMapData.buildLevel(depth);
      } catch (final IOException e) {
        return null;
      }

      return dungeonLevel;
    }

    if (!StringUtils.isBlank(dungeonMapFile)) {
      try {
        dungeonLevel = DungeonFactory.buildDungeon(DungeonMapData.fromFile(dungeonMapFile));
      } catch (final IOException | ReflectiveOperationException e) {
        return null;
      }

      return dungeonLevel;
    }

    return null;
  }
}
