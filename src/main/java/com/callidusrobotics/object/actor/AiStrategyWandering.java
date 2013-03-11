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

import com.callidusrobotics.Message;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;

/**
 * Defines behavior for a NonPlayerCharacter that wanders randomly.
 *
 * @author Rusty
 * @since 0.0.1
 */
@Behaves(Behavior.WANDERING)
class AiStrategyWandering extends AbstractAiStrategy {
  private Command direction;
  private int steps = 0;

  AiStrategyWandering() {
    super();
    init();
  }

  @Override
  public final void init() {
    super.init();
    steps = 0;
    direction = Direction.COMPASS_POINTS.get(random.nextInt(Direction.COMPASS_POINTS.size())).toCommand();
  }

  @Override
  public Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    final Message message = self.processCommand(currentLevel, direction);
    if (message.getAction() == Command.REST || steps++ > random.nextInt(10)) {
      init();
    }

    return message;
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    return base;
  }
}
