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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.callidusrobotics.object.actor.PlayerCharacter;

abstract class AbstractFieldOfViewStrategy implements FieldOfViewStrategy {
  protected DungeonLevel currentLevel;
  protected boolean playerEnterRoom = true;
  protected Set<Coordinate> lastIlluminated = new HashSet<Coordinate>();

  @Override
  public final List<Coordinate> illuminate(final PlayerCharacter player, final DungeonLevel currentLevel) {
    this.currentLevel = currentLevel;
    final Set<Coordinate> illuminated = getIlluminated(player, currentLevel);

    for (final Coordinate point : illuminated) {
      final Tile currentTile = currentLevel.getTileRelative(point);
      currentTile.setVisible(true);
      currentTile.setExplored(true);
    }

    if (playerEnterRoom) {
      playerEnterRoom = false;
      lastIlluminated.clear();

      final List<Coordinate> result = new ArrayList<Coordinate>(illuminated.size());
      result.addAll(illuminated);
      return result;
    }

    final Set<Coordinate> union = union(lastIlluminated, illuminated);
    final Set<Coordinate> intersection = intersection(lastIlluminated, illuminated);

    // Compute the list of tiles that have changed (to-be dimmed or newly illuminated)
    final List<Coordinate> result = new ArrayList<Coordinate>(union.size() - intersection.size());
    union.removeAll(intersection);
    result.addAll(union);

    // Dim any tiles that are no longer illuminated
    lastIlluminated.removeAll(illuminated);
    for (final Coordinate point : lastIlluminated) {
      if (currentLevel.checkCoordinatesRelative(point)) {
        currentLevel.getTileRelative(point).setVisible(false);
      }
    }

    // Return the results so they can be drawn by the GameMediator
    lastIlluminated = illuminated;
    return result;
  }

  private Set<Coordinate> union(final Set<Coordinate> set1, final Set<Coordinate> set2) {
    final Set<Coordinate> result = new HashSet<Coordinate>(set1);
    result.addAll(set2);

    return result;
  }

  private Set<Coordinate> intersection(final Set<Coordinate> set1, final Set<Coordinate> set2) {
    final Set<Coordinate> result = new HashSet<Coordinate>(set1);
    result.retainAll(set2);

    return result;
  }
}
