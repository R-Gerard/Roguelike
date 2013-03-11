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
import com.callidusrobotics.locale.DungeonLevel;

/**
 * Interface defining objects able to respond to Commands.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Command
 */
public interface Actor {
  /**
   * Faction accessor
   *
   * @return The faction that this is a member of
   * @see Relationship
   */
  String getFaction();

  /**
   * Method to request this to act upon a Command.
   *
   * @param currentLevel
   *          The dungeonLevel this occupies, not null
   * @param command
   *          The command that should be acted upon, not null
   * @return The action executed by this, never null
   */
  Message processCommand(DungeonLevel currentLevel, Command command);
}
