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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.MutableCoordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;
import com.callidusrobotics.util.TrueColor;

/**
 * Defines behavior for a NonPlayerCharacter that seeks out and picks up items
 * of value.
 *
 * @author Rusty
 * @since 0.0.3
 */
@Behaves(Behavior.GREEDY)
class AiStrategyGreedy extends AbstractAiStrategy {

  @Override
  public Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    final Coordinate position = findRichestTile(currentLevel);

    // There's nothing worth going for
    if (position == null) {
      return new Message(Command.REST, null, null, null, null);
    }

    // It's at our feet
    if (position.equals(self.getPosition())) {
      return pickUpStuff(currentLevel);
    }

    // Move towards the desired Item
    final Command direction = getDirection(position, currentLevel);
    return self.processCommand(currentLevel, direction);
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    final Coordinate position = findRichestTile(currentLevel);

    if (position != null) {
      final int value = currentLevel.getTileRelative(position).getInventoryValue();

      return base + multiplier * value;
    }

    return min;
  }

  private Coordinate findRichestTile(final DungeonLevel currentLevel) {
    // If we are standing on a tile with valuables, we have found our destination!
    if (currentLevel.getTileRelative(self.getPosition()).getInventoryValue() > 0) {
      return self.getPosition();
    }

    final Coordinate topLeftCorner = currentLevel.getPosition();
    final MutableCoordinate position = new MutableCoordinate(0, 0);

    int maxRatio = 0;
    Coordinate bestPosition = null;

    // Search for the tile that has the highest value relative to our position
    for (int r = 0; r < currentLevel.getHeight(); r++) {
      for (int c = 0; c < currentLevel.getWidth(); c++) {
        position.setPosition(r, c);
        final Coordinate temp = topLeftCorner.add(position);

        // Ignore tiles that have something standing on them
        if (currentLevel.getAbstractActorAtPosition(temp) != null) {
          continue;
        }

        final Tile tile = currentLevel.getTileRelative(temp);
        final int dist2 = self.getPosition().distance2(temp);
        final int ratio = tile.getInventoryValue() / dist2;

        if (ratio > maxRatio) {
          bestPosition = temp;
          maxRatio = ratio;
        }
      }
    }

    return bestPosition;
  }

  private Message pickUpStuff(final DungeonLevel currentLevel) {
    final Tile tile = currentLevel.getTileRelative(self.getPosition());
    Validate.isTrue(tile.getInventoryValue() > 0);

    // Search for the most valuable item, ignoring any items of zero value
    int index = 0;
    int bestIndex = -1;
    int maxValue = 0;
    Inventoriable bestItem = null;
    for (final Inventoriable item : tile.getInventory()) {
      if (item.getValue() > maxValue) {
        bestIndex = index;
        maxValue = item.getValue();
        bestItem = item;
      }

      index++;
    }

    // Pick up the valuables
    Validate.notNull(bestItem);
    self.addItemToInventory(tile.removeItemFromInventory(bestIndex));

    return new Message(Command.GRABALL, null, StringUtils.capitalize(self.getNameThirdPerson()) + " picked up: " + bestItem.getName() + ".", self.getForeground(), TrueColor.BLACK);
  }

  @Override
  protected Message filterMessage(final Message message, final ActorFactionData factionData, final DungeonLevel currentLevel) {
    if (currentLevel.getTileRelative(self.getPosition()).isVisible() || message.getAction() != Command.GRABALL) {
      return message;
    }

    return new Message(Command.GRABALL, null, "You hear something rummaging.", TrueColor.GRAY, null);
  }
}
