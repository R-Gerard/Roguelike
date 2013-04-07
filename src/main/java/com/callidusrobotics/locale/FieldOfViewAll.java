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

import java.util.HashSet;
import java.util.Set;

import com.callidusrobotics.object.actor.AbstractActor;

@Illuminates(FieldOfViewType.ALL)
class FieldOfViewAll extends AbstractFieldOfViewStrategy {
  @Override
  public void playerExitLevel() {
    playerEnterRoom = true;
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  @Override
  public Set<Coordinate> getIlluminated(final AbstractActor actor, final DungeonLevel currentLevel) {
    final Coordinate topLeftCorner = currentLevel.getPosition();
    final Set<Coordinate> result = new HashSet<Coordinate>();

    for (int r = 0; r < currentLevel.getHeight(); r++) {
      for (int c = 0; c < currentLevel.getWidth(); c++) {
        result.add(new Coordinate(topLeftCorner.getRow() + r, topLeftCorner.getCol() + c));
      }
    }

    return result;
  }
}
