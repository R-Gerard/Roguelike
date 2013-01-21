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

import java.awt.Font;

import com.callidusrobotics.util.NotInstantiableError;

/**
 * Singleton factory for the <code>Console</code>.
 *
 * @author Rusty
 * @since 0.0.1
 */
public final class ConsoleFactory {
  private static Console instance = null;

  private ConsoleFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Lazy singleton accessor. Builds a new <code>Console</code> instance if
   * {@link #initInstance(Font, int, int, int)} has not been called.
   *
   * @return The <code>Console</code>, never null
   * @see #initInstance(Font, int, int, int)
   */
  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized Console getInstance() {
    if (instance == null) {
      initInstance(new Font("Monospaced", Font.PLAIN, 12), 25, 80, 1000);
    }

    return instance;
  }

  /**
   * Singleton builder. Builds a new <code>Console</code> instance if and only
   * if there is currently no <code>Console</code> instance.
   *
   * @param font
   *          The font to use for displaying text, not null
   * @param rows
   *          The number of rows of text to display
   * @param columns
   *          The number of columns of text to display
   * @param animationSpeed
   *          The delay, in milliseconds, between console animations
   */
  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized void initInstance(final Font font, final int rows, final int columns, final int animationSpeed) {
    if (instance == null) {
      instance = new SwingConsole(font, rows, columns, animationSpeed);
    }
  }
}
