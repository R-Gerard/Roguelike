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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.Unicode;

public abstract class AbstractReticle extends AbstractActor {

  protected final Coordinate startPosition;
  protected final DungeonLevel dungeonLevel;
  protected boolean visible = true;
  protected int targetIndex = 0;

  protected AbstractReticle(final DungeonLevel dungeonLevel, final Coordinate startPosition) {
    super(new ConsoleGraphic(Unicode.SYMBOL_MISC_INTERSECTION_FOUR_CORNERS, TrueColor.YELLOW, null), startPosition, "Reticle", "Reticle");

    this.dungeonLevel = dungeonLevel;
    this.startPosition = startPosition;
    this.inventory = Collections.emptyList();

    Validate.notNull(dungeonLevel);
    Validate.notNull(startPosition);
  }

  final Coordinate selectTarget(final DungeonLevel currentLevel, final int targetIndex) {
    final Map<Integer, List<AbstractActor>> actors = currentLevel.getAbstractActorsRankedByDistance(startPosition, true);
    actors.remove(0);
    int index = 0;
    for (final int distance : actors.keySet()) {
      final List<AbstractActor> actorList = actors.get(distance);
      for (final AbstractActor actor : actorList) {
        if (index++ == targetIndex) {
          return actor.getPosition();
        }
      }
    }

    return startPosition;
  }

  // This seems to be a bug in PMD
  @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
  @Override
  public boolean canAddItemToInventory(final Inventoriable item) {
    return false;
  }

  @Override
  public void draw(final Console console) {
    if (visible) {
      console.showCursor();
      console.setCursor(getRow(), getCol(), consoleGraphic);
    } else {
      console.hideCursor();
    }
  }

  public boolean isVisible() {
    return visible;
  }

  public void hide() {
    visible = false;

    // Clear the last position
    setPosition(position);
  }

  public void show() {
    visible = true;
  }
}
