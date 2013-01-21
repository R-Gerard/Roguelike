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

package com.callidusrobotics.swing;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.callidusrobotics.attribute.Dimensional;

/**
 * Interface defining standard Console I/O.
 *
 * @author Rusty Gerard
 * @since 0.0.1
 */
public interface Console extends Dimensional {
  /**
   * Sets the string to be displayed in the window's border.
   *
   * @param title
   *          The string to be displayed
   */
  void setTitle(String title);

  /**
   * Wait until the keyboard is pressed and return the <code>KeyEvent</code>
   * corresponding to the keystroke. Does not echo the input to the console.
   *
   * @return The key pressed
   */
  KeyEvent getKeyPress();

  /**
   * Interactive keyboard listener. Echoes keyboard input to the console until
   * the user presses the ENTER key, then returns the result. The current string
   * is echoed at the location of the console cursor and advances until the
   * string reaches the maximum specified length.
   *
   * @param maxLen
   *          The maximum string length
   * @param foreground
   *          The font foreground color of the string
   * @param background
   *          The font background color of the string
   * @return The String
   * @see #setCursor(int, int, ConsoleGraphic)
   * @see #moveCursor(int, int)
   * @throws ArrayIndexOutOfBoundsException
   *           if the cursor falls outside the bounds of this
   */
  String getline(int maxLen, Color foreground, Color background);

  /**
   * Displays the <code>ConsoleGraphic</code> object at the specified position.
   * Does not take effect until the next call to {@link #render()}.
   *
   * @param row
   *          The vertical position of the graphic
   * @param col
   *          The horizontal position of the graphic
   * @param consoleGraphic
   *          The object to display
   * @see #render()
   * @see ConsoleGraphic
   * @throws ArrayIndexOutOfBoundsException
   *           if the row or col are outside the bounds of this
   */
  void print(int row, int col, ConsoleGraphic consoleGraphic);

  /**
   * Prints a character at the specified position. Does not take effect until
   * the next call to {@link #render()}.
   *
   * @param row
   *          The vertical position of the character to print
   * @param col
   *          The horizontal position of the character to print
   * @param character
   *          The character to print
   * @param foreground
   *          The font foreground color of the character
   * @param background
   *          The font background color of the character
   *          <p>
   *          A <code>null</code> value indicates transparency
   * @see #render()
   * @throws ArrayIndexOutOfBoundsException
   *           if the row or col are outside the bounds of this
   */
  void print(int row, int col, char character, Color foreground, Color background);

  /**
   * Prints a string at the specified position. Does not take effect until the
   * next call to {@link #render()}.
   *
   * @param row
   *          The vertical position of the string
   * @param col
   *          The horizontal position of the string
   * @param string
   *          The string to print
   * @param foreground
   *          The font foreground color of the character
   * @param background
   *          The font background color of the character
   *          <p>
   *          A <code>null</code> value indicates transparency
   * @see #render()
   * @throws ArrayIndexOutOfBoundsException
   *           if the row or col are outside the bounds of this
   */
  void print(int row, int col, String string, Color foreground, Color background);

  /**
   * Makes the console's cursor visible. Does not take effect until the next
   * call to {@link #render()}.
   *
   * @see #setCursor(int, int, ConsoleGraphic)
   * @see #render()
   */
  void showCursor();

  /**
   * Makes the console's cursor invisible. Does not take effect until the next
   * call to {@link #render()}.
   *
   * @see #showCursor()
   * @see #setCursor(int, int, ConsoleGraphic)
   * @see #render()
   */
  void hideCursor();

  /**
   * Moves the cursor's position. Does not take effect until the next call to
   * {@link #render()}.
   *
   * @param row
   *          The vertical position of the cursor
   * @param col
   *          The horizontal position of the cursor
   * @see #showCursor()
   * @see #setCursor(int, int, ConsoleGraphic)
   * @see #render()
   * @throws ArrayIndexOutOfBoundsException
   *           if the row or col are outside the bounds of this
   */
  void moveCursor(int row, int col);

  /**
   * Creates a new cursor object at the specified position and appearance. The
   * cursor is not displayed unless {@link #showCursor()} has been called. Does
   * not take effect until the next call to {@link #render()}.
   *
   * @param row
   *          The vertical position of the cursor
   * @param col
   *          The horizontal position of the cursor
   * @param cursor
   *          The cursor graphic to display
   * @see #showCursor()
   * @see #render()
   * @throws ArrayIndexOutOfBoundsException
   *           if the row or col are outside the bounds of this
   */
  void setCursor(int row, int col, ConsoleGraphic cursor);

  /**
   * Sets the animation speed of the console (e.g. blinking cursors).
   *
   * @param delay
   *          The delay in milliseconds between animation changes
   */
  void setAnimationSpeed(int delay);

  /**
   * Returns the current animation speed of the console (the delay between
   * animation changes in milliseconds).
   *
   * @return The delay in milliseconds
   */
  int getAnimationSpeed();

  /**
   * Clears the console window. Does not take effect until the next call to
   * {@link #render()}.
   */
  void clear();

  /**
   * Renders the console.
   */
  void render();

  /**
   * Closes the console window.
   */
  void exit();
}
