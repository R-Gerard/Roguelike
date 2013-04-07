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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import com.callidusrobotics.ConsoleColleague;
import com.callidusrobotics.locale.DungeonLevel.Room;
import com.callidusrobotics.object.actor.NonPlayerCharacterFactory;
import com.callidusrobotics.util.XmlMarshaller;

/**
 * POJO for XML data describing pre-defined maps such as towns and special
 * levels inside of dungeons.
 *
 * @author Rusty
 * @since 0.0.1
 */
@XmlRootElement
public final class AreaMapData {
  @XmlAttribute(required = true) String name;
  @XmlElement(required = true) boolean isDangerous = false;
  @XmlElement(required = false) String fovAlgorithm;
  @XmlElementWrapper(required = true) @XmlElement(name = "tile") List<TileData> tiles;
  @XmlElement(required = true) int width;
  @XmlElement(required = true) String map;
  @XmlElementWrapper(required = false) @XmlElement(name = "exit") List<ExitData> exits;
  @XmlElementWrapper(required = false) @XmlElement(name = "npc") List<String> npcs;

  AreaMapData() { /* Required by JAXB */ }

  public static AreaMapData fromFile(final String xmlFile) throws IOException {
    // TODO: Keep a map of loaded files and return entries that have already been loaded
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(AreaMapData.class);

    return (AreaMapData) xmlMarshaller.unmarshalSystemResource(xmlFile);
  }

  /**
   * Builds a dungeonLevel object from the XML data.
   *
   * @param depth
   *          The depth of level
   * @return The dungeonLevel constructed from the XML
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public DungeonLevel buildLevel(final int depth) {
    map = map.replaceAll("\\s", "");

    final DungeonLevel dungeonLevel = new DungeonLevel(map.length() / width, width, name, depth, Tile.makeDefaultInvisibleBarrier());
    dungeonLevel.getRooms().add(new Room(RoomType.RECTANGLE, dungeonLevel.getPosition(), dungeonLevel.getHeight(), dungeonLevel.getWidth()));
    dungeonLevel.setIsDangerous(isDangerous);
    dungeonLevel.setDungeonMapData(getDungeonMapData());
    final Map<Character, TileData> tileMap = new HashMap<Character, TileData>();

    for (final TileData tile : tiles) {
      tileMap.put(tile.getId(), tile);
    }

    for (int r = 0; r < dungeonLevel.getHeight(); r++) {
      for (int c = 0; c < dungeonLevel.getWidth(); c++) {
        final char key = map.charAt(r * dungeonLevel.getWidth() + c);
        dungeonLevel.setTileAbsolute(r, c, tileMap.get(key).makeTile());
      }
    }

    for (final String npcId : getNpcs()) {
      Coordinate position = NonPlayerCharacterFactory.getNonPlayerCharacterData(npcId).getStartingPosition();
      if (position == null || !dungeonLevel.isVacantRelative(position)) {
        position = dungeonLevel.getRandomCoordinate();
      }

      if (position != null) {
        dungeonLevel.getNonPlayerCharacters().add(NonPlayerCharacterFactory.makeNpc(npcId, position));
      }
    }

    // Lazy-load the adjacent areas
    for (final ExitData exit : getExits()) {
      Validate.isTrue(map.contains(exit.tileId), "AreaMapData '" + name + "' does not contain tile ID '" + exit.getTileId() + "'.");

      final int index = map.indexOf(exit.getTileId());
      final int row = index / width;
      final int col = index % width;
      final MutableCoordinate coordinate = new MutableCoordinate(row, col);

      // Populate the exit data for lazy-loading (i.e. don't actually create the neighbor and connect it yet)
      dungeonLevel.neighbors[exit.getDirection().ordinal()] = exit;

      // Generally, a tile's relative and absolute positions are not the same, except we have not called dungeonLevel.setPosition yet
      // so it is still safe to call getTileRelative and setTileAbsolute
      final Tile exitTile = dungeonLevel.getTileRelative(coordinate);
      exitTile.exit = exit.getDirection();
      exitTile.barrier = false;
      dungeonLevel.setExitAbsolute(coordinate, exit.getDirection(), exitTile);
    }

    // Now that the dungeonLevel is fully initialized we can compute its position on the screen
    dungeonLevel.setPosition(ConsoleColleague.getPosition(dungeonLevel));

    return dungeonLevel;
  }

  /**
   * Generates a dummy DungeonMapData wrapper around the fovAlgorithm.
   *
   * @return The DungeonMapData, nullable
   * @see #getFovAlgorithm()
   */
  public DungeonMapData getDungeonMapData() {
    if (StringUtils.isBlank(fovAlgorithm)) {
      return null;
    }

    // Rather than store a DungeonMapData object in the XML definition, the only thing we should need is a fovAlgorithm.
    // (If this AreaMapData is used as a special room inside of a dungeon it will inherit the parent dungeon's DungeonMapData.)
    final DungeonMapData result = new DungeonMapData();
    result.name = name;
    result.fovAlgorithm = getFovAlgorithm().toString();

    return result;
  }

  public FieldOfViewType getFovAlgorithm() {
    if (StringUtils.isBlank(fovAlgorithm)) {
      return FieldOfViewType.ALL;
    }

    return FieldOfViewType.valueOf(fovAlgorithm);
  }

  public List<String> getNpcs() {
    if (npcs == null) {
      return Collections.emptyList();
    }

    return Collections.unmodifiableList(npcs);
  }

  public List<ExitData> getExits() {
    if (exits == null) {
      return Collections.emptyList();
    }

    return Collections.unmodifiableList(exits);
  }
}
