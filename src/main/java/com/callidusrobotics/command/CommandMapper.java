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

import java.awt.AWTKeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.KeyStroke;

import com.callidusrobotics.util.NotInstantiableError;

/**
 * Singleton class for managing user input to <code>Command</code> mappings.
 *
 * @author Rusty
 * @since 0.0.1
 */
public final class CommandMapper {
  private static final Map<Integer, Command> COMMAND_MAP = new ConcurrentHashMap<Integer, Command>();
  private static final Map<String, Command> COMMAND_STRINGS = new ConcurrentHashMap<String, Command>();

  private static final int MODIFIER_MASK =
      InputEvent.SHIFT_MASK |
      InputEvent.CTRL_MASK |
      InputEvent.META_MASK |
      InputEvent.ALT_MASK |
      InputEvent.ALT_GRAPH_MASK |
      InputEvent.BUTTON1_MASK |
      InputEvent.BUTTON1_MASK |
      InputEvent.BUTTON3_MASK;

  /**
   * Commands that do not cause time to elapse when they are performed.
   */
  public static final Set<Command> NO_UPDATE_CMD_SET;

  static {
    final Set<Command> noUpdateCommands = new HashSet<Command>(Arrays.asList(
        Command.UNKNOWN,
        Command.INVENTORY,
        Command.SELECT,
        Command.EXAMINE,
        Command.TOGGLE,
        Command.HELP,
        Command.QUIT
        ));
    NO_UPDATE_CMD_SET = Collections.unmodifiableSet(noUpdateCommands);

    // Populate the command map with default keybindings
    setCommand("KP_UP", Command.NORTH);
    setCommand("UP", Command.NORTH);
    setCommand("8", Command.NORTH);

    setCommand("9", Command.NORTHEAST);

    setCommand("KP_RIGHT", Command.EAST);
    setCommand("RIGHT", Command.EAST);
    setCommand("6", Command.EAST);

    setCommand("3", Command.SOUTHEAST);

    setCommand("KP_DOWN", Command.SOUTH);
    setCommand("DOWN", Command.SOUTH);
    setCommand("2", Command.SOUTH);

    setCommand("1", Command.SOUTHWEST);

    setCommand("KP_LEFT", Command.WEST);
    setCommand("LEFT", Command.WEST);
    setCommand("4", Command.WEST);

    setCommand("7", Command.NORTHWEST);

    setCommand("DECIMAL", Command.REST);
    setCommand("PERIOD", Command.REST);
    setCommand("5", Command.REST);

    setCommand("LESS", Command.ASCEND);
    setCommand("SHIFT COMMA", Command.ASCEND);
    setCommand("GREATER", Command.DESCEND);
    setCommand("SHIFT PERIOD", Command.DESCEND);

    setCommand("F", Command.FIRE);
    setCommand("G", Command.GRAB_PROMPT);
    setCommand("SHIFT G", Command.GRABALL);
    setCommand("D", Command.DROP_PROMPT);
    setCommand("SHIFT D", Command.DROPALL);
    setCommand("I", Command.INVENTORY);
    setCommand("U", Command.USE);
    setCommand("E", Command.EQUIP);
    setCommand("R", Command.UNEQUIP);
    setCommand("SHIFT R", Command.UNLOAD);
    setCommand("L", Command.RELOAD);

    setCommand("X", Command.EXAMINE);
    setCommand("C", Command.CHAT);

    setCommand("ENTER", Command.SELECT);
    setCommand("TAB", Command.TOGGLE);
    setCommand("SPACE", Command.TOGGLE);
    setCommand("ESCAPE", Command.ESCAPE);
    setCommand("SHIFT SLASH", Command.HELP);
    setCommand("SHIFT Q", Command.QUIT);
  }

  private CommandMapper() {
    throw new NotInstantiableError();
  }

  /**
   * Retrieves the <code>Command</code> that corresponds with the keystroke.
   *
   * @param input
   *          The user input, not null
   * @return The command, never null
   */
  public static Command getCommand(final KeyEvent input) {
    final Command command = COMMAND_MAP.get(virtualKey2HashCode(input.getKeyCode(), input.getModifiers()));

    if (command == null) {
      return Command.UNKNOWN;
    }

    return command;
  }

  /**
   * Maps a keystroke to a <code>Command</code>.
   * <p>
   * Examples:
   * <ul>
   * <li>["G",&emsp;GRAB_PROMPT] - lowercase 'g' issues the GRAB_PROMPT command</li>
   * <li>["SHIFT G",&emsp;GRABALL] - uppercase 'G' issues the GRABALL command</li>
   * </ul>
   *
   * @param input
   *          The string representing a keystroke, not null
   * @param command
   *          The command to map to the keystroke, not null
   * @see KeyStroke#getKeyStroke(String)
   */
  public static void setCommand(final String input, final Command command) {
    // Record the original input string into the auxiliary map
    COMMAND_STRINGS.put(input, command);

    // Modifier keywords must be lowercase
    String commandString = input;
    commandString = commandString.replaceAll("SHIFT", "shift");
    commandString = commandString.replaceAll("CONTROL", "control");
    commandString = commandString.replaceAll("CTRL", "ctrl");
    commandString = commandString.replaceAll("META", "meta");
    commandString = commandString.replaceAll("ALTGRAPH", "altGraph");
    commandString = commandString.replaceAll("ALT", "alt");
    commandString = commandString.replaceAll("BUTTON", "button");

    // Convert the corrected input string into a KeyEvent code
    try {
      final AWTKeyStroke keyStroke = AWTKeyStroke.getAWTKeyStroke(commandString);

      COMMAND_MAP.put(virtualKey2HashCode(keyStroke.getKeyCode(), keyStroke.getModifiers()), command);
    } catch (final IllegalArgumentException e) {
      // Don't store invalid input strings
      COMMAND_STRINGS.remove(input);

      throw e;
    }
  }

  /**
   * Command map accessor.
   *
   * @return The [keystroke, command] pairs, never null
   */
  public static Map<String, Command> getCommandMap() {
    return Collections.unmodifiableMap(COMMAND_STRINGS);
  }

  private static int virtualKey2HashCode(final int keyCode, final int modifiers) {
    return (((modifiers & MODIFIER_MASK) << 16) | (keyCode & 0xFFFF));
  }
}
