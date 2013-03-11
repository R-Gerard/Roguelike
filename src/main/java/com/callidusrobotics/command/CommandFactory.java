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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.reflections.Reflections;

import com.callidusrobotics.GameMediator;
import com.callidusrobotics.Message;
import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.Identifier;

/**
 * Generates a mapping of <code>Command</code> to <code>CommandPerformer</code>
 * objects by scanning for classes with the <code>Performs</code> annotation.
 * Assumes implementations of <code>CommandPerformer</code> are derived from
 * <code>AbstractCommandPerformer</code>.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Command
 * @see CommandMapper
 * @see CommandPerformer
 * @see AbstractCommandPerformer
 * @see Performs
 * @see Message
 */
public class CommandFactory {
  private static final Set<Class<?>> PERFORMERS;

  static {
    final Reflections reflections = Identifier.isDebugMode() ? new Reflections(CommandFactory.class.getPackage()) : Reflections.collect();
    PERFORMERS = reflections.getTypesAnnotatedWith(Performs.class);
  }

  private final Map<Command, CommandPerformer> commandPerformers = new HashMap<Command, CommandPerformer>();

  public CommandFactory(final GameMediator gameMediator) {
    Validate.notNull(gameMediator);

    for (final Class<?> clazz : PERFORMERS) {
      Object instance = null;
      try {
        instance = clazz.newInstance();
      } catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }

      if (instance instanceof AbstractCommandPerformer) {
        final AbstractCommandPerformer base = (AbstractCommandPerformer) instance;
        final CommandPerformer performer = (CommandPerformer) instance;

        base.setGameMediator(gameMediator);
        for (final Command command : performer.getClass().getAnnotation(Performs.class).value()) {
          if (commandPerformers.containsKey(command)) {
            throw new DuplicateKeyException("Command '" + command + "' is registered by more than one " + CommandPerformer.class.getSimpleName() + ".");
          }

          commandPerformers.put(command, performer);
        }
      } else {
        throw new UnsupportedOperationException("Class '" + clazz.getSimpleName() + "' does not extend " + AbstractCommandPerformer.class.getSimpleName() + ".");
      }
    }
  }

  /**
   * Delegate method to invoke the appropriate CommandPerformer.
   *
   * @param command
   *          The Command to perform, not null
   * @return Message the result of the attempt to perform the Command, never
   *         null
   * @see Command
   * @see CommandPerformer
   */
  public Message performCommand(final Command command) {
    Validate.notNull(command);

    if (commandPerformers.containsKey(command)) {
      return commandPerformers.get(command).perform(command);
    }

    if (Identifier.isDebugMode()) {
      throw new UnsupportedOperationException(CommandPerformer.class.getSimpleName() + " for command " + command + " is not implemented.");
    } else {
      return new Message(Command.UNKNOWN, null, null, null, null);
    }
  }
}
