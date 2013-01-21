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

package com.callidusrobotics.util;

import java.awt.Color;

/**
 * Extension of {@link java.awt.Color} with additional color constants.
 *
 * @author Rusty
 * @since 0.0.1
 * @see java.awt.Color
 */
@SuppressWarnings("PMD.LongVariable")
public class TrueColor extends Color {
  private static final long serialVersionUID = -2533438709755647494L;

  public TrueColor(final int rgb) {
    super(rgb);
  }

  public TrueColor(final int rgba, final boolean hasAlpha) {
    super(rgba, hasAlpha);
  }

  public TrueColor(final int red, final int green, final int blue) {
    super(red, green, blue);
  }

  public TrueColor(final int red, final int green, final int blue, final int alpha) {
    super(red, green, blue, alpha);
  }

  @Override
  public TrueColor brighter() {
    final int red = Math.min(255, Math.max(4, getRed() * 4));
    final int green = Math.min(255, Math.max(4, getGreen() * 4));
    final int blue = Math.min(255, Math.max(4, getBlue() * 4));

    return new TrueColor(red, green, blue, getAlpha());
  }

  @Override
  public TrueColor darker() {
    return new TrueColor(getRed() / 4, getGreen() / 4, getBlue() / 4, getAlpha());
  }

  @Override
  public String toString() {
    return "0x" + String.format("%06x", 0xFFFFFF & getRGB()).toUpperCase();
  }

  // Implementation of the 147 HTML Color names
  // http://www.w3schools.com/html/html_colornames.asp
  public static final TrueColor ALICE_BLUE = new TrueColor(0xF0F8FF);
  public static final TrueColor ANTIQUE_WHITE = new TrueColor(0xFAEBD7);
  public static final TrueColor AQUA = new TrueColor(0x00FFFF);
  public static final TrueColor AQUAMARINE = new TrueColor(0x7FFFD4);
  public static final TrueColor AZURE = new TrueColor(0xF0FFFF);
  public static final TrueColor BEIGE = new TrueColor(0xF5F5DC);
  public static final TrueColor BISQUE = new TrueColor(0xFFE4C4);
  public static final TrueColor BLACK = new TrueColor(0x000000);
  public static final TrueColor BLANCHED_ALMOND = new TrueColor(0xFFEBCD);
  public static final TrueColor BLUE = new TrueColor(0x0000FF);
  public static final TrueColor BLUE_VIOLET = new TrueColor(0x8A2BE2);
  public static final TrueColor BROWN = new TrueColor(0xA52A2A);
  public static final TrueColor BURLY_WOOD = new TrueColor(0xDEB887);
  public static final TrueColor CADET_BLUE = new TrueColor(0x5F9EA0);
  public static final TrueColor CHARTREUSE = new TrueColor(0x7FFF00);
  public static final TrueColor CHOCOLATE = new TrueColor(0xD2691E);
  public static final TrueColor CORAL = new TrueColor(0xFF7F50);
  public static final TrueColor CORNFLOWER_BLUE = new TrueColor(0x6495ED);
  public static final TrueColor CORNSILK = new TrueColor(0xFFF8DC);
  public static final TrueColor CRIMSON = new TrueColor(0xDC143C);
  public static final TrueColor CYAN = new TrueColor(0x00FFFF);
  public static final TrueColor DARK_BLUE = new TrueColor(0x00008B);
  public static final TrueColor DARK_CYAN = new TrueColor(0x008B8B);
  public static final TrueColor DARK_GOLDENROD = new TrueColor(0xB8860B);
  public static final TrueColor DARK_GRAY = new TrueColor(0xA9A9A9);
  public static final TrueColor DARK_GREY = DARK_GRAY;
  public static final TrueColor DARK_GREEN = new TrueColor(0x006400);
  public static final TrueColor DARK_KHAKI = new TrueColor(0xBDB76B);
  public static final TrueColor DARK_MAGENTA = new TrueColor(0x8B008B);
  public static final TrueColor DARK_OLIVE_GREEN = new TrueColor(0x556B2F);
  public static final TrueColor DARK_ORANGE = new TrueColor(0xFF8C00);
  public static final TrueColor DARK_ORCHID = new TrueColor(0x9932CC);
  public static final TrueColor DARK_RED = new TrueColor(0x8B0000);
  public static final TrueColor DARK_SALMON = new TrueColor(0xE9967A);
  public static final TrueColor DARK_SEA_GREEN = new TrueColor(0x8FBC8F);
  public static final TrueColor DARK_SLATE_BLUE = new TrueColor(0x483D8B);
  public static final TrueColor DARK_SLATE_GRAY = new TrueColor(0x2F4F4F);
  public static final TrueColor DARK_SLATE_GREY = DARK_SLATE_GRAY;
  public static final TrueColor DARK_TURQUOISE = new TrueColor(0x00CED1);
  public static final TrueColor DARK_VIOLET = new TrueColor(0x9400D3);
  public static final TrueColor DEEP_PINK = new TrueColor(0xFF1493);
  public static final TrueColor DEEP_SKY_BLUE = new TrueColor(0x00BFFF);
  public static final TrueColor DIM_GRAY = new TrueColor(0x696969);
  public static final TrueColor DIM_GREY = DIM_GRAY;
  public static final TrueColor DODGER_BLUE = new TrueColor(0x1E90FF);
  public static final TrueColor FIRE_BRICK = new TrueColor(0xB22222);
  public static final TrueColor FLORAL_WHITE = new TrueColor(0xFFFAF0);
  public static final TrueColor FOREST_GREEN = new TrueColor(0x228B22);
  public static final TrueColor FUCHSIA = new TrueColor(0xFF00FF);
  public static final TrueColor GAINSBORO = new TrueColor(0xDCDCDC);
  public static final TrueColor GHOST_WHITE = new TrueColor(0xF8F8FF);
  public static final TrueColor GOLD = new TrueColor(0xFFD700);
  public static final TrueColor GOLDENROD = new TrueColor(0xDAA520);
  public static final TrueColor GRAY = new TrueColor(0x808080);
  public static final TrueColor GREY = GRAY;
  public static final TrueColor GREEN = new TrueColor(0x008000);
  public static final TrueColor GREEN_YELLOW = new TrueColor(0xADFF2F);
  public static final TrueColor HONEYDEW = new TrueColor(0xF0FFF0);
  public static final TrueColor HOT_PINK = new TrueColor(0xFF69B4);
  public static final TrueColor INDIAN_RED = new TrueColor(0xCD5C5C);
  public static final TrueColor INDIGO = new TrueColor(0x4B0082);
  public static final TrueColor IVORY = new TrueColor(0xFFFFF0);
  public static final TrueColor KHAKI = new TrueColor(0xF0E68C);
  public static final TrueColor LAVENDER = new TrueColor(0xE6E6FA);
  public static final TrueColor LAVENDER_BLUSH = new TrueColor(0xFFF0F5);
  public static final TrueColor LAWN_GREEN = new TrueColor(0x7CFC00);
  public static final TrueColor LEMON_CHIFFON = new TrueColor(0xFFFACD);
  public static final TrueColor LIGHT_BLUE = new TrueColor(0xADD8E6);
  public static final TrueColor LIGHT_CORAL = new TrueColor(0xF08080);
  public static final TrueColor LIGHT_CYAN = new TrueColor(0xE0FFFF);
  public static final TrueColor LIGHT_GOLDENROD_YELLOW = new TrueColor(0xFAFAD2);
  public static final TrueColor LIGHT_GRAY = new TrueColor(0xD3D3D3);
  public static final TrueColor LIGHT_GREY = LIGHT_GRAY;
  public static final TrueColor LIGHT_GREEN = new TrueColor(0x90EE90);
  public static final TrueColor LIGHT_PINK = new TrueColor(0xFFB6C1);
  public static final TrueColor LIGHT_SALMON = new TrueColor(0xFFA07A);
  public static final TrueColor LIGHT_SEA_GREEN = new TrueColor(0x20B2AA);
  public static final TrueColor LIGHT_SKY_BLUE = new TrueColor(0x87CEFA);
  public static final TrueColor LIGHT_SLATE_GRAY = new TrueColor(0x778899);
  public static final TrueColor LIGHT_SLATE_GREY = LIGHT_SLATE_GRAY;
  public static final TrueColor LIGHT_STEEL_BLUE = new TrueColor(0xB0C4DE);
  public static final TrueColor LIGHT_YELLOW = new TrueColor(0xFFFFE0);
  public static final TrueColor LIME = new TrueColor(0x00FF00);
  public static final TrueColor LIME_GREEN = new TrueColor(0x32CD32);
  public static final TrueColor LINEN = new TrueColor(0xFAF0E6);
  public static final TrueColor MAGENTA = new TrueColor(0xFF00FF);
  public static final TrueColor MAROON = new TrueColor(0x800000);
  public static final TrueColor MEDIUM_AQUAMARINE = new TrueColor(0x66);
  public static final TrueColor MEDIUM_BLUE = new TrueColor(0x0000CD);
  public static final TrueColor MEDIUM_ORCHID = new TrueColor(0xBA55D3);
  public static final TrueColor MEDIUM_PURPLE = new TrueColor(0x9370D8);
  public static final TrueColor MEDIUM_SEA_GREEN = new TrueColor(0x3CB371);
  public static final TrueColor MEDIUM_SLATE_BLUE = new TrueColor(0x7B);
  public static final TrueColor MEDIUM_SPRING_GREEN = new TrueColor(0x00);
  public static final TrueColor MEDIUM_TURQUOISE = new TrueColor(0x48);
  public static final TrueColor MEDIUM_VIOLET_RED = new TrueColor(0xC7);
  public static final TrueColor MIDNIGHT_BLUE = new TrueColor(0x191970);
  public static final TrueColor MINT_CREAM = new TrueColor(0xF5FFFA);
  public static final TrueColor MISTY_ROSE = new TrueColor(0xFFE4E1);
  public static final TrueColor MOCCASIN = new TrueColor(0xFFE4B5);
  public static final TrueColor NAVAJO_WHITE = new TrueColor(0xFFDEAD);
  public static final TrueColor NAVY = new TrueColor(0x000080);
  public static final TrueColor OLD_LACE = new TrueColor(0xFDF5E6);
  public static final TrueColor OLIVE = new TrueColor(0x808000);
  public static final TrueColor OLIVE_DRAB = new TrueColor(0x6B8E23);
  public static final TrueColor ORANGE = new TrueColor(0xFFA500);
  public static final TrueColor ORANGE_RED = new TrueColor(0xFF4500);
  public static final TrueColor ORCHID = new TrueColor(0xDA70D6);
  public static final TrueColor PALE_GOLDENROD = new TrueColor(0xEEE8AA);
  public static final TrueColor PALE_GREEN = new TrueColor(0x98FB98);
  public static final TrueColor PALE_TURQUOISE = new TrueColor(0xAFEEEE);
  public static final TrueColor PALE_VIOLET_RED = new TrueColor(0xD87093);
  public static final TrueColor PAPAYA_WHIP = new TrueColor(0xFFEFD5);
  public static final TrueColor PEACH_PUFF = new TrueColor(0xFFDAB9);
  public static final TrueColor PERU = new TrueColor(0xCD853F);
  public static final TrueColor PINK = new TrueColor(0xFFC0CB);
  public static final TrueColor PLUM = new TrueColor(0xDDA0DD);
  public static final TrueColor POWDER_BLUE = new TrueColor(0xB0E0E6);
  public static final TrueColor PURPLE = new TrueColor(0x800080);
  public static final TrueColor RED = new TrueColor(0xFF0000);
  public static final TrueColor ROSY_BROWN = new TrueColor(0xBC8F8F);
  public static final TrueColor ROYAL_BLUE = new TrueColor(0x4169E1);
  public static final TrueColor SADDLE_BROWN = new TrueColor(0x8B4513);
  public static final TrueColor SALMON = new TrueColor(0xFA8072);
  public static final TrueColor SANDY_BROWN = new TrueColor(0xF4A460);
  public static final TrueColor SEA_GREEN = new TrueColor(0x2E8B57);
  public static final TrueColor SEA_SHELL = new TrueColor(0xFFF5EE);
  public static final TrueColor SIENNA = new TrueColor(0xA0522D);
  public static final TrueColor SILVER = new TrueColor(0xC0C0C0);
  public static final TrueColor SKY_BLUE = new TrueColor(0x87CEEB);
  public static final TrueColor SLATE_BLUE = new TrueColor(0x6A5ACD);
  public static final TrueColor SLATE_GRAY = new TrueColor(0x708090);
  public static final TrueColor SLATE_GREY = SLATE_GRAY;
  public static final TrueColor SNOW = new TrueColor(0xFFFAFA);
  public static final TrueColor SPRING_GREEN = new TrueColor(0x00FF7F);
  public static final TrueColor STEEL_BLUE = new TrueColor(0x4682B4);
  public static final TrueColor TAN = new TrueColor(0xD2B48C);
  public static final TrueColor TEAL = new TrueColor(0x008080);
  public static final TrueColor THISTLE = new TrueColor(0xD8BFD8);
  public static final TrueColor TOMATO = new TrueColor(0xFF6347);
  public static final TrueColor TURQUOISE = new TrueColor(0x40E0D0);
  public static final TrueColor VIOLET = new TrueColor(0xEE82EE);
  public static final TrueColor WHEAT = new TrueColor(0xF5DEB3);
  public static final TrueColor WHITE = new TrueColor(0xFFFFFF);
  public static final TrueColor WHITE_SMOKE = new TrueColor(0xF5F5F5);
  public static final TrueColor YELLOW = new TrueColor(0xFFFF00);
  public static final TrueColor YELLOW_GREEN = new TrueColor(0x9ACD32);
}
