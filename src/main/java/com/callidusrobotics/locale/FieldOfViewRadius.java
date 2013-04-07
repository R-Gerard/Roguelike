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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.util.RasterGeometry;

@Illuminates(FieldOfViewType.SIMPLE_RADIUS)
class FieldOfViewRadius extends AbstractFieldOfViewStrategy {
  @Override
  public void playerExitLevel() {
    playerEnterRoom = true;

    if (currentLevel != null) {
      for (final Coordinate point : lastIlluminated) {
        currentLevel.getTileRelative(point).setVisible(false);
      }
    }
  }

  @Override
  public Set<Coordinate> getIlluminated(final AbstractActor actor, final DungeonLevel currentLevel) {
    if (actor == null) {
      return Collections.emptySet();
    }

    final int radius = actor.getCurrentStatBlock().getIntelligence() / 2 + 1;
    final Set<Coordinate> circle = RasterGeometry.getCircle(actor.getPosition(), radius, true);

    Validate.isTrue(circle.contains(actor.getPosition()));

    final Set<Coordinate> result = new HashSet<Coordinate>();
    for (final Coordinate point : circle) {
      if (currentLevel.checkCoordinatesRelative(point)) {
        result.add(point);
      }
    }

    return result;
  }
}
