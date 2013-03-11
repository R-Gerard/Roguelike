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

import static com.callidusrobotics.locale.DungeonGraphString.PASSAGE_WIDTH_MAX;
import static com.callidusrobotics.locale.DungeonGraphString.PASSAGE_WIDTH_MIN;

import java.util.Set;

import org.apache.commons.lang.Validate;

import com.callidusrobotics.ConsoleColleague;
import com.callidusrobotics.locale.DungeonLevel.Room;
import com.callidusrobotics.swing.ConsoleFactory;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.RasterGeometry;

/**
 * Draws rooms in DungeonLevel objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonLevel
 */
public final class RoomFactory {

  private RoomFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Makes a blank DungeonLevel.
   *
   * @param height
   *          The number of rows in the DungeonLevel
   * @param width
   *          The number of columns in the DungeonLevel
   * @param dungeonMapData
   *          The DungeonMapData to use (for Tile data), not null
   * @param depth
   *          The depth of the DungeonLevel
   * @return A blank DungeonLevel, never null
   */
  public static DungeonLevel makeCanvas(final int height, final int width, final DungeonMapData dungeonMapData, final int depth) {
    final int rows = Math.min(height, ConsoleFactory.getInstance().getHeight());
    final int cols = Math.min(width, ConsoleColleague.getDivColumn());

    final DungeonLevel canvas = new DungeonLevel(rows, cols, "NOT SPECIFIED", depth, dungeonMapData.makeInvisibleBarrier());
    canvas.setDungeonMapData(dungeonMapData);

    return canvas;
  }

  /**
   * Draws a room.
   *
   * @param canvas
   *          The DungeonLevel to draw on, not null
   * @param roomType
   *          The shape of the room to draw, not null
   * @param topLeftCorner
   *          The location to position the room, not null
   * @param height
   *          The number of rows in the room
   * @param width
   *          The number of columns in the room
   * @return A Room object representing the final position and
   *         dimensions of what was drawn, never null
   */
  public static Room drawRoom(final DungeonLevel canvas, final RoomType roomType, final Coordinate topLeftCorner, final int height, final int width) {
    Validate.notNull(canvas.getDungeonMapData());

    Room room;
    int passageWidth;
    switch (roomType) {
      case RECTANGLE:
        room = RoomFactory.drawRectangularRoom(canvas, height, width, topLeftCorner);
        break;

      case VERTICAL_HALL:
        passageWidth = height / 2 % 2 == 0 ? PASSAGE_WIDTH_MIN : PASSAGE_WIDTH_MAX;
        room = RoomFactory.drawRectangularRoom(canvas, height, passageWidth, topLeftCorner);
        room.type = RoomType.VERTICAL_HALL;
        break;

      case HORIZONTAL_HALL:
        passageWidth = width / 2 % 2 == 0 ? PASSAGE_WIDTH_MIN : PASSAGE_WIDTH_MAX;
        room = RoomFactory.drawRectangularRoom(canvas, passageWidth, width, topLeftCorner);
        room.type = RoomType.HORIZONTAL_HALL;
        break;

      case INTERSECTION:
        passageWidth = (height + width) / 2 % 2 == 0 ? PASSAGE_WIDTH_MIN : PASSAGE_WIDTH_MAX;
        room = RoomFactory.drawCrossIntersection(canvas, height, width, passageWidth, topLeftCorner);
        break;

      case CIRCLE:
      default:
        room = RoomFactory.drawRoundRoom(canvas, Math.min(height, width), topLeftCorner);
    }

    return room;
  }

  public static Room drawCrossIntersection(final DungeonLevel canvas, final int height, final int width, final int passageWidth, final Coordinate topLeftCorner) {
    Validate.notNull(canvas.getDungeonMapData());

    final int rows = getOddNumber(PASSAGE_WIDTH_MAX, canvas.getHeight(), height);
    final int cols = getOddNumber(PASSAGE_WIDTH_MAX, canvas.getWidth(), width);
    final int passageRows = getOddNumber(PASSAGE_WIDTH_MIN, PASSAGE_WIDTH_MAX, passageWidth);

    // Endpoints of 4 line segments
    final int[] hDim = { 0, rows / 2 - passageRows / 2, rows / 2 + passageRows / 2, rows - 1 };
    final int[] vDim = { 0, cols / 2 - passageRows / 2, cols / 2 + passageRows / 2, cols - 1 };

    for (int i = 0; i < 4; i++) {
      hDim[i] += topLeftCorner.getRow();
      vDim[i] += topLeftCorner.getCol();
    }

    drawWall(canvas, new Coordinate(hDim[0], vDim[1]), new Coordinate(hDim[0], vDim[2])); // First row
    drawWall(canvas, new Coordinate(hDim[1], vDim[0]), new Coordinate(hDim[1], vDim[3])); // Second row
    drawWall(canvas, new Coordinate(hDim[2], vDim[0]), new Coordinate(hDim[2], vDim[3])); // Third row
    drawWall(canvas, new Coordinate(hDim[3], vDim[1]), new Coordinate(hDim[3], vDim[2])); // Fourth row
    drawWall(canvas, new Coordinate(hDim[1], vDim[0]), new Coordinate(hDim[2], vDim[0])); // First col
    drawWall(canvas, new Coordinate(hDim[0], vDim[1]), new Coordinate(hDim[3], vDim[1])); // Second col
    drawWall(canvas, new Coordinate(hDim[0], vDim[2]), new Coordinate(hDim[3], vDim[2])); // Third col
    drawWall(canvas, new Coordinate(hDim[1], vDim[3]), new Coordinate(hDim[2], vDim[3])); // Fourth col

    // Blank out the walls that overlap with each other
    for (int r = 1; r < rows - 1; r++) {
      for (int c = 1; c < cols - 1; c++) {
        if (hDim[1] < r && r < hDim[2] || vDim[1] < c && c < vDim[2]) {
          canvas.setTileAbsolute(r, c, canvas.getDungeonMapData().makeFloor());
        }
      }
    }

    final Room room = new Room(RoomType.INTERSECTION, topLeftCorner, rows, cols);
    canvas.rooms.add(room);
    return room;
  }

  public static Room drawRectangularRoom(final DungeonLevel canvas, final int height, final int width, final Coordinate topLeftCorner) {
    Validate.notNull(canvas.getDungeonMapData());

    final int rows = getOddNumber(PASSAGE_WIDTH_MIN, canvas.getHeight(), height);
    final int cols = getOddNumber(PASSAGE_WIDTH_MIN, canvas.getWidth(), width);

    final Set<Coordinate> floorPoints = RasterGeometry.getRectangle(topLeftCorner, topLeftCorner.add(new Coordinate(rows - 1, cols - 1)), true);
    for (final Coordinate point : floorPoints) {
      canvas.setTileAbsolute(point, canvas.getDungeonMapData().makeFloor());
    }

    final Set<Coordinate> wallPoints = RasterGeometry.getRectangle(topLeftCorner, topLeftCorner.add(new Coordinate(rows - 1, cols - 1)), false);
    for (final Coordinate point : wallPoints) {
      canvas.setTileAbsolute(point, canvas.getDungeonMapData().makeWall());
    }

    final Room room = new Room(RoomType.RECTANGLE, topLeftCorner, rows, cols);
    canvas.rooms.add(room);
    return room;
  }

  public static Room drawRoundRoom(final DungeonLevel canvas, final int diameter, final Coordinate topLeftCorner) {
    Validate.notNull(canvas.getDungeonMapData());

    final int radius = Math.min((diameter - 1) / 2, Math.min(canvas.getWidth(), canvas.getHeight()));

    final Set<Coordinate> floorPoints = RasterGeometry.getCircle(topLeftCorner.add(new Coordinate(radius, radius)), radius, true);
    for (final Coordinate point : floorPoints) {
      canvas.setTileAbsolute(point, canvas.getDungeonMapData().makeFloor());
    }

    final Set<Coordinate> wallPoints = RasterGeometry.getCircle(topLeftCorner.add(new Coordinate(radius, radius)), radius, false);
    for (final Coordinate point : wallPoints) {
      canvas.setTileAbsolute(point, canvas.getDungeonMapData().makeWall());
    }

    final Room room = new Room(RoomType.CIRCLE, topLeftCorner, 2 * radius + 1, 2 * radius + 1);
    canvas.rooms.add(room);
    return room;
  }

  public static int getOddNumber(final int min, final int max, final int value) {
    final int oddMin = min + 1 - (min % 2);
    final int oddMax = max - 1 + (max % 2);

    if (value < oddMin) {
      return oddMin;
    }

    if (value > oddMax) {
      return oddMax;
    }

    if (value % 2 == 0) {
      return value - 1;
    }

    return value;
  }

  private static void drawWall(final DungeonLevel canvas, final Coordinate pointA, final Coordinate pointB) {
    Validate.notNull(canvas.getDungeonMapData());

    final Set<Coordinate> points = RasterGeometry.getLine(pointA, pointB);
    for (final Coordinate point : points) {
      canvas.setTileAbsolute(point, canvas.getDungeonMapData().makeWall());
    }
  }
}
