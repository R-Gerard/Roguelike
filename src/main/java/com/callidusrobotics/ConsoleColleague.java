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

package com.callidusrobotics;

import java.awt.Color;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.attribute.Dimensional;
import com.callidusrobotics.attribute.Drawable;
import com.callidusrobotics.locale.MutableCoordinate;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleFactory;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.ui.ConsoleTextBox;
import com.callidusrobotics.ui.MessageBox;
import com.callidusrobotics.ui.PaginatedMenuBox;
import com.callidusrobotics.ui.PlayerDataBox;
import com.callidusrobotics.ui.TextBoxBuilder;
import com.callidusrobotics.ui.TextBoxBuilder.Layout;

/**
 * Helper class for managing resources like text boxes, menus, etc. that are
 * drawn in a <code>Console</code>.
 *
 * @author Rusty
 * @since 0.0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ConsoleColleague {
  enum DisplayMode {
    STANDARD,
    LONG_MESSAGE_BUFFER;

    public DisplayMode next() {
      return values()[(ordinal() + 1) % values().length];
    }
  }

  private final Console console;
  private final MessageBox msgBufferBoxTall, msgBufferBoxShort;
  private PlayerDataBox playerDataBox;
  private ConsoleTextBox leftPane, rightPane;
  private final Color boxForeground, boxBackground, fontForeground, fontBackground, selectForeground, selectBackground;
  private DisplayMode displayMode = DisplayMode.STANDARD;

  public ConsoleColleague(final Console console, final PlayerCharacter player, final Color boxForeground, final Color boxBackground, final Color fontForeground, final Color fontBackground, final Color selectForeground, final Color selectBackground) {
    this.console = console;
    this.boxForeground = boxForeground;
    this.boxBackground = boxBackground;
    this.fontForeground = fontForeground;
    this.fontBackground = fontBackground;
    this.selectForeground = selectForeground;
    this.selectBackground = selectBackground;

    Validate.notNull(console);
    //Validate.notNull(player);
    Validate.notNull(boxForeground);
    Validate.notNull(boxBackground);
    Validate.notNull(fontForeground);
    Validate.notNull(fontBackground);
    Validate.notNull(selectForeground);
    Validate.notNull(selectBackground);

    final int displayWidth = getWidth() - getDivColumn() + 1;
    msgBufferBoxTall = TextBoxBuilder.buildFancyMessageBox(null, getHeight(), displayWidth, boxForeground, boxBackground, Layout.TOPRIGHT);
    msgBufferBoxShort = TextBoxBuilder.buildFancyMessageBox(null, getHeight() / 2, displayWidth, boxForeground, boxBackground, Layout.TOPRIGHT);
    playerDataBox = TextBoxBuilder.buildPlayerDataBox(player, getHeight() / 2, displayWidth, boxForeground, boxBackground, Layout.BOTTOMRIGHT);

    leftPane = rightPane = null;
  }

  /**
   * Console height accessor.
   *
   * @return The height of the Console
   * @see Console#getHeight()
   */
  public static int getHeight() {
    return ConsoleFactory.getInstance().getHeight();
  }

  /**
   * Console width accessor.
   *
   * @return The width of the Console
   * @see Console#getWidth()
   */
  public static int getWidth() {
    return ConsoleFactory.getInstance().getWidth();
  }

  /**
   * Default font color accessor.
   *
   * @return The default font color, never null
   */
  public Color getFontForeground() {
    return fontForeground;
  }

  /**
   * Divide the console into two panes, a square on the left and the rest on the
   * right.
   *
   * @return The dividing column of the two panes
   */
  public static int getDivColumn() {
    return Math.min(ConsoleFactory.getInstance().getWidth(), ConsoleFactory.getInstance().getHeight());
  }

  /**
   * Computes the top-left position of the box when centered in the left pane.
   *
   * @param box
   *          The box to center in the console, nullable
   * @return The top left corner
   */
  public static MutableCoordinate getPosition(final Dimensional box) {
    final int boxWidth = box == null ? 1 : box.getWidth();
    final int boxHeight = box == null ? 1 : box.getHeight();

    final int row = ConsoleFactory.getInstance().getHeight() - 1;
    final int col = getDivColumn();

    final int hOffset = (col - boxWidth) / 2;
    final int vOffset = (row - boxHeight) / 2;

    return new MutableCoordinate(vOffset, hOffset);
  }

  /**
   * Computes the top-left position of the box when centered in the whole
   * console.
   *
   * @param box
   *          The box to center in the console, nullable
   * @return The top left corner
   */
  public static MutableCoordinate getPositionCentered(final Dimensional box) {
    final int boxWidth = box == null ? 1 : box.getWidth();
    final int boxHeight = box == null ? 1 : box.getHeight();

    final int row = ConsoleFactory.getInstance().getHeight();
    final int col = ConsoleFactory.getInstance().getWidth();

    final int hOffset = (col - boxWidth) / 2;
    final int vOffset = (row - boxHeight) / 2;

    return new MutableCoordinate(vOffset, hOffset);
  }

  /**
   * Cycles to the next display mode for the right-hand side of the console.
   */
  public void nextDisplayMode() {
    displayMode = displayMode.next();

    drawDisplay();
  }

  /**
   * Player mutator. Used to generate a PlayerDataBox for display in the
   * console.
   *
   * @param player
   *          The PlayerCharacter
   * @see PlayerDataBox
   */
  public void setPlayer(final PlayerCharacter player) {
    final int displayWidth = getWidth() - getDivColumn() + 1;
    playerDataBox = TextBoxBuilder.buildPlayerDataBox(player, getHeight() / 2, displayWidth, boxForeground, boxBackground, Layout.BOTTOMRIGHT);
    drawDisplay();
  }

  /**
   * Draws the object in the Console.
   *
   * @param object
   *          The object to draw, nullable
   */
  public void draw(final Drawable object) {
    if (object == null) {
      return;
    }

    object.draw(console);
  }

  /**
   * Creates a multiple-choice menu and returns the user's selection.
   *
   * @param message
   *          A message that explains the choices provided, not null
   * @param list
   *          The list of choices, not null
   * @param fullScreen
   *          If true, the box fills the entire console area
   * @param preformatted
   *          If the choice strings have been preformatted (i.e. internal
   *          spacing should be preserved or it is OK to tokenize on spaces)
   * @return The index of the choice string the user selected
   * @see PaginatedMenuBox
   */
  public int select(final String message, final List<String> list, final boolean fullScreen, final boolean preformatted) {
    PaginatedMenuBox menu;
    if (fullScreen) {
      menu = TextBoxBuilder.buildFancyMenu(message, getHeight(), getWidth(), boxForeground, boxBackground, selectForeground, selectBackground, Layout.CENTER);
    } else {
      int width = message.length();
      for (final String listItem : list) {
        if (listItem.length() > width) {
          width = listItem.length();
        }
      }

      if (width % 2 == 0) {
        width++;
      }

      menu = TextBoxBuilder.buildFancyMenu(String.format("%" + (width - (width - message.length()) / 2) + "s", message), list.size() + 7, width + 4, boxForeground, boxBackground, selectForeground, selectBackground, Layout.CENTER);
    }

    for (final String listItem : list) {
      menu.addLine(listItem, fontForeground, fontBackground, preformatted);
    }

    console.hideCursor();
    console.clear();
    menu.draw(console);
    final int index = menu.selectIndex(console, true);
    console.clear();

    drawDisplay();

    return index;
  }

  /**
   * Draws the contents of the left-hand side of the console. Clears the LHS of
   * the screen if the parameter is null.
   *
   * @param box
   *          The object to draw, nullable
   */
  public void drawLeftPane(final ConsoleTextBox box) {
    draw(leftPane, box);
    leftPane = box;
  }

  /**
   * Draws the contents of the right-hand side of the console. Clears the RHS of
   * the screen if the parameter is null.
   *
   * @param box
   *          The object to draw, nullable
   */
  public void drawRightPane(final ConsoleTextBox box) {
    draw(rightPane, box);
    rightPane = box;
  }

  /**
   * Draws an object onto the Console. If there is a previous object, it clears
   * the region occupied by previous.
   *
   * @param previous
   *          The object last drawn, nullable
   * @param next
   *          The object to be drawn, nullable
   */
  protected void draw(final ConsoleTextBox previous, final ConsoleTextBox next) {
    // Clear the previously drawn box
    if (previous != null) {
      final int row = previous.getRow();
      final int col = previous.getCol();

      for (int r = row; r < row + previous.getHeight(); r++) {
        for (int c = col; c < col + previous.getWidth(); c++) {
          console.print(r, c, ' ', boxBackground, boxBackground);
        }
      }
    }

    if (next != null) {
      next.draw(console);
    }
  }

  /**
   * Adds a string to the message log and displays it appropriately with
   * vertical scrolling. Uses the default colors for the message log.
   *
   * @param string
   *          The entry to append to the message log, not null
   */
  public void printMessageLogEntry(final String string) {
    printMessageLogEntry(string, fontForeground, fontBackground);
  }

  /**
   * Adds a string to the message log and displays it appropriately with
   * vertical scrolling.
   *
   * @param string
   *          The entry to append to the message log, not null
   * @param foreground
   *          The font foreground color to display for the string, not null
   * @param background
   *          The font background color to display for the string, not null
   */
  public void printMessageLogEntry(final String string, final Color foreground, final Color background) {
    msgBufferBoxTall.addLine(string, foreground, background, false);
    msgBufferBoxShort.addLine(string, foreground, background, false);

    drawDisplay();
  }

  /**
   * Prints a string of text directly to the console, starting in the top-left
   * corner. This method does not perform boundary checking.
   * <p>
   * This method does not return control to the user. Use
   * {@link #waitForKeyPress()}.
   * <p>
   * Prefer {@link ConsoleColleague#splash(String)}, but this method is
   * useful if box-drawing characters are not desired.
   *
   * @param text
   *          The text to display, not null
   * @param foreground
   *          The font foreground color to display for the string, not null
   * @param background
   *          The font background color to display for the string, not null
   * @see #waitForKeyPress()
   */
  public void printRaw(final String text, final Color foreground, final Color background) {
    int row = 0, col = 0;
    for (final char character : text.toCharArray()) {
      if (character == '\n') {
        row++;
        col = 0;
      } else {
        console.print(row, col++, character, foreground, background);
      }
    }

    console.hideCursor();
    console.render();
  }

  /**
   * Waits for the user to press any key.
   * <p>
   * Useful in conjunction with {@link #printRaw(String, Color, Color)}.
   * @see Console#getKeyPress()
   */
  public void waitForKeyPress() {
    console.getKeyPress();
  }

  /**
   * Clears the console and creates a MessageBox centered in the console. Waits
   * for the user to press any key before re-drawing the previous display.
   *
   * @param text
   *          The contents of the MessageBox, not null
   * @see MessageBox
   */
  public void splash(final String text) {
    final MessageBox box = TextBoxBuilder.buildFancyMessageBox(null, getHeight(), getWidth(), boxForeground, boxBackground, Layout.CENTER);
    box.addLine(text, fontForeground, fontBackground, true);
    console.hideCursor();
    console.clear();
    box.draw(console);
    box.pause(console);
    console.clear();

    if (leftPane != null) {
      leftPane.draw(console);
    }

    drawDisplay();
  }

  /**
   * Creates a MessageBox and overlays it over the current LHS display. Waits
   * for the user to press a confirmation key before re-drawing the underlying
   * display.
   *
   * @param message
   *          The contents of the MessageBox, not null
   * @see MessageBox
   */
  public void alert(final String message) {
    // TODO: A height of 10 rows is just a guess. Try to determine the box height based on length of text.
    final MessageBox box = TextBoxBuilder.buildMessageBox("Press ENTER or ESC to quit.", 10, 40, boxForeground, boxBackground, Layout.CENTER);

    box.addLine("#", fontBackground, fontBackground, false);
    final String[] lines = message.split("\n");
    for (final String line : lines) {
      box.addLine(line, fontForeground, fontBackground, false);
    }

    console.hideCursor();
    console.clear();
    box.draw(console);
    box.pause(console);
    console.clear();
    leftPane.draw(console);
    drawDisplay();
  }

  /**
   * Clears the console and creates a MessageBox centered in the console. Waits
   * for the user to select "Yes" or "No" before re-drawing the previous
   * display.
   *
   * @param message
   *          The yes/no question for the user to confirm, not null
   * @return The user's yes/no response
   */
  public boolean confirm(final String message) {
    // TODO: A height of 10 rows is just a guess. Try to determine the box dimensions based on length of text.
    final PaginatedMenuBox menu = TextBoxBuilder.buildMenu(message, 10, 40, boxForeground, boxBackground, selectForeground, selectBackground, Layout.CENTER);
    menu.addLine("Yes", fontForeground, fontBackground, false);
    menu.addLine("No", fontForeground, fontBackground, false);

    console.hideCursor();
    console.clear();
    menu.draw(console);
    final String result = menu.selectValue(console, true);
    console.clear();
    leftPane.draw(console);
    msgBufferBoxTall.draw(console);

    return "Yes".equals(result);
  }

  /**
   * Clears the console and creates a MessageBox centered in the console. Waits
   * for the user to enter a numeric value and press a confirmation key before
   * re-drawing the previous display.
   *
   * @param defaultVal
   *          The default value the user can resort to by pressing ENTER
   * @param maxLen
   *          The maximum number of digits the user may type
   * @return The integer value the user provided
   */
  public int getQuantity(final int defaultVal, final int maxLen) {
    final MessageBox box = TextBoxBuilder.buildMessageBox("", 10, 40, boxForeground, boxBackground, Layout.CENTER);
    final String prompt = "\n\n\n Enter quantity (" + defaultVal + "): ";

    box.addLine(prompt, fontForeground, fontBackground, true);
    console.hideCursor();
    console.clear();
    console.setCursor(box.getRow() + 4, box.getCol() + prompt.length() - 2, new ConsoleGraphic('_', fontForeground, fontBackground));
    box.draw(console);
    final String input = console.getline(maxLen, fontForeground, fontBackground);
    console.clear();
    leftPane.draw(console);
    drawDisplay();

    try {
      return Integer.parseInt(input);
    } catch (final NumberFormatException e) {
      return defaultVal;
    }
  }

  /**
   * Console title mutator.
   *
   * @param title
   *          The string to display in the title bar of the console, nullable
   */
  public void setTitle(final String title) {
    console.setTitle(title);
  }

  /**
   * Clears the console.
   *
   * @see Console#clear()
   */
  public void clear() {
    console.hideCursor();
    console.clear();
  }

  /**
   * Renders the console after one or more updates have been made.
   *
   * @see Console#render()
   */
  public void render() {
    console.render();
  }

  private void drawDisplay() {
    if (leftPane != null) {
      leftPane.draw(console);
    }

    if (displayMode == DisplayMode.STANDARD) {
      msgBufferBoxShort.draw(console);
      playerDataBox.draw(console);
    }

    if (displayMode == DisplayMode.LONG_MESSAGE_BUFFER) {
      msgBufferBoxTall.draw(console);
    }
  }
}
