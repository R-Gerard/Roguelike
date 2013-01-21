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

package com.callidusrobotics.example;

import java.awt.Font;
import java.awt.event.KeyEvent;

import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleFactory;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.swing.FontFactory;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.Unicode;

@SuppressWarnings("PMD")
public final class ConsoleDemo {
  private static Console console;

  public static void main(final String[] args) {
    final Font font = FontFactory.makeTrueTypeFontResource("/fonts/custom/commodore64.ttf", 8.0f);
    ConsoleFactory.initInstance(font, 50, 80, 1000);

    console = ConsoleFactory.getInstance();
    console.setTitle("Hello, World!");

    printDemo();
    getlineDemo();
    cursorDemo();

    console.exit();
  }

  private ConsoleDemo() {
    throw new NotInstantiableError();
  }

  private static void printDemo() {
    console.print(1, 1, " !\"#$%&'()*+,-./0123456789:;<=>?", TrueColor.LIME, TrueColor.BLACK);
    console.print(2, 1, "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_", TrueColor.LIME, TrueColor.BLACK);
    console.print(3, 1, "`abcdefghijklmnopqrstuvwxyz{|}~", TrueColor.LIME, TrueColor.BLACK);
    console.print(5, 1, Unicode.ARROW_NORTH + " " + Unicode.ARROW_NORTHEAST + " " + Unicode.ARROW_EAST + " " + Unicode.ARROW_SOUTHEAST, TrueColor.PINK, TrueColor.BLACK);
    console.print(5, 9, Unicode.ARROW_SOUTH + " " + Unicode.ARROW_SOUTHWEST + " " + Unicode.ARROW_WEST + " " + Unicode.ARROW_NORTHWEST, TrueColor.PINK, TrueColor.BLACK);
    console.print(5, 17, Unicode.SHADE_LIGHT_25 + " " + Unicode.SHADE_MEDIUM_50 + " " + Unicode.SHADE_HEAVY_75 + " " + Unicode.SHADE_SOLID_100, TrueColor.GRAY, TrueColor.BLACK);
    console.print(5, 25, "" + Unicode.SYMBOL_CARDS_CLUB_OUTLINE + Unicode.SYMBOL_CARDS_DIAMOND_OUTLINE + Unicode.SYMBOL_CARDS_SPADE_OUTLINE + Unicode.SYMBOL_CARDS_HEART_OUTLINE, TrueColor.RED, TrueColor.BLACK);
    console.print(5, 29, "" + Unicode.SYMBOL_CARDS_CLUB_SOLID + Unicode.SYMBOL_CARDS_DIAMOND_SOLID + Unicode.SYMBOL_CARDS_SPADE_SOLID + Unicode.SYMBOL_CARDS_HEART_SOLID, TrueColor.RED, TrueColor.BLACK);

    final StringBuilder stringBuilder = new StringBuilder();
    for (char rune : Unicode.ELDER_FUTHARK) {
      stringBuilder.append(rune);
    }
    console.print(7, 1, stringBuilder.toString(), TrueColor.CYAN, TrueColor.BLACK);
  }

  private static void getlineDemo() {
    console.print(9, 1, new ConsoleGraphic('>', TrueColor.LIME, TrueColor.BLACK));
    console.setCursor(9, 2, new ConsoleGraphic('_', TrueColor.LIGHT_GRAY, null));
    console.render();
    final String str = console.getline(20, TrueColor.GRAY, TrueColor.BLACK);
    console.setTitle(str);
  }

  private static void cursorDemo() {
    int row = console.getHeight() / 2;
    int col = console.getWidth() / 2;

    console.setCursor(row, col, new ConsoleGraphic('@', TrueColor.WHITE_SMOKE, null));
    console.showCursor();

    boolean keepPolling = true;
    while (keepPolling) {
      console.render();

      final KeyEvent input = console.getKeyPress();
      switch (input.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          col--;
          break;

        case KeyEvent.VK_RIGHT:
          col++;
          break;

        case KeyEvent.VK_UP:
          row--;
          break;

        case KeyEvent.VK_DOWN:
          row++;
          break;

        case 'Q':
        case KeyEvent.VK_ESCAPE:
          keepPolling = false;
          break;

        default:
          break;
      }

      if (row < 0) {
        row = 0;
      } else if (row >= console.getHeight()) {
        row = console.getHeight() - 1;
      }

      if (col < 0) {
        col = 0;
      } else if (col >= console.getWidth()) {
        col = console.getWidth() - 1;
      }

      console.moveCursor(row, col);
    }
  }
}
