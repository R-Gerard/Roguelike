package com.callidusrobotics.locale;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordinateTest {

  @Test
  public void isBetweenVerticalLine() throws Exception {
    final int col = 10;
    Coordinate a, b, c;

    a = new Coordinate(10, col);
    b = new Coordinate(15, col);
    c = new Coordinate(20, col);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenVerticalLineNegativeCoordinates() throws Exception {
    final int col = -10;
    Coordinate a, b, c;

    a = new Coordinate(-10, col);
    b = new Coordinate(-15, col);
    c = new Coordinate(-20, col);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenVerticalLineThroughOrigin() throws Exception {
    final int col = 0;
    Coordinate a, b, c;

    a = new Coordinate(-10, col);
    b = new Coordinate(  0, col);
    c = new Coordinate( 10, col);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenVerticalLineStartsAtOrigin() throws Exception {
    final int col = 0;
    Coordinate a, b, c;

    a = new Coordinate(0, col);
    b = new Coordinate(1, col);
    c = new Coordinate(2, col);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenHorizontalLine() throws Exception {
    final int row = 10;
    Coordinate a, b, c;

    a = new Coordinate(row, 10);
    b = new Coordinate(row, 15);
    c = new Coordinate(row, 20);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenHorizontalLineNegativeCoordinates() throws Exception {
    final int row = -10;
    Coordinate a, b, c;

    a = new Coordinate(row, -10);
    b = new Coordinate(row, -15);
    c = new Coordinate(row, -20);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenHorizontalLineThroughOrigin() throws Exception {
    final int row = 0;
    Coordinate a, b, c;

    a = new Coordinate(row, -10);
    b = new Coordinate(row,   0);
    c = new Coordinate(row,  10);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenHorizontalLineStartsAtOrigin() throws Exception {
    final int row = 0;
    Coordinate a, b, c;

    a = new Coordinate(row, 0);
    b = new Coordinate(row, 1);
    c = new Coordinate(row, 2);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenDiagonalLine() throws Exception {
    Coordinate a, b, c;

    a = new Coordinate(1, 1);
    b = new Coordinate(2, 2);
    c = new Coordinate(3, 3);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenDiagonalLineNegativePoints() throws Exception {
    Coordinate a, b, c;

    a = new Coordinate(-1, -1);
    b = new Coordinate(-2, -2);
    c = new Coordinate(-3, -3);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetweenDiagonalLineThroughOrigin() throws Exception {
    Coordinate a, b, c;

    a = new Coordinate(-1, -1);
    b = new Coordinate( 0,  0);
    c = new Coordinate( 1,  1);

    checkPointsInLine(a, b, c);
  }

  @Test
  public void isBetwenNonCollinearPoints() throws Exception {
    Coordinate a, b, c;

    a = new Coordinate(1, 1);
    b = new Coordinate(1, 2);
    c = new Coordinate(2, 2);

    // Invalid conditions
    final boolean abc = b.isBetween(a, c);
    final boolean cba = b.isBetween(c, a);
    final boolean acb = c.isBetween(a, b);
    final boolean bca = c.isBetween(b, a);
    final boolean bac = a.isBetween(b, c);
    final boolean cab = a.isBetween(c, b);

    assertEquals("Reversing parameters is inconsistent", abc, cba);
    assertFalse("Incorrect geometry", abc);

    assertEquals("Reversing parameters is inconsistent", acb, bca);
    assertFalse("Incorrect geometry", acb);

    assertEquals("Reversing parameters is inconsistent", bac, cab);
    assertFalse("Incorrect geometry", bac);
  }

  private void checkPointsInLine(final Coordinate a, final Coordinate b, final Coordinate c) {
    // Valid conditions
    final boolean abc = b.isBetween(a, c);
    final boolean cba = b.isBetween(c, a);

    // Invalid conditions
    final boolean acb = c.isBetween(a, b);
    final boolean bca = c.isBetween(b, a);
    final boolean bac = a.isBetween(b, c);
    final boolean cab = a.isBetween(c, b);

    assertEquals("Reversing parameters is inconsistent", abc, cba);
    assertTrue("Incorrect geometry", abc);

    assertEquals("Reversing parameters is inconsistent", acb, bca);
    assertFalse("Incorrect geometry", acb);

    assertEquals("Reversing parameters is inconsistent", bac, cab);
    assertFalse("Incorrect geometry", bac);
  }
}
