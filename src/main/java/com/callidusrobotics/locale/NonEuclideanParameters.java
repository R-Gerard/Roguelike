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

import java.util.Random;

import javax.xml.bind.annotation.XmlElement;

/**
 * Helper POJO for parameters for the NonEuclideanDungeonBuilder.
 *
 * @author Rusty
 * @see DungeonMapData
 * @see NonEuclideanDungeonBuilder
 */
class NonEuclideanParameters implements DungeonSubtypeParameters {
  @XmlElement(required = true) protected int minRooms, maxRooms;
  @XmlElement(required = true) protected int rectangleRoomFreq, hallwayFrequency, roundRoomFreq;
  @XmlElement(required = true) protected boolean hasStairs;

  private transient boolean initialized = false;

  NonEuclideanParameters() { /* Required by JAXB */ }

  public NonEuclideanParameters(final int minRooms, final int maxRooms, final int rectangleRoomFreq, final int hallwayFrequency, final int roundRoomFreq, final boolean hasStairs) {

    this.minRooms = Math.max(Math.min(minRooms, maxRooms), 5);
    this.maxRooms = Math.max(minRooms, maxRooms);

    this.rectangleRoomFreq = Math.max(0, rectangleRoomFreq);
    this.hallwayFrequency = Math.max(0, hallwayFrequency);
    this.roundRoomFreq = Math.max(0, roundRoomFreq);

    this.hasStairs = hasStairs;
  }

  @Override
  public void normalizeParameters() {
    if (initialized) {
      return;
    }
    initialized = true;

    final int roomFrequencies = rectangleRoomFreq + hallwayFrequency + roundRoomFreq;

    rectangleRoomFreq = 1000 * rectangleRoomFreq / roomFrequencies;
    hallwayFrequency = 1000 * hallwayFrequency / roomFrequencies;
    roundRoomFreq = 1000 * roundRoomFreq / roomFrequencies;
  }

  @Override
  public RoomType getRandomRoomType(final Random random) {
    normalizeParameters();
    final int seed = random.nextInt(1000);

    int cumulativeP = rectangleRoomFreq;
    if (seed <= cumulativeP) {
      return RoomType.RECTANGLE;
    }

    cumulativeP += hallwayFrequency;
    if (seed <= cumulativeP) {
      if (seed % 3 == 0) {
        return RoomType.VERTICAL_HALL;
      } else if (seed % 3 == 1) {
        return RoomType.HORIZONTAL_HALL;
      } else {
        return RoomType.INTERSECTION;
      }
    }

    return RoomType.CIRCLE;
  }

  @Override
  public int getMinSize() {
    return minRooms;
  }

  @Override
  public int getMaxSize() {
    return maxRooms;
  }
}
