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
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.callidusrobotics.util.NotInstantiableError;

/**
 * Convenience methods for loading <code>Font</code>s.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Font
 */
public final class FontFactory {
  private static final Set<String> FONT_NAMES;

  static {
    final String fontNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    FONT_NAMES = new LinkedHashSet<String>(fontNames.length);

    for (final String name : fontNames) {
      FONT_NAMES.add(name);
    }
  }

  private FontFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Checks if a <code>Font</code> exists in the environment.
   *
   * @param fontName
   *          The name of the <code>Font</code> to search for
   * @return True if the <code>Font</code> exists
   */
  public static boolean fontIsAvailable(final String fontName) {
    return FONT_NAMES.contains(fontName);
  }

  /**
   * Adds a <code>Font</code> to the environment.
   *
   * @param font
   *          The <code>Font</code> to add, not null
   * @return True if the <code>Font</code> was successfully registered
   * @see #makeTrueTypeFontResource(String, float)
   */
  public static boolean registerFont(final Font font) {
    final boolean result = GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);

    if (result) {
      FONT_NAMES.add(font.getFontName());
    }

    return result;
  }

  /**
   * Loads a <code>Font</code> resource from the classpath and instantiates it.
   *
   * @param fontName
   *          The <code>Font</code> resource name, not null
   * @param fontSize
   *          The size for the <code>Font</code>
   * @return The <code>Font</code> if it is initialized successfully, nullable
   */
  public static Font makeTrueTypeFontResource(final String fontName, final float fontSize) {
    Font font = null;

    try {
      font = Font.createFont(Font.TRUETYPE_FONT, FontFactory.class.getResourceAsStream(fontName));
      font = font.deriveFont(Font.PLAIN, fontSize);
    } catch (final FontFormatException | IOException e) {
      return null;
    }

    return font;
  }
}
