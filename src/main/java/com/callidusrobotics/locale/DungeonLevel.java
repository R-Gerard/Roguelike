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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.callidusrobotics.attribute.Dimensional;
import com.callidusrobotics.attribute.Positionable;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.NonPlayerCharacter;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.ui.ConsoleTextBox;

/**
 * Container object for Tile and NonPlayerCharacter data that the user interacts
 * with Typically produced by a DungeonBuilder as part of a
 * procedurally-generated dungeon, but could also be a statically-generated area
 * map.
 * <p>
 * Typically a DungeonLevel occupies the entire printable Console text area
 * (minus any regions reserved for message logs and other in-game data).
 * <p>
 * DungeonLevel objects are joined together to make larger areas by linking
 * neighbors and specific tiles (a.k.a. exits).
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonBuilder
 * @see DungeonMapData
 * @see AreaMapData
 * @see ExitData
 */
public class DungeonLevel implements ConsoleTextBox {
  static class Room implements Positionable, Dimensional {
    RoomType type;
    Coordinate topLeftCorner;
    int height, width;

    Room(final RoomType type, final Coordinate topLeftCorner, final int height, final int width) {
      this.type = type;
      this.topLeftCorner = new Coordinate(topLeftCorner);
      this.height = height;
      this.width = width;
    }

    @Override
    public int getHeight() {
      return height;
    }

    @Override
    public int getWidth() {
      return width;
    }

    @Override
    public Coordinate getPosition() {
      return topLeftCorner;
    }

    @Override
    public void setPosition(final int row, final int col) {
      topLeftCorner = new Coordinate(row, col);
    }

    @Override
    public void setPosition(final Coordinate position) {
      this.topLeftCorner = new Coordinate(position);
    }

    @Override
    public int getRow() {
      return topLeftCorner.getRow();
    }

    @Override
    public int getCol() {
      return topLeftCorner.getCol();
    }

    public RoomType getType() {
      return type;
    }
  }

  protected int depth;
  protected Tile tiles[][];
  protected String name;
  protected final MutableCoordinate topLeftCorner;

  protected List<Room> rooms = new ArrayList<Room>();
  protected ExitData[] neighbors;
  private Tile[] exits;

  protected boolean visited = false;
  protected boolean dangerous = true;

  protected final List<NonPlayerCharacter> npcs = new LinkedList<NonPlayerCharacter>();

  protected PlayerCharacter player = null;

  protected DungeonMapData dungeonMapData = null;

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public DungeonLevel(final int rows, final int columns, final String name, final int depth, final Tile defaultTile) {
    tiles = new Tile[rows][columns];
    this.name = name;
    this.depth = depth;
    topLeftCorner = new MutableCoordinate(0, 0);

    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        tiles[r][c] = new Tile(defaultTile);
      }
    }

    neighbors = new ExitData[Direction.values().length];
    exits = new Tile[Direction.values().length];
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(name);
    buffer.append(" (#" + depth + ") (" + getHeight() + "x" + getWidth() + ") ");
    buffer.append(dangerous ? "dangerous, " : "not dangerous, ");
    buffer.append(visited ? "visited, " : "not visited, ");
    buffer.append("[ ");

    for (final Direction direction : Direction.values()) {
      if (exits[direction.ordinal()] != null) {
        buffer.append(direction.toString());
        buffer.append(' ');
      }
    }

    buffer.append(']');

    return buffer.toString();
  }

  public final boolean checkCoordinatesRelative(final Coordinate position) {
    final int row = position.row - topLeftCorner.row;
    final int col = position.col - topLeftCorner.col;
    return row >= 0 && row < getHeight() && col >= 0 && col < getWidth();
  }

  public Coordinate getExitPositionRelative(final Direction direction) {
    final Tile exit = exits[direction.ordinal()];

    if (exit == null) {
      return null;
    }

    return exit.getPosition();
  }

  public List<Tile> getExits() {
    return Collections.unmodifiableList(Arrays.asList(exits));
  }

  public DungeonLevel getNeighbor(final Direction direction) {
    if (neighbors[direction.ordinal()] == null) {
      return null;
    }

    final boolean isLoaded = neighbors[direction.ordinal()].isLoaded();
    final DungeonLevel neighbor = neighbors[direction.ordinal()].load(depth);

    if (!isLoaded) {
      setNeighbor(neighbor, direction);
    }

    return neighbor;
  }

  void setNeighbor(final DungeonLevel neighbor, final Direction direction) {
    neighbors[direction.ordinal()] = new ExitData(direction, neighbor);
    neighbor.neighbors[direction.opposite().ordinal()] = new ExitData(direction.opposite(), this);
  }

  public int getNumNeighbors() {
    int count = 0;
    for (final ExitData neighbor : neighbors) {
      if (neighbor != null) {
        count++;
      }
    }

    return count;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  @Override
  public void setPosition(final Coordinate topLeftCorner) {
    setPosition(topLeftCorner.row, topLeftCorner.col);
  }

  @Override
  public void setPosition(final int row, final int col) {
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        tiles[r][c].setPosition(r + row, c + col);
      }
    }

    for (final Room room : rooms) {
      room.setPosition(room.getRow() - topLeftCorner.row + row, room.getCol() - topLeftCorner.col + col);
    }

    for (final NonPlayerCharacter npc : npcs) {
      // Set the position twice so that the last position does not correspond to a coordinate relative to the old topLeftCorner
      npc.setPosition(npc.getRow() - topLeftCorner.row + row, npc.getCol() - topLeftCorner.col + col);
      npc.setPosition(npc.getPosition());
    }

    topLeftCorner.row = row;
    topLeftCorner.col = col;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(final int depth) {
    this.depth = depth;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setIsVisited(final boolean visited) {
    this.visited = visited;
  }

  public boolean isDangerous() {
    return dangerous;
  }

  public void setIsDangerous(final boolean dangerous) {
    this.dangerous = dangerous;
  }

  public List<NonPlayerCharacter> getNonPlayerCharacters() {
    return npcs;
  }

  public Map<Integer, List<AbstractActor>> getAbstractActorsRankedByDistance(final Coordinate position, final boolean mustBeVisible) {
    final Map<Integer, List<AbstractActor>> distanceMap = getNonPlayerCharactersRankedByDistance(position, mustBeVisible);

    final boolean playerIsVisible = player == null ? false : getTileRelative(player.getPosition()).isVisible();
    if (player != null && (playerIsVisible || !mustBeVisible)) {
      final int dist2 = player.getPosition().distance2(position);
      if (!distanceMap.containsKey(dist2)) {
        distanceMap.put(dist2, new LinkedList<AbstractActor>());
      }

      distanceMap.get(dist2).add(player);
    }

    return distanceMap;
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  protected Map<Integer, List<AbstractActor>> getNonPlayerCharactersRankedByDistance(final Coordinate position, final boolean mustBeVisible) {
    final Map<Integer, List<AbstractActor>> distanceMap = new TreeMap<Integer, List<AbstractActor>>();

    for (final NonPlayerCharacter npc : npcs) {
      final boolean npcIsVisible = getTileRelative(npc.getPosition()).isVisible();
      if (!npcIsVisible && mustBeVisible) {
        continue;
      }

      final int dist2 = npc.getPosition().distance2(position);
      if (!distanceMap.containsKey(dist2)) {
        distanceMap.put(dist2, new LinkedList<AbstractActor>());
      }

      distanceMap.get(dist2).add(npc);
    }

    return distanceMap;
  }

  public AbstractActor getAbstractActorAtPosition(final Coordinate position) {
    // The player reference isn't initialized until GameMediator#playerEnterLevel() is called
    if (player != null && player.getPosition().equals(position)) {
      return player;
    }

    return getNonPlayerCharacterAtPosition(position);
  }

  protected NonPlayerCharacter getNonPlayerCharacterAtPosition(final Coordinate position) {
    for (final NonPlayerCharacter npc : npcs) {
      if (npc.getPosition().equals(position)) {
        return npc;
      }
    }

    return null;
  }

  /**
   * Selects a random empty Tile.
   *
   * @return Coordinate of empty Tile or null
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public Coordinate getRandomCoordinate() {
    final List<Coordinate> positions = new ArrayList<Coordinate>(getHeight() * getWidth());

    for (int r = 1; r < getHeight() - 1; r++) {
      for (int c = 1; c < getWidth() - 1; c++) {
        final Coordinate position = new MutableCoordinate(r, c).increment(topLeftCorner);
        if (isVacantRelative(position)) {
          positions.add(position);
        }
      }
    }

    if (!positions.isEmpty()) {
      final Random random = new Random();
      return positions.get(random.nextInt(positions.size()));
    }

    return null;
  }

  @Override
  public Coordinate getPosition() {
    return topLeftCorner;
  }

  @Override
  public int getRow() {
    return topLeftCorner.row;
  }

  @Override
  public int getCol() {
    return topLeftCorner.col;
  }

  @Override
  public final int getWidth() {
    return tiles[0].length;
  }

  @Override
  public final int getHeight() {
    return tiles.length;
  }

  public Tile getTileRelative(final Coordinate position) {
    return tiles[position.row - topLeftCorner.row][position.col - topLeftCorner.col];
  }

  /**
   * Checks if an AbstractActor can occupy the space.
   *
   * @param position
   *          The relative position to check, not null
   * @return True if the tile is not occupied and not a barrier, otherwise false
   * @see #checkCoordinatesRelative(Coordinate)
   * @see #getTileRelative(Coordinate)
   * @see #getAbstractActorAtPosition(Coordinate)
   * @see Tile#isBarrier()
   */
  public boolean isVacantRelative(final Coordinate position) {
    if (!checkCoordinatesRelative(position)) {
      return false;
    }

    if (getTileRelative(position).isBarrier()) {
      return false;
    }

    if (getAbstractActorAtPosition(position) != null) {
      return false;
    }

    return true;
  }

  /**
   * Exit-tile mutator. Package-Private implementation for use by the
   * RoomFactory; performs a shallow copy of the tile object.
   *
   * @param position
   *          The position of the tile
   * @param direction
   *          The direction of the exit
   * @param tile
   *          The tile to place in the dungeonLevel
   * @see RoomFactory
   * @see DungeonMapData
   */
  void setExitAbsolute(final Coordinate position, final Direction direction, final Tile tile) {
    setExitAbsolute(position.row, position.col, direction, tile);
  }

  /**
   * Exit-tile mutator. Package-Private implementation for use by the
   * RoomFactory; performs a shallow copy of the tile object.
   *
   * @param row
   *          The vertical position of the tile
   * @param col
   *          The horizontal position of the tile
   * @param direction
   *          The direction of the exit
   * @param tile
   *          The tile to place in the dungeonLevel
   * @see RoomFactory
   * @see DungeonMapData
   */
  void setExitAbsolute(final int row, final int col, final Direction direction, final Tile tile) {
    setTileAbsolute(row, col, tile);
    exits[direction.ordinal()] = tile;
  }

  /**
   * Tile mutator. Package-Private implementation for use by the RoomFactory;
   * performs a shallow copy of the tile object.
   *
   * @param position
   *          The position of the tile
   * @param tile
   *          The tile to place in the dungeonLevel
   * @see RoomFactory
   */
  void setTileAbsolute(final Coordinate position, final Tile tile) {
    setTileAbsolute(position.row, position.col, tile);
  }

  /**
   * Tile mutator. Package-Private implementation for use by the RoomFactory;
   * performs a shallow copy of the tile object.
   *
   * @param row
   *          The vertical position of the tile
   * @param col
   *          The horizontal position of the tile
   * @param tile
   *          The tile to place in the dungeonLevel
   * @see RoomFactory
   * @see DungeonMapData
   */
  void setTileAbsolute(final int row, final int col, final Tile tile) {
    tiles[row][col] = tile;
    tiles[row][col].setPosition(row + topLeftCorner.row, col + topLeftCorner.col);
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setPlayer(final PlayerCharacter player) {
    this.player = player;
  }

  public PlayerCharacter getPlayer() {
    return player;
  }

  /**
   * DungeonMapData accessor. Package-Private implementation for use by the
   * RoomFactory. Returns the parameters used to generate the dungeon that this
   * is apart of.
   *
   * @return The dungeon parameters
   * @see RoomFactory
   */
  DungeonMapData getDungeonMapData() {
    return dungeonMapData;
  }

  /**
   * The FoV strategy used to illuminate this level.
   *
   * @return The FieldOfViewStrategy, never null
   */
  public FieldOfViewStrategy getFovStrategy() {
    if (dungeonMapData != null && player != null) {
      return FieldOfViewFactory.makeFovStrategy(dungeonMapData.getFovAlgorithm());
    }

    return FieldOfViewFactory.makeFovStrategy(FieldOfViewType.ALL);
  }

  /**
   * Illuminates each Tile according to the field-of-view algorithm specified by
   * the DungeonMapData.
   *
   * @return List of coordinates that have changed illumination since the last
   *         call to this, never null
   * @see FieldOfViewFactory
   */
  public List<Coordinate> illuminate() {
    return getFovStrategy().illuminate(player, this);
  }

  /**
   * DungeonMapData mutator. Package-Private implementation for use by the
   * RoomFactory. Sets the parameters used to generate the dungeon that this is
   * apart of.
   *
   * @param dungeonMapData
   *          The dungeon parameters
   * @see RoomFactory
   */
  void setDungeonMapData(final DungeonMapData dungeonMapData) {
    this.dungeonMapData = dungeonMapData;
  }

  public void addRandomMonstersToLevel() {
    if (dungeonMapData == null) {
      return;
    }

    dungeonMapData.addRandomMonstersToLevel(this, player);
  }

  @Override
  public void draw(final Console console) {
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        tiles[r][c].draw(console);
      }
    }
  }
}
