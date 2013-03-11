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

package com.callidusrobotics.util;

import java.util.LinkedHashSet;
import java.util.Set;

import com.callidusrobotics.locale.Coordinate;

@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops", "PMD.NPathComplexity" })
public final class RasterGeometry {
  private RasterGeometry() {
    throw new NotInstantiableError();
  }

  public static Set<Coordinate> getLine(final Coordinate pointA, final Coordinate pointB) {
    final Set<Coordinate> points = new LinkedHashSet<Coordinate>();

    final int deltaC = Math.abs(pointB.getCol() - pointA.getCol());
    final int colStep = pointA.getCol() < pointB.getCol() ? 1 : -1;
    final int deltaR = Math.abs(pointB.getRow() - pointA.getRow());
    final int rowStep = pointA.getRow() < pointB.getRow() ? 1 : -1;
    int err = (deltaC > deltaR ? deltaC : -deltaR) / 2;

    int row = pointA.getRow();
    int col = pointA.getCol();

    points.add(pointA);

    while (row != pointB.getRow() || col != pointB.getCol()) {
      points.add(new Coordinate(row, col));

      final int errorDelta = err;
      if (errorDelta > -deltaC) {
        err -= deltaR;
        col += colStep;
      }

      if (errorDelta < deltaR) {
        err += deltaC;
        row += rowStep;
      }
    }

    points.add(pointB);

    return points;
  }

  public static Set<Coordinate> getCircle(final int radius, final boolean fill) {
    return getCircle(new Coordinate(radius, radius), radius, fill);
  }

  public static Set<Coordinate> getCircle(final Coordinate center, final int radius, final boolean fill) {
    final Set<Coordinate> points = new LinkedHashSet<Coordinate>();

    // Generate a list of all discrete points that fall inside the circle
    if (fill) {
      final int rad2 = radius * radius;

      for (int r = center.getRow() - radius; r < center.getRow() + radius; r++) {
        for (int c = center.getCol() - radius; c < center.getCol() + radius; c++) {
          final int vDelta = (r - center.getRow());
          final int hDelta = (c - center.getCol());
          final int dist2 = vDelta * vDelta + hDelta * hDelta;

          if (dist2 < rad2) {
            points.add(new Coordinate(r, c));
          }
        }
      }
    }

    final int centerRow = center.getRow();
    final int centerCol = center.getCol();

    // Bresenham's circle algorithm
    int hDelta = 0;
    int vDelta = radius;
    int err = 3 - 2 * radius;
    while (vDelta >= hDelta) {
      points.add(new Coordinate(centerRow - hDelta, centerCol - vDelta));
      points.add(new Coordinate(centerRow - vDelta, centerCol - hDelta));
      points.add(new Coordinate(centerRow + vDelta, centerCol - hDelta));
      points.add(new Coordinate(centerRow + hDelta, centerCol - vDelta));
      points.add(new Coordinate(centerRow - hDelta, centerCol + vDelta));
      points.add(new Coordinate(centerRow - vDelta, centerCol + hDelta));
      points.add(new Coordinate(centerRow + vDelta, centerCol + hDelta));
      points.add(new Coordinate(centerRow + hDelta, centerCol + vDelta));

      if (err < 0) {
        err += 4 * hDelta + 6;
        hDelta++;
      } else {
        err += 4 * (hDelta - vDelta) + 10;
        hDelta++;
        vDelta--;
      }
    }

    return points;
  }

  public static Set<Coordinate> getRectangle(final int height, final int width, final boolean fill) {
    return getRectangle(new Coordinate(0, 0), new Coordinate(height, width), fill);
  }

  public static Set<Coordinate> getRectangle(final Coordinate topLeft, final Coordinate bottomRight, final boolean fill) {
    final Set<Coordinate> points = new LinkedHashSet<Coordinate>();

    final Coordinate topRight = new Coordinate(topLeft.getRow(), bottomRight.getCol());
    final Coordinate bottomLeft = new Coordinate(bottomRight.getRow(), topLeft.getCol());

    points.addAll(getLine(topLeft, topRight));
    points.addAll(getLine(topRight, bottomRight));
    points.addAll(getLine(bottomRight, bottomLeft));
    points.addAll(getLine(bottomLeft, topLeft));

    if (fill) {
      for (int r = topLeft.getRow() + 1; r < bottomRight.getRow(); r++) {
        for (int c = topLeft.getCol() + 1; c < bottomRight.getCol(); c++) {
          points.add(new Coordinate(r, c));
        }
      }
    }

    return points;
  }
}
