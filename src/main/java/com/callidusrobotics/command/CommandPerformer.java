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

package com.callidusrobotics.command;

import com.callidusrobotics.Message;

/**
 * Interface defining performance of a Command to interact with in-game objects.
 * Classes that implement this interface should be annotated with
 * {@link Performs}.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Performs
 * @see Command
 * @see Message
 */
public interface CommandPerformer {
  /**
   * Attempt to update the game state.
   *
   * @param command
   *          The Command to perform, not null
   * @return Message describing the results of the attempt, never null
   */
  Message perform(Command command);
}
