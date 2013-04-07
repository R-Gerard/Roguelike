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

package com.callidusrobotics.object.actor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.MutableConsoleGraphic;
import com.callidusrobotics.util.RasterGeometry;
import com.callidusrobotics.util.TrueColor;

/**
 * Implementation of AbstractReticle used by weapons that affect a single Tile.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class TargetingReticle extends AbstractReticle {
  protected final int range2;

  public TargetingReticle(final DungeonLevel dungeonLevel, final Coordinate startPosition, final int range, final Size size) {
    super(dungeonLevel, startPosition);

    this.range2 = range * range;
    this.size = size;

    // TODO: Add constructor arg for last targetPosition
    if (!dungeonLevel.getNonPlayerCharacters().isEmpty()) {
      final Coordinate targetPosition = selectTarget(dungeonLevel, targetIndex);
      setPosition(targetPosition);
    }
  }

  @Override
  public Message processCommand(final DungeonLevel currentLevel, final Command command) {
    super.processCommand(currentLevel, command);

    if ((command == Command.SELECT || command == Command.FIRE) && currentLevel.getAbstractActorAtPosition(position) != null && !position.equals(startPosition) && isInLineOfSight()) {
      return new Message(Command.ATTACKRANGED, Arrays.asList((Coordinate) position), null, null, null);
    }

    final int count = getNumVisibleTargets(currentLevel);
    if (command == Command.TOGGLE && count > 0) {
      targetIndex = (targetIndex + 1) % count;
      final Coordinate targetPosition = selectTarget(currentLevel, targetIndex);
      setPosition(targetPosition);
    }

    // Return UNKNOWN because moving the reticle should not update the game state
    return new Message(Command.UNKNOWN, null, null, null, null);
  }

  @Override
  public void draw(final Console console) {
    clear(console);

    if (visible) {
      drawLineOfSight(console);

      console.showCursor();
      console.setCursor(getRow(), getCol(), consoleGraphic);
    } else {
      console.hideCursor();
    }
  }

  private void clear(final Console console) {
    final Set<Coordinate> line = RasterGeometry.getLine(startPosition, getLastPosition());
    line.remove(startPosition);
    line.remove(getLastPosition());
    for (final Coordinate point : line) {
      final MutableConsoleGraphic consoleGraphic = getConsoleGraphic(point);
      console.print(point.getRow(), point.getCol(), consoleGraphic);
    }
  }

  public boolean isInLineOfSight() {
    final Set<Coordinate> visiblePoints = computeLineOfSight();
    return visiblePoints.contains(position);
  }

  private void drawLineOfSight(final Console console) {
    final Set<Coordinate> visiblePoints = computeLineOfSight();
    final Set<Coordinate> line = RasterGeometry.getLine(startPosition, getPosition());
    line.remove(startPosition);
    line.remove(position);

    for (final Coordinate point : line) {
      final boolean inLineOfSight = visiblePoints.contains(point);
      final MutableConsoleGraphic tempGraphic = getConsoleGraphic(point);

      if (inLineOfSight) {
        tempGraphic.setForeground(TrueColor.YELLOW);
        tempGraphic.setBackground(TrueColor.YELLOW.darker());
      } else {
        tempGraphic.setForeground(TrueColor.RED);
        tempGraphic.setBackground(TrueColor.RED.darker());
      }

      console.print(point.getRow(), point.getCol(), tempGraphic);
    }

    if (visiblePoints.contains(position)) {
      consoleGraphic.setForeground(TrueColor.YELLOW);
    } else {
      consoleGraphic.setForeground(TrueColor.RED);
    }
  }

  private Set<Coordinate> computeLineOfSight() {
    final Set<Coordinate> line = RasterGeometry.getLine(startPosition, getPosition());
    final Set<Coordinate> visibilePoints = new LinkedHashSet<Coordinate>();
    line.remove(startPosition);
    line.remove(position);
    boolean inLineOfSight = true;

    // Follow the line marking each point as in sight if it is within range and nothing bigger than the shooter occupies the space
    for (final Coordinate point : line) {
      inLineOfSight = inLineOfSight && startPosition.distance2(point) <= range2;
      inLineOfSight = inLineOfSight && !dungeonLevel.getTileRelative(point).isBarrier();

      if (inLineOfSight) {
        visibilePoints.add(point);

        // The shooter can shoot over the top of anything smaller than it
        // We could perform ray tracing in 3D, but gaps in the line of sight (smaller objects behind larger objects) would be confusing to the user in a 2D display
        final AbstractActor actor = dungeonLevel.getAbstractActorAtPosition(point);
        if (actor != null && actor.getSize().ordinal() >= size.ordinal()) {
          inLineOfSight = false;
        }
      }
    }

    // Nothing along the line blocks LoS
    if (inLineOfSight && startPosition.distance2(position) <= range2) {
      visibilePoints.add(position);
    }

    return visibilePoints;
  }

  private MutableConsoleGraphic getConsoleGraphic(final Coordinate point) {
    if (!dungeonLevel.getTileRelative(point).isVisible()) {
      return Tile.makeDefaultInvisibleBarrier().getConsoleGraphic();
    }

    final AbstractActor actor = dungeonLevel.getAbstractActorAtPosition(point);
    if (actor == null || actor.getCurrentStatBlock().getCurrentHp() < 1) {
      return dungeonLevel.getTileRelative(point).getConsoleGraphic();
    } else {
      return new MutableConsoleGraphic(actor.getConsoleGraphic());
    }
  }

  protected int getNumVisibleTargets(final DungeonLevel currentLevel) {
    int count = 0;
    for (final AbstractActor actor : currentLevel.getNonPlayerCharacters()) {
      if (currentLevel.getTileRelative(actor.getPosition()).isVisible()) {
        count++;
      }
    }

    return count;
  }

  @Override
  protected boolean isBarrier(final DungeonLevel currentLevel, final Coordinate position) {
    if (!currentLevel.checkCoordinatesRelative(position)) {
      return true;
    }

    // We want the reticle to be able to step over visible barriers
    return !currentLevel.getTileRelative(position).isVisible();
  }
}
