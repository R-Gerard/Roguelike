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

package com.callidusrobotics.ui;

import java.awt.Color;

import com.callidusrobotics.ConsoleColleague;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.util.NotInstantiableError;

public final class TextBoxBuilder {
  public enum Layout {
    TOPLEFT, TOPCENTER, TOPRIGHT,
    CENTERLEFT, CENTER, CENTERRIGHT,
    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
  };

  private TextBoxBuilder() {
    throw new NotInstantiableError();
  }

  public static PlayerDataBox buildPlayerDataBox(final PlayerCharacter player, final int height, final int width, final Color foreground, final Color background, final Layout layout) {
    final PlayerDataBox box = new PlayerDataBox(player, new ConsoleTextBoxFancy(new DrawStyleStrategyDouble(), 0, 0, height, width, foreground, background));

    setPosition(box, layout);

    return box;
  }

  public static MessageBox buildFancyMessageBox(final String message, final int height, final int width, final Color foreground, final Color background, final Layout layout) {
    final MessageBox box = new MessageBox(new ConsoleTextBoxFancy(new DrawStyleStrategyDouble(), 0, 0, height, width, foreground, background));

    setPosition(box, layout);

    if (message != null && !message.isEmpty()) {
      box.addLine(message + "\n", foreground, background, false);
    }

    return box;
  }

  public static MessageBox buildMessageBox(final String message, final int height, final int width, final Color foreground, final Color background, final Layout layout) {
    final MessageBox box = new MessageBox(new ConsoleTextBoxPlain(new DrawStyleStrategyHeavy(), 0, 0, height, width, foreground, background));

    setPosition(box, layout);

    if (message != null && !message.isEmpty()) {
      box.addLine("\n" + message, foreground, background, false);
    }

    return box;
  }

  public static PaginatedMenuBox buildFancyMenu(final String message, final int height, final int width, final Color foreground, final Color background, final Color selectForeground, final Color selectBackground, final Layout layout) {
    final PaginatedMenuBox menu = new PaginatedMenuBox(new ConsoleTextBoxFancy(new DrawStyleStrategyDouble(), 0, 0, height, width, foreground, background), message, selectForeground, selectBackground);

    setPosition(menu, layout);

    return menu;
  }

  public static PaginatedMenuBox buildMenu(final String message, final int height, final int width, final Color foreground, final Color background, final Color selectForeground, final Color selectBackground, final Layout layout) {
    final PaginatedMenuBox menu = new PaginatedMenuBox(new ConsoleTextBoxPlain(new DrawStyleStrategyHeavy(), 0, 0, height, width, foreground, background), message, selectForeground, selectBackground);

    setPosition(menu, layout);

    return menu;
  }

  protected static void setPosition(final ConsoleTextBox box, final Layout layout) {
    box.setPosition(ConsoleColleague.getPositionCentered(box));

    if (layout != null) {
      if (layout.toString().contains("LEFT")) {
        box.setPosition(box.getRow(), 0);
      } else if (layout.toString().contains("RIGHT")) {
        box.setPosition(box.getRow(), ConsoleColleague.getWidth() - box.getWidth());
      }

      if (layout.toString().contains("TOP")) {
        box.setPosition(0, box.getCol());
      } else if (layout.toString().contains("BOTTOM")) {
        box.setPosition(ConsoleColleague.getHeight() - box.getHeight(), box.getCol());
      }
    }
  }
}
