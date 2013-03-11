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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;

/**
 * Wrapper object for the base64-encoded String representing a serialized
 * dungeon made of a spanning tree of room nodes.
 *
 * @author Rusty
 * @since 0.0.1
 * @see RoomType
 */
final class DungeonGraphString {
  public static final Map<Direction, Integer> DIRECTION_BITMAP;

  // bit-map parameters
  private static final int DIMENSIONS = 5;
  private static final int BYTES_PER_ROOM = 4;
  private static final int TYPE_OFFSET = 0;
  private static final int HEIGHT_OFFSET = 1;
  private static final int WIDTH_OFFSET = 2;
  private static final int EXITS_OFFSET = 3;

  // Room parameters
  public static final int PASSAGE_WIDTH_MIN = 3;
  public static final int PASSAGE_WIDTH_MAX = 5;

  byte[] buffer;

  static {
    // We only care about the cardinal directions (N,S,E,W) plus Up and Down, but there is a gap in their ordinal values that must be re-mapped
    final Map<Direction, Integer> directionBitMap = new LinkedHashMap<Direction, Integer>();

    for (final Direction direction : Direction.CARDINAL_POINTS) {
      directionBitMap.put(direction, direction.ordinal());
    }

    // Now add the values that are not contiguous with the cardinal points
    directionBitMap.put(Direction.UP, Direction.CARDINAL_POINTS.size() + 0);
    directionBitMap.put(Direction.DOWN, Direction.CARDINAL_POINTS.size() + 1);

    DIRECTION_BITMAP = Collections.unmodifiableMap(directionBitMap);
  }

  public DungeonGraphString(final int numRooms) {
    buffer = new byte[numRooms * BYTES_PER_ROOM];
  }

  public DungeonGraphString(final String serializedDungeon) {
    buffer = Base64.decodeBase64(serializedDungeon);
  }

  @Override
  public String toString() {
    return Base64.encodeBase64String(buffer);
  }

  public int getNumRooms() {
    return buffer.length / BYTES_PER_ROOM;
  }

  public void fixRoom(final int roomNumber) {
    final int numExits = getNumRoomExits(roomNumber);
    final int width = getRoomWidth(roomNumber);
    final int height = getRoomHeight(roomNumber);
    final int dimension = Collections.max(Arrays.asList(DIMENSIONS, width, height));
    final RoomType roomType = getRoomType(roomNumber);
    final boolean isHallway = roomType == RoomType.INTERSECTION || roomType == RoomType.VERTICAL_HALL || roomType == RoomType.HORIZONTAL_HALL;
    final boolean hasNorthExit = isRoomExit(roomNumber, Direction.NORTH);
    final boolean hasSouthExit = isRoomExit(roomNumber, Direction.SOUTH);
    final boolean hasEastExit = isRoomExit(roomNumber, Direction.EAST);
    final boolean hasWestExit = isRoomExit(roomNumber, Direction.WEST);

    if (!isHallway) {
      return;
    }

    if (numExits == 1) {
      setRoomType(roomNumber, RoomType.RECTANGLE);
      setRoomWidth(roomNumber, dimension);
      setRoomHeight(roomNumber, dimension);
      return;
    }

    if (numExits == 2) {
      if (hasNorthExit && hasSouthExit) {
        setRoomType(roomNumber, RoomType.VERTICAL_HALL);
        setRoomWidth(roomNumber, PASSAGE_WIDTH_MIN);
        setRoomHeight(roomNumber, Math.max(width, height));
        return;
      }

      if (hasEastExit && hasWestExit) {
        setRoomType(roomNumber, RoomType.HORIZONTAL_HALL);
        setRoomWidth(roomNumber, Math.max(width, height));
        setRoomHeight(roomNumber, PASSAGE_WIDTH_MIN);
        return;
      }
    }

    setRoomType(roomNumber, RoomType.INTERSECTION);
    setRoomWidth(roomNumber, Math.max(PASSAGE_WIDTH_MAX, width));
    setRoomHeight(roomNumber, Math.max(PASSAGE_WIDTH_MAX, height));
  }

  public void setRoomType(final int roomNumber, final RoomType roomType) {
    buffer[BYTES_PER_ROOM * roomNumber + TYPE_OFFSET] = (byte) roomType.ordinal();
  }

  public RoomType getRoomType(final int roomNumber) {
    return RoomType.values()[buffer[BYTES_PER_ROOM * roomNumber + TYPE_OFFSET]];
  }

  public void setRoomHeight(final int roomNumber, final int height) {
    buffer[BYTES_PER_ROOM * roomNumber + HEIGHT_OFFSET] = (byte) height;
  }

  public int getRoomHeight(final int roomNumber) {
    return buffer[BYTES_PER_ROOM * roomNumber + HEIGHT_OFFSET];
  }

  public void setRoomWidth(final int roomNumber, final int width) {
    buffer[BYTES_PER_ROOM * roomNumber + WIDTH_OFFSET] = (byte) width;
  }

  public int getRoomWidth(final int roomNumber) {
    return buffer[BYTES_PER_ROOM * roomNumber + WIDTH_OFFSET];
  }

  public void setRoomExit(final int roomNumber, final Direction direction) {
    Validate.isTrue(DIRECTION_BITMAP.keySet().contains(direction));
    buffer[BYTES_PER_ROOM * roomNumber + EXITS_OFFSET] |= (1 << DIRECTION_BITMAP.get(direction));
  }

  public void clearRoomExit(final int roomNumber, final Direction direction) {
    Validate.isTrue(DIRECTION_BITMAP.keySet().contains(direction));
    buffer[BYTES_PER_ROOM * roomNumber + EXITS_OFFSET] &= ~(1 << DIRECTION_BITMAP.get(direction));
  }

  public Direction[] getRoomExits(final int roomNumber) {
    final Direction[] exits = new Direction[getNumRoomExits(roomNumber)];

    int index = 0;
    for (final Direction direction : DIRECTION_BITMAP.keySet()) {
      if (isRoomExit(roomNumber, direction)) {
        exits[index++] = direction;
      }
    }

    return exits;
  }

  public int getNumRoomExits(final int roomNumber) {
    return Integer.bitCount(getRoomExitByte(roomNumber));
  }

  public boolean isRoomExit(final int roomNumber, final Direction direction) {
    Validate.isTrue(DIRECTION_BITMAP.keySet().contains(direction));
    return (getRoomExitByte(roomNumber) & (1 << DIRECTION_BITMAP.get(direction))) != 0;
  }

  private byte getRoomExitByte(final int roomNumber) {
    return buffer[BYTES_PER_ROOM * roomNumber + EXITS_OFFSET];
  }
}
