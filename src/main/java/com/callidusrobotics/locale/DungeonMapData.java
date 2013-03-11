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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.object.actor.NonPlayerCharacterFactory;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.XmlMarshaller;

/**
 * POJO for XML data describing a procedurally-generated dungeon, including Tile
 * data, spawning NonPlayerCharacter objects, FieldOfViewType etc.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonBuilder
 */
@SuppressWarnings("PMD.TooManyMethods")
@XmlRootElement
final class DungeonMapData {
    static class SpawnEntry {
      @XmlAttribute(required = true, name = "relativeFrequency") int frequency;
      @XmlValue String npcId;

      SpawnEntry() { /* Required by JAXB */ }

      SpawnEntry(final int frequency, final String npcId) {
        Validate.isTrue(frequency > 0);
        Validate.notEmpty(npcId);

        this.frequency = frequency;
        this.npcId = npcId;
      }

      @Override
      public boolean equals(final Object other) {
        if (! (other instanceof SpawnEntry)) {
          return false;
        }

        final SpawnEntry otherEntry = (SpawnEntry) other;
        return npcId.equals(otherEntry.npcId);
      }

      @Override
      public int hashCode() {
        return npcId.hashCode();
      }
    }

    static class SpawnList {
      @XmlAttribute(required = true) int startLevel;
      @XmlElement(required = true, name = "npc") List<SpawnEntry> npcs;

      transient int npcFrequencies = 0;

      SpawnList() { /* Required by JAXB */ }

      SpawnList(final int startLevel, final List<SpawnEntry> npcs) {
        this.startLevel = startLevel;
        this.npcs = npcs;
      }
    }

    @XmlAttribute(required = true) String name;
    @XmlAttribute(required = true, name = "type") String dungeonType;
    @XmlElement(required = true) String foreground;
    @XmlElement(required = false) String background = null;
    @XmlElement(required = false, name = "nonEuclideanParameters") NonEuclideanParameters neParameters = null;
    @XmlElement(required = true, name = "npcPopulationParameters") NpcPopulationParameters npcPopParams;
    @XmlElementWrapper(required = true) @XmlElement(name = "spawnList") List<SpawnList> spawnLists;
    @XmlElementWrapper(required = false) @XmlElement(name = "tile") List<TileData> tiles = null;

    @XmlElementWrapper(required = false) @XmlElement(name = "specialRoomFile") List<String> specialRoomFiles = new ArrayList<String>();

    private transient final Random random = new Random();
    private transient DungeonSubtypeParameters subtypeParameters = null;

    public static DungeonMapData fromFile(final String xmlFile) throws IOException {
      final XmlMarshaller xmlMarshaller = new XmlMarshaller(DungeonMapData.class);
      return (DungeonMapData) xmlMarshaller.unmarshalSystemResource(xmlFile);
    }

    DungeonMapData() { /* Required by JAXB */ }

    DungeonMapData(final String name, final TrueColor foreground, final DungeonType dungeonType, final NonEuclideanParameters subtypeParameters, final NpcPopulationParameters spawnParameters, final List<SpawnList> spawnLists, final List<TileData> tiles, final List<String> specialRoomFiles) {
      this.name = name;
      this.foreground = foreground.toString();

      this.dungeonType = dungeonType.toString();
      this.subtypeParameters = this.neParameters = subtypeParameters;

      this.npcPopParams = spawnParameters;
      this.spawnLists = spawnLists;
      this.tiles = tiles;
      this.specialRoomFiles = specialRoomFiles;

      Validate.notNull(subtypeParameters);
      Validate.notNull(spawnParameters);
    }

    public DungeonType getDungeonType() {
      return DungeonType.valueOf(dungeonType);
    }

    TileData getStandardTile(final String name) {
      for (final TileData tile : tiles) {
        if (tile.getName().equals(name)) {
          return tile;
        }
      }

      return null;
    }

    private void normalizeParameters() {
      // Normalize the parameters only once to avoid rounding errors
      if (subtypeParameters != null) {
        return;
      }

      // TODO: Check each DungeonSubtypeParameters object and pick the first one that isn't null
      subtypeParameters = neParameters;

      if (subtypeParameters == null) {
        return;
      }

      subtypeParameters.normalizeParameters();

      for (final SpawnList list : spawnLists) {
        list.npcFrequencies = 0;

        for (final SpawnEntry entry : list.npcs) {
          list.npcFrequencies += entry.frequency;
        }
      }
    }

    int getMinLevels() {
      if (subtypeParameters == null) {
        normalizeParameters();
      }

      return subtypeParameters.getMinSize();
    }

    int getMaxLevels() {
      if (subtypeParameters == null) {
        normalizeParameters();
      }

      return subtypeParameters.getMaxSize();
    }

    TrueColor getForeground() {
      if (foreground == null) {
        foreground = Tile.makeDefaultWall().getForeground().toString();
      }

      return new TrueColor(Integer.decode(foreground));
    }

    TrueColor getBackground() {
      if (background == null) {
        return getForeground().darker();
      }

      return new TrueColor(Integer.decode(background));
    }

    int nextInt() {
      return random.nextInt();
    }

    int nextInt(final int limit) {
      return Math.max(0, random.nextInt(Math.max(1, limit)));
    }

    RoomType getRandomRoomType() {
      if (subtypeParameters == null) {
        normalizeParameters();
      }

      return subtypeParameters.getRandomRoomType(random);
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void addRandomMonstersToLevel(final DungeonLevel currentLevel, final PlayerCharacter player) {
      if (!currentLevel.isDangerous()) {
        return;
      }

      normalizeParameters();
      int numMonsters = getNumMonsters(currentLevel);
      final SpawnList spawnList = getSpawnList(currentLevel.getDepth());
      while (numMonsters-- > 0) {
        final int seed = random.nextInt(spawnList.npcFrequencies);

        int cumulativeP = 0;
        for (final SpawnEntry entry : spawnList.npcs) {
          cumulativeP += entry.frequency;
          if (seed <= cumulativeP) {
            final Coordinate position = currentLevel.getRandomCoordinate();
            if (position != null) {
              currentLevel.getNonPlayerCharacters().add(NonPlayerCharacterFactory.makeNpc(entry.npcId, position));
              break;
            }
          }
        }
      }
    }

    private int getNumMonsters(final DungeonLevel currentLevel) {
      // Don't populate safe levels
      if (!currentLevel.isDangerous()) {
        return 0;
      }

      // Don't re-populate dead-end levels
      if (currentLevel.isVisited() && currentLevel.getNumNeighbors() == 1) {
        return 0;
      }

      final int min = currentLevel.isVisited() ? npcPopParams.respawnParameters.min : npcPopParams.spawnParameters.min;
      final int max = currentLevel.isVisited() ? npcPopParams.respawnParameters.max : npcPopParams.spawnParameters.max;
      final int cap = npcPopParams.populationCap - currentLevel.getNonPlayerCharacters().size();

      return Math.min(cap, min + random.nextInt(1 + max - min));
    }

    private SpawnList getSpawnList(final int level) {
      if (spawnLists == null || spawnLists.isEmpty()) {
        return null;
      }

      int index = 0;
      for (final SpawnList list : spawnLists) {
        if (list.startLevel >= level) {
          return spawnLists.get(index);
        }

        index++;
      }

      return spawnLists.get(spawnLists.size() - 1);
    }

  public Tile makeInvisibleBarrier() {
    if (getStandardTile(Tile.INV_BARRIER_NAME) == null) {
      return Tile.makeDefaultInvisibleBarrier();
    }

    return getStandardTile(Tile.INV_BARRIER_NAME).makeTile();
  }

  public Tile makeWall() {
    if (getStandardTile(Tile.WALL_NAME) == null) {
      return Tile.makeDefaultWall();
    }

    return getStandardTile(Tile.WALL_NAME).makeTile();
  }

  public Tile makeFloor() {
    if (getStandardTile(Tile.FLOOR_NAME) == null) {
      return Tile.makeDefaultFloor();
    }

    return getStandardTile(Tile.FLOOR_NAME).makeTile();
  }

  public Tile makeExitTile(final Direction direction) {
    final TrueColor foreground = getForeground();
    final TrueColor background = getBackground();

    if (direction == Direction.UP) {
      return new Tile(new ConsoleGraphic('<', foreground, background), "Stairs Up", "Stairs leading up.", false, false, Direction.UP);
    }

    if (direction == Direction.DOWN) {
      return new Tile(new ConsoleGraphic('>', foreground, background), "Stairs Down", "Stairs leading down.", false, false, Direction.DOWN);
    }

    return new Tile(new ConsoleGraphic(' ', TrueColor.BLACK, TrueColor.BLACK), "Exit", "Exit to the " + direction.toString().toLowerCase() + ".", false, false, direction);
  }
}
