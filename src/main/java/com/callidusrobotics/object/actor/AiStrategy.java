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
import com.callidusrobotics.locale.DungeonLevel;

/**
 * Interface defining the behavior of a NonPlayerCharacter. Classes that
 * implement this interface must be annotated with {@link Behaves}.
 * <p>
 * Each derived instance of AiStrategy is designed to be stored in a collection
 * owned by a NonPlayerCharacter. The NonPlayerCharacter allows each AiStrategy
 * instance to vote and self-elect the instance that should control the
 * NonPlayerCharacter every time the NonPlayerCharacter may act.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Behaves
 * @see Actor#processCommand(DungeonLevel, com.callidusrobotics.command.Command)
 */
public interface AiStrategy {
  /**
   * A NonPlayerCharacter may act once its speed counter matches or exceeds the
   * SPEED_QUANTUM, which should be incremented once each time
   * {@linkplain Actor#processCommand(DungeonLevel, com.callidusrobotics.command.Command)}
   * is called. The speed counter can be incremented by any positive value &lt;=
   * SPEED_QUANTUM, but typically the value will be from {@link StatBlock#speed}.
   *
   * @see AbstractActor#statBlock
   * @see StatBlock#speed
   */
  int SPEED_QUANTUM = 100;

  Behavior getBehavior();

  /**
   * Resets the internal state of this.
   */
  void init();

  /**
   * Method to rank the usefulness of this strategy compared to other strategies
   * the NonPlayerCharacter may use.
   *
   * @param factionData
   *          The current relationships between Actor factions
   * @param currentLevel
   *          The dungeonLevel this occupies, not null
   * @return The usefulness of this strategy given current conditions
   */
  int getWeight(ActorFactionData factionData, DungeonLevel currentLevel);

  /**
   * Delegate method to control the NonPlayerCharacter.
   *
   * @param factionData
   *          The current relationships between Actor factions
   * @param currentLevel
   *          The dungeonLevel this occupies, not null
   * @return The action executed by this, never null
   * @see Actor#processCommand(DungeonLevel, com.callidusrobotics.command.Command)
   */
  // TODO: Return List<Message> in case actor's speed is greater than the player's
  Message updateState(ActorFactionData factionData, DungeonLevel currentLevel);
}
