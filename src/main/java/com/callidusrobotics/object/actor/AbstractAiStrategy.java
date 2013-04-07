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

import java.util.List;
import java.util.Map;
import java.util.Random;
import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;

abstract class AbstractAiStrategy implements AiStrategy {
  protected NonPlayerCharacter self;
  protected int min, max, base, multiplier, speedCounter;
  protected final Random random = new Random();

  AbstractAiStrategy() {
    min = max = base = multiplier = speedCounter = 0;
    speedCounter = random.nextInt(SPEED_QUANTUM);
  }

  @Override
  public Behavior getBehavior() {
    return getClass().getAnnotation(Behaves.class).value();
  }

  @Override
  public void init() {
    speedCounter = random.nextInt(SPEED_QUANTUM);
  }

  @Override
  public int getWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    final int weight = getAbsoluteWeight(factionData, currentLevel);

    if (weight < min) {
      return min;
    }

    if (weight > max) {
      return max;
    }

    return weight;
  }

  @Override
  public Message updateState(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    speedCounter += self.getCurrentStatBlock().getSpeed();
    if (speedCounter >= SPEED_QUANTUM) {
      speedCounter -= SPEED_QUANTUM;
      return filterMessage(updateStateDelegate(factionData, currentLevel), factionData, currentLevel);
    }

    return new Message(Command.REST, null, null, null, null);
  }

  protected Message filterMessage(final Message message, final ActorFactionData factionData, final DungeonLevel currentLevel) {
    // Display the message to the player if and only if the Actor is visible to the player
    if (currentLevel.getTileRelative(self.getPosition()).isVisible()) {
      return message;
    }

    // Return the bare minimum information to the GameMediator
    return new Message(message.getAction(), message.getTargets(), null, null, null);
  }

  protected abstract Message updateStateDelegate(ActorFactionData factionData, DungeonLevel currentLevel);

  protected abstract int getAbsoluteWeight(ActorFactionData factionData, DungeonLevel currentLevel);

  public void setSelf(final NonPlayerCharacter self) {
    this.self = self;
  }

  public void setMin(final int min) {
    this.min = min;
  }

  public void setMax(final int max) {
    this.max = max;
  }

  public void setBase(final int base) {
    this.base = base;
  }

  public void setMultiplier(final int multiplier) {
    this.multiplier = multiplier;
  }

  public void setSpeedCounter(final int speedCounter) {
    this.speedCounter = speedCounter;
  }

  protected AbstractActor getNearestOpponent(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    //final Set<Coordinate> illuminatedPoints = currentLevel.getFovStrategy().getIlluminated(self, currentLevel);
    final Map<Integer, List<AbstractActor>> actors = currentLevel.getAbstractActorsRankedByDistance(self.getPosition(), false);
    actors.remove(0);

    for (final int distance : actors.keySet()) {
      final List<AbstractActor> actorList = actors.get(distance);
      for (final AbstractActor actor : actorList) {
        // TODO: Make NPCs less aware and indicate to the player what their awareness level is
        //final boolean awareOfActor = illuminatedPoints.contains(actor.getPosition()) || self.getKnownEntities().contains(actor);

        if (factionData.areEnemies(self.getFaction(), actor.getFaction())) {
          return actor;
        }
      }
    }

    return null;
  }

  protected Command getDirection(final Coordinate position, final DungeonLevel currentLevel) {
    Command direction = Command.REST;
    int minDist = self.getPosition().distance2(position);

    // Look for an empty adjacent tile that is closer to the desired position
    for (final Direction tempDir : Direction.COMPASS_POINTS) {
      final int newDistance = getDistance(position, currentLevel, tempDir);

      if (newDistance < minDist) {
        minDist = newDistance;
        direction = tempDir.toCommand();
      }
    }

    return direction;
  }

  private int getDistance(final Coordinate position, final DungeonLevel currentLevel, final Direction desiredDirection) {
    if (self.canMove(currentLevel, desiredDirection)) {
      return desiredDirection.toCoordinate().increment(self.getPosition()).distance2(position);
    }

    return Integer.MAX_VALUE;
  }

  protected int getPercentHpRemaining() {
    final StatBlock statBlock = self.getCurrentStatBlock();
    final int currentHp = Math.max(1, statBlock.getCurrentHp());
    final int maxHp = statBlock.getMaxHp();

    return 100 * currentHp / maxHp;
  }
}
