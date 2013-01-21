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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines Unicode character sets.
 *
 * @author Rusty
 * @since 0.0.1
 */
@SuppressWarnings("PMD.LongVariable")
public final class Unicode {
  private Unicode() {
    throw new NotInstantiableError();
  }

  // Latin-1 Supplement
  // http://www.unicode.org/charts/PDF/U0080.pdf
  public static final char NO_BREAK_SPACE = '\u00A0';
  public static final char PUNCTUATION_MIDDLEDOT_INTERPUNCT = '\u00B7';

  // Box drawing
  // http://unicode.org/charts/PDF/U2500.pdf
  public static final char LINE_HEAVY_HORIZONTAL = '\u2501';
  public static final char LINE_HEAVY_VERTICAL = '\u2503';
  public static final char LINE_HEAVY_DOWN_AND_RIGHT = '\u250F';
  public static final char LINE_HEAVY_DOWN_AND_LEFT = '\u2513';
  public static final char LINE_HEAVY_UP_AND_RIGHT = '\u2517';
  public static final char LINE_HEAVY_UP_AND_LEFT = '\u251B';
  public static final char LINE_HEAVY_VERTICAL_AND_RIGHT = '\u2523';
  public static final char LINE_HEAVY_VERTICAL_AND_LEFT = '\u252B';
  public static final char LINE_HEAVY_HORIZONTAL_AND_DOWN = '\u2533';
  public static final char LINE_HEAVY_HORIZONTAL_AND_UP = '\u253B';
  public static final char LINE_HEAVY_HORIZONTAL_AND_VERTICAL = '\u254B';
  public static final char LINE_DOUBLE_HORIZONTAL = '\u2550';
  public static final char LINE_DOUBLE_VERTICAL = '\u2551';
  public static final char LINE_DOUBLE_DOWN_AND_RIGHT = '\u2554';
  public static final char LINE_DOUBLE_DOWN_AND_LEFT = '\u2557';
  public static final char LINE_DOUBLE_UP_AND_RIGHT = '\u255A';
  public static final char LINE_DOUBLE_UP_AND_LEFT = '\u255D';
  public static final char LINE_DOUBLE_VERTICAL_AND_RIGHT = '\u2560';
  public static final char LINE_DOUBLE_VERTICAL_AND_LEFT = '\u2563';
  public static final char LINE_DOUBLE_HORIZONTAL_AND_DOWN = '\u2566';
  public static final char LINE_DOUBLE_HORIZONTAL_AND_UP = '\u2569';
  public static final char LINE_DOUBLE_HORIZONTAL_AND_VERTICAL = '\u256C';

  // Arrows
  // http://unicode.org/charts/PDF/U2190.pdf
  public static final char ARROW_WEST = '\u2190';
  public static final char ARROW_NORTH = '\u2191';
  public static final char ARROW_EAST = '\u2192';
  public static final char ARROW_SOUTH = '\u2193';
  public static final char ARROW_NORTHWEST = '\u2196';
  public static final char ARROW_NORTHEAST = '\u2197';
  public static final char ARROW_SOUTHEAST = '\u2198';
  public static final char ARROW_SOUTHWEST = '\u2199';

  // Block elements
  // http://unicode.org/charts/PDF/U2580.pdf
  public static final char SHADE_SOLID_100 = '\u2588';
  public static final char SHADE_LIGHT_25 = '\u2591';
  public static final char SHADE_MEDIUM_50 = '\u2592';
  public static final char SHADE_HEAVY_75 = '\u2593';

  // Miscellaneous Symbols
  // http://unicode.org/charts/PDF/U2600.pdf
  public static final char SYMBOL_MISC_STAR_SOLID = '\u2605';
  public static final char SYMBOL_MISC_TELEPHONE_SOLID = '\u260E';
  public static final char SYMBOL_MISC_BALLOT_BOX = '\u2610';
  public static final char SYMBOL_MISC_BALLOT_BOX_WITH_CHECK = '\u2611';
  public static final char SYMBOL_MISC_BALLOT_BOX_WITH_X = '\u2612';
  public static final char SYMBOL_MISC_ANKH = '\u2625';

  public static final char SYMBOL_CARDS_SPADE_SOLID = '\u2660';
  public static final char SYMBOL_CARDS_HEART_OUTLINE = '\u2661';
  public static final char SYMBOL_CARDS_DIAMOND_OUTLINE = '\u2662';
  public static final char SYMBOL_CARDS_CLUB_SOLID = '\u2663';
  public static final char SYMBOL_CARDS_SPADE_OUTLINE = '\u2664';
  public static final char SYMBOL_CARDS_HEART_SOLID = '\u2665';
  public static final char SYMBOL_CARDS_DIAMOND_SOLID = '\u2666';
  public static final char SYMBOL_CARDS_CLUB_OUTLINE = '\u2667';

  public static final char SYMBOL_MUSIC_QUARTER_NOTE_CROTCHET = '\u2669';
  public static final char SYMBOL_MUSIC_EIGHTH_NOTE_QUAVER = '\u266A';
  public static final char SYMBOL_MUSIC_BEAMED_EIGHTH_NOTES_QUAVERS = '\u266B';
  public static final char SYMBOL_MUSIC_BEAMED_SIXTEENTH_NOTES_SEMIQUAVERS = '\u266A';

  public static final char SYMBOL_MISC_INTERSECTION_FOUR_CORNERS = '\u26F6';

  // Runic alphabet
  // http://unicode.org/charts/PDF/U16A0.pdf
  public static final char RUNE_FEHU_F = '\u16A0';
  public static final char RUNE_URUZ_U = '\u16A2';
  public static final char RUNE_THURISAZ_TH = '\u16A6';
  public static final char RUNE_ANSUZ_A = '\u16A8';
  public static final char RUNE_RAIDO_R = '\u16B1';
  public static final char RUNE_KAUNA_K = '\u16B2';
  public static final char RUNE_GEBO_G = '\u16B7';
  public static final char RUNE_WUNJO_W = '\u16B9';
  public static final char RUNE_HAGLAZ_H = '\u16BA';
  public static final char RUNE_NAUDIZ_N = '\u16BE';
  public static final char RUNE_ISAZ_I = '\u16C1';
  public static final char RUNE_JERAN_J = '\u16C3';
  public static final char RUNE_IWAZ_AE = '\u16C7';
  public static final char RUNE_PEORTH_P = '\u16C8';
  public static final char RUNE_ALGIZ_Z = '\u16C9';
  public static final char RUNE_SOWILO_S = '\u16CA';
  public static final char RUNE_TIWAZ_T = '\u16CF';
  public static final char RUNE_BERKANAN_B = '\u16D2';
  public static final char RUNE_EHWAZ_E = '\u16D6';
  public static final char RUNE_MANNAZ_M = '\u16D7';
  public static final char RUNE_LAGU_L = '\u16DA';
  public static final char RUNE_INGWAZ_NG = '\u16DC';
  public static final char RUNE_DAGAZ_D = '\u16DE';
  public static final char RUNE_OTHALAN_O = '\u16DF';

  public static final List<Character> ELDER_FUTHARK;

  static {
    final Character[] allRunes = { RUNE_FEHU_F, RUNE_URUZ_U, RUNE_THURISAZ_TH, RUNE_ANSUZ_A, RUNE_RAIDO_R, RUNE_KAUNA_K, RUNE_GEBO_G, RUNE_WUNJO_W,
        RUNE_HAGLAZ_H, RUNE_NAUDIZ_N, RUNE_ISAZ_I, RUNE_JERAN_J, RUNE_IWAZ_AE, RUNE_PEORTH_P, RUNE_ALGIZ_Z, RUNE_SOWILO_S, RUNE_TIWAZ_T, RUNE_BERKANAN_B,
        RUNE_EHWAZ_E, RUNE_MANNAZ_M, RUNE_LAGU_L, RUNE_INGWAZ_NG, RUNE_OTHALAN_O, RUNE_DAGAZ_D };

    ELDER_FUTHARK = Collections.unmodifiableList(Arrays.asList(allRunes));
  }
}
