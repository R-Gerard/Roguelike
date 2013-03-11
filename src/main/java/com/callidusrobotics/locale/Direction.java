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
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.callidusrobotics.command.Command;

/**
 * A superclass of named points in 3D space (2D compass points, Up, and Down).
 *
 * @author Rusty
 * @since 0.0.1
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public enum Direction {
  NORTH, SOUTH, EAST, WEST,
  NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST,
  UP, DOWN;

  public static final List<Direction> COMPASS_POINTS;
  public static final List<Direction> ORDINAL_POINTS;
  public static final List<Direction> CARDINAL_POINTS;

  static {
    final Direction[] cardinalPts = { NORTH, SOUTH, EAST, WEST };
    CARDINAL_POINTS = Collections.unmodifiableList(Arrays.asList(cardinalPts));

    final Direction[] ordinalPts = { NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST };
    ORDINAL_POINTS = Collections.unmodifiableList(Arrays.asList(ordinalPts));

    final Direction[] compassPts = ArrayUtils.addAll(cardinalPts, ordinalPts);
    COMPASS_POINTS = Collections.unmodifiableList(Arrays.asList(compassPts));
  }

  public static Direction fromCommand(final Command command) {
    if (command == null) {
      return null;
    }

    try {
      return Direction.valueOf(command.toString());
    } catch (final IllegalArgumentException e) {
      return null;
    }
  }

  public Direction opposite() {
    switch (this) {
      case NORTH:
        return SOUTH;

      case SOUTH:
        return NORTH;

      case EAST:
        return WEST;

      case WEST:
        return EAST;

      case UP:
        return DOWN;

      case DOWN:
        return UP;

      case NORTHEAST:
        return SOUTHWEST;

      case NORTHWEST:
        return SOUTHEAST;

      case SOUTHWEST:
        return NORTHEAST;

      case SOUTHEAST:
        return NORTHWEST;

      default:
        return null;
    }
  }

  public Command toCommand() {
    switch (this) {
      case UP:
        return Command.ASCEND;

      case DOWN:
        return Command.DESCEND;

      default:
        return Command.valueOf(toString());
    }
  }

  public MutableCoordinate toCoordinate() {
    int rowOffset = 0;
    int colOffset = 0;

    final String val = toString();
    if (val.contains(NORTH.toString())) {
      rowOffset = -1;
    }

    if (val.contains(SOUTH.toString())) {
      rowOffset = 1;
    }

    if (val.contains(EAST.toString())) {
      colOffset = 1;
    }

    if (val.contains(WEST.toString())) {
      colOffset = -1;
    }

    return new MutableCoordinate(rowOffset, colOffset);
  }
}
