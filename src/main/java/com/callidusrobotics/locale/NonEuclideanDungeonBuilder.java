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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.callidusrobotics.ConsoleColleague;
import com.callidusrobotics.locale.DungeonLevel.Room;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.XmlMarshaller;

/**
 * Builds a non-euclidean dungeon.
 * <p>
 * Each room in the dungeon is a DungeonLevel object made of a single room.
 * <p>
 * Each room is treated as a node in a graph and each exit from the room is a
 * vertex. Serialization and de-serialization is handled by constructing a
 * spanning tree of room nodes.
 * <p>
 * <ol>
 * <li>Create a list of N levels</li>
 * <ul>
 * <li><a. Assign each room a random shape according to the
 * NonEuclideanParameters</li>
 * <li>Assign each room [2, 4] exits (or [2, 5] exits if stairs are enabled)</li>
 * </ul>
 * <li>Select room 0, assign it to have 4 exits</li>
 * <li>For each room, select a neighbor</li>
 * <ul>
 * <li>Pick an exit that is not yet connected to another room (e.g. NORTH)</li>
 * <li>Search using quadratic probing for a room that has an unconnected
 * opposite exit (e.g. SOUTH)</li>
 * <li>neighbor = (roomNumber + numExitsInThisRoom + collisions / 2 + collisions
 * * collisions / 2) % N</li>
 * <li>Connect the rooms</li>
 * </ul>
 * <li>Repeat neighbor selection a large number of times (e.g. 1000)</li>
 * <li>Detect orphans (rooms that are not connected to room 0)</li>
 * <li>For each orphan</li>
 * <ul>
 * <li>Pick an exit that is not yet connected</li>
 * <li>Find the first non-orphan room with an opposite exit that is unused</li>
 * <li>Connect the rooms</li>
 * </ul>
 * <li>Done</li>
 * </ol>
 *
 * @author Rusty
 * @since 0.0.1
 * @see NonEuclideanParameters
 * @see DungeonGraphString
 */
@Builds(DungeonType.NON_EUCLIDEAN)
final class NonEuclideanDungeonBuilder extends AbstractDungeonBuilder {
  static class MazeTraverser {
    private MazeTraverser() {
      throw new NotInstantiableError();
    }

    static void depthFirstTraversal(final DungeonLevel currentLevel) {
      currentLevel.setIsVisited(true);

      for (final ExitData exit : currentLevel.neighbors) {
        final DungeonLevel neighbor = exit == null ? null : exit.load(currentLevel.depth + 1);

        if (neighbor != null && !neighbor.isVisited()) {
          depthFirstTraversal(neighbor);
        }
      }
    }
  }

  private final List<DungeonLevel> specialRooms = new LinkedList<DungeonLevel>();
  private final List<Direction> specialRoomExits = new LinkedList<Direction>();
  private int specialRoomCount = 0;

  @Override
  public DungeonLevel fromString(final String serializedDungeon) {
    final DungeonGraphString buffer = new DungeonGraphString(serializedDungeon);

    final int numRooms = buffer.getNumRooms();
    levels = new ArrayList<DungeonLevel>(numRooms);

    for (int i = 0; i < numRooms; i++) {
      final RoomType roomType = buffer.getRoomType(i);
      final int height = buffer.getRoomHeight(i);
      final int width = buffer.getRoomWidth(i);

      final String name = StringUtils.capitalize(StringUtils.lowerCase(StringUtils.replace(roomType.toString(), "_", " ")));
      final DungeonLevel canvas = RoomFactory.makeCanvas(height, width, dungeonMapData, i);
      canvas.setName(name);
      final Room dims = RoomFactory.drawRoom(canvas, roomType, canvas.getPosition(), height, width);
      canvas.setPosition(ConsoleColleague.getPosition(canvas));

      Validate.isTrue(dims.getHeight() == canvas.getHeight() && dims.getWidth() == canvas.getWidth(), "Dungeon data is corrupted");
      levels.add(canvas);
    }

    linkRooms(buffer);

    // Attach the special levels to the dungeon
    int collisionCount = 1;
    while (collisionCount++ < 1000 && specialRoomCount < specialRooms.size()) {
      final int index = levels.size() - (1 + collisionCount % levels.size());
      if (specialRoomCount < specialRooms.size() && levels.get(index).getNeighbor(specialRoomExits.get(specialRoomCount).opposite()) == null) {
        // Attach the special room to the dungeon before we set the dungeonMapData
        final DungeonLevel room1 = specialRooms.get(specialRoomCount);
        final DungeonLevel room2 = levels.get(index);
        final Direction direction = specialRoomExits.get(specialRoomCount);
        createExit(room2, direction.opposite());
        room1.setNeighbor(room2, direction);

        // Set the dungeonMapData so that if the special room is dangerous it can be populated with random monsters from this dungeon
        specialRooms.get(specialRoomCount).setDungeonMapData(dungeonMapData);

        // Set the special room is as dangerous as its neighbor we connected it to
        specialRooms.get(specialRoomCount).setDepth(levels.get(index).getDepth());
        specialRoomCount++;
      } else {
        collisionCount++;
      }
    }

    return levels.get(0);
  }

  @Override
  public String buildRandomDungeonString() {
    final int maxRooms = dungeonMapData.getMinLevels() + dungeonMapData.nextInt(dungeonMapData.getMaxLevels() - dungeonMapData.getMinLevels() + 1);

    // Build a list of placeholder rooms to generate the string from
    levels = new ArrayList<DungeonLevel>(maxRooms);
    final DungeonGraphString buffer = new DungeonGraphString(maxRooms);

    for (int i = 0; i < maxRooms; i++) {
      // Create a dungeonLevel as a placeholder; we may change its type later if necessary
      final DungeonLevel level = makeRandomRoom(dungeonMapData, i);

      levels.add(level);

      final int maxNeighbors = getMaxNeighbors();
      int numNeighbors = 2 + dungeonMapData.nextInt(maxNeighbors - 1);
      while (numNeighbors > 0) {
        final Direction direction = getDirection(dungeonMapData.nextInt(getDirections().size()));

        // Don't allow both up and down stairs in the same room
        if ((direction == Direction.UP || direction == Direction.DOWN) && buffer.isRoomExit(i, direction.opposite())) {
          continue;
        }

        if (!buffer.isRoomExit(i, direction)) {
          numNeighbors--;
          buffer.setRoomExit(i, direction);
        }
      }

      buffer.setRoomType(i, level.getRooms().get(0).getType());
      buffer.setRoomHeight(i, level.getHeight());
      buffer.setRoomWidth(i, level.getWidth());
    }

    // Set the root node of the spanning tree to have 4 exits
    for (final Direction direction : Direction.CARDINAL_POINTS) {
      buffer.setRoomExit(0, direction);
    }

    // Link the rooms together in a non-linear way
    linkRooms(buffer);

    // Re-map rooms with funky connections (e.g. straight hallways with more than two exits)
    for (int i = 0; i < buffer.getNumRooms(); i++) {
      fixRooms(buffer, i);
    }

    // Clear the placeholder rooms
    levels.clear();

    return buffer.toString();
  }

  @Override
  public void setDungeonMapData(final DungeonMapData dungeonMapData) throws IOException {
    super.setDungeonMapData(dungeonMapData);

    final XmlMarshaller xmlMarshaller = new XmlMarshaller(AreaMapData.class);

    for (final String xmlFile : dungeonMapData.specialRoomFiles) {
      final AreaMapData roomBuilder = (AreaMapData) xmlMarshaller.unmarshalSystemResource(xmlFile);
      final DungeonLevel specialRoom = roomBuilder.buildLevel(0);
      if (specialRoom != null && roomBuilder.exits != null) {
        specialRoom.setPosition(ConsoleColleague.getPosition(specialRoom));
        specialRoomExits.add(roomBuilder.exits.get(0).getDirection());
        specialRooms.add(specialRoom);
      }
    }
  }

  @Override
  protected void createExit(final DungeonLevel dungeonLevel, final Direction direction) {
    int row, col;

    if (direction == Direction.NORTH) {
      row = 0;
    } else if (direction == Direction.SOUTH) {
      row = dungeonLevel.getHeight() - 1;
    } else {
      row = dungeonLevel.getHeight() / 2;
    }

    if (direction == Direction.EAST) {
      col = dungeonLevel.getWidth() - 1;
    } else if (direction == Direction.WEST) {
      col = 0;
    } else {
      col = dungeonLevel.getWidth() / 2;
    }

    final Coordinate position = new Coordinate(row, col);
    dungeonLevel.setExitAbsolute(position, direction, dungeonMapData.makeExitTile(direction));
  }

  protected DungeonLevel makeRandomRoom(final DungeonMapData dungeonMapData, final int depth) {
    final RoomType roomType = dungeonMapData.getRandomRoomType();
    int height = 10 + dungeonMapData.nextInt(ConsoleColleague.getHeight() - 10);
    int width = 10 + dungeonMapData.nextInt(ConsoleColleague.getDivColumn() - 10);

    if (height % 2 == 0) {
      height--;
    }

    if (width % 2 == 0) {
      width--;
    }

    DungeonLevel canvas = new DungeonLevel(height, width, "temp", depth, dungeonMapData.makeInvisibleBarrier());
    canvas.setDungeonMapData(dungeonMapData);
    final Room dims = RoomFactory.drawRoom(canvas, roomType, canvas.getPosition(), height, width);

    // The RoomFactory had to re-size the room according to the parameters, so re-build it to fit perfectly
    // We don't actually need to call the RoomFactory again to re-draw the tiles because this is a throw-away object anyway
    if (dims.getHeight() != height || dims.getWidth() != width) {
      canvas = new DungeonLevel(dims.getHeight(), dims.getWidth(), "temp", depth, dungeonMapData.makeInvisibleBarrier());
      canvas.getRooms().add(dims);
    }

    return canvas;
  }

  public Collection<Direction> getDirections() {
    if (dungeonMapData.neParameters.hasStairs) {
      return DungeonGraphString.DIRECTION_BITMAP.keySet();
    }

    return Direction.CARDINAL_POINTS;
  }

  public Direction getDirection(final int index) {
    return (Direction) getDirections().toArray()[index];
  }

  public int getMaxNeighbors() {
    // Don't allow ascending and descending stairs in the same dungeonLevel
    if (getDirections().contains(Direction.UP)) {
      return getDirections().size() - 1;
    }

    return getDirections().size();
  }

  private void linkRooms(final DungeonGraphString buffer) {
    for (int i = 0; i < levels.size(); i++) {
      int neighborCount = 0;

      for (final Direction direction : getDirections()) {
        if (buffer.isRoomExit(i, direction)) {
          neighborCount++;
          int collisions = 0;
          boolean linked = levels.get(i).getNeighbor(direction) != null;

          int sanityCount = 0;
          while (!linked && sanityCount++ < 1000) {
            collisions++;
            final int index = (i + neighborCount + collisions / 2 + collisions * collisions / 2) % levels.size();
            linked = levels.get(index).getNeighbor(direction.opposite()) == null && buffer.isRoomExit(index, direction.opposite());

            if (linked) {
              createExit(levels.get(i), direction);
              createExit(levels.get(index), direction.opposite());
              levels.get(i).setNeighbor(levels.get(index), direction);
            }
          }
        }
      }
    }

    fixOrphans();
  }

  private void fixRooms(final DungeonGraphString buffer, final int roomNumber) {
    for (final Direction direction : getDirections()) {
      final boolean linked = buffer.isRoomExit(roomNumber, direction) && (levels.get(roomNumber).getNeighbor(direction) != null);
      if (!linked) {
        buffer.clearRoomExit(roomNumber, direction);
      }

      buffer.fixRoom(roomNumber);
    }
  }

  private void fixOrphans() {
    final List<DungeonLevel> orphans = new ArrayList<DungeonLevel>(levels.size());
    final List<DungeonLevel> siblings = new ArrayList<DungeonLevel>(levels.size());

    // Do a depth-first search of the rooms list and mark each room as visited
    MazeTraverser.depthFirstTraversal(levels.get(0));

    // Add each unvisited room to the orphans list
    for (final DungeonLevel dungeonLevel : levels) {
      if (dungeonLevel.isVisited()) {
        siblings.add(dungeonLevel);
        dungeonLevel.setIsVisited(false);
      } else {
        orphans.add(dungeonLevel);
      }
    }

    // For each orphaned dungeonLevel, connect it to the first potential sibling
    // If the orphan is fully connected to other orphans, then proceed to the next orphan in the list
    // Note that there is an extremely low probability of orphans and this code usually is not executed
    for (final DungeonLevel orphan : orphans) {
      boolean foundSibling = false;

      final Iterator<Direction> iterator = getDirections().iterator();
      while (iterator.hasNext() && !foundSibling) {
        final Direction exit = iterator.next();
        final int oppositeIndex = exit.opposite().ordinal();

        if (orphan.neighbors[exit.ordinal()] == null) {
          for (int j = 0; j < siblings.size() && !foundSibling; j++) {
            if (siblings.get(j).neighbors[oppositeIndex] == null) {
              foundSibling = true;

              final DungeonLevel sibling = siblings.get(j);

              createExit(orphan, exit);
              createExit(sibling, exit.opposite());
              orphan.setNeighbor(sibling, exit);
            }
          }
        }
      }
    }
  }
}
