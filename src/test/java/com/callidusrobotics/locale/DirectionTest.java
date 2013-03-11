package com.callidusrobotics.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.callidusrobotics.locale.Direction.*;

import java.util.List;

import org.junit.Test;

public class DirectionTest {

  @Test
  public void testCardinalDirections() throws Exception {
    // The cardinal directions (N,S,E,W)
    final Direction[] expectedElements = { NORTH, SOUTH, EAST, WEST };

    // The cardinal directions (N,S,E,W) must be the lowest order
    for (final Direction direction : expectedElements) {
      testDirection(direction, 0, 3);
    }

    // The collection of cardinal directions must contain the expected elements
    testCollection(CARDINAL_POINTS, 4, "cardinal directions", expectedElements);
  }

  @Test
  public void testOrdinalDirections() throws Exception {
    // The ordinal directions (NE,NW,SE,SW)
    final Direction[] expectedElements = { NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST };

    // The cardinal directions (N,S,E,W) must be the lowest order
    for (final Direction direction : expectedElements) {
      testDirection(direction, 4, 7);
    }

    // The collection of cardinal directions must contain the expected elements
    testCollection(ORDINAL_POINTS, 4, "ordinal directions", expectedElements);
  }

  @Test
  public void testCompassPoints() throws Exception {
    // The collection of compass points must contain all cardinal and ordinal values
    testCollection(COMPASS_POINTS, 8, "compass points", CARDINAL_POINTS.toArray(new Direction[4]));
    testCollection(COMPASS_POINTS, 8, "compass points", ORDINAL_POINTS.toArray(new Direction[4]));
  }

  private void testDirection(final Direction direction, final int minVal, final int maxVal) {
    assertTrue(direction + " must be in the range [" + minVal + "," + maxVal + "].", direction.ordinal() >= minVal);
    assertTrue(direction + " must be in the range [" + minVal + "," + maxVal + "].", direction.ordinal() <= maxVal);
  }

  private void testCollection(final List<Direction> collection, final int expectedSize, final String name, final Direction[] expectedElements) {
    assertEquals("Collection of " + name + " must be exactly " + expectedSize + " elements.", expectedSize, collection.size());

    for (final Direction direction : expectedElements) {
      assertTrue("Collection of " + name + " must contain '" + direction + "'.", collection.contains(direction));
    }
  }
}
