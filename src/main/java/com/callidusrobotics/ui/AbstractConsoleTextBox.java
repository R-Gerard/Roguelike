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

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.attribute.Colorable;
import com.callidusrobotics.attribute.Resizable;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.MutableCoordinate;
import com.callidusrobotics.swing.Console;

/**
 * Abstract class defining common methods for objects that occupy regions of a
 * console that contain text.
 *
 * @author Rusty
 * @since 0.0.1
 */
abstract class AbstractConsoleTextBox implements ConsoleTextBox, Resizable, Colorable {
  protected DrawStyleStrategy drawStyleStrategy;
  protected MutableCoordinate topLeftCorner;
  protected int height, width;
  protected Color foreground, background;

  public AbstractConsoleTextBox(final DrawStyleStrategy drawStyleStrategy, final int row, final int col, final int height, final int width, final Color foreground, final Color background) {
    Validate.notNull(drawStyleStrategy);
    Validate.isTrue(height > 0, "Invalid height");
    Validate.isTrue(width > 0, "Invalid width");

    this.drawStyleStrategy = drawStyleStrategy;
    topLeftCorner = new MutableCoordinate(row, col);
    this.height = height;
    this.width = width;
    this.foreground = foreground;
    this.background = background;
  }

  @Override
  public void draw(final Console console) {
    drawStyleStrategy.draw(console, this);
  }

  protected void draw(final Console console, final char hLine, final char vLine, final char topLeft, final char topRight, final char bottomLeft, final char bottomRight, final char intersection) {
    final int row = getInternalRow();
    final int col = getInternalCol();

    // Erase everything inside the box so that there is no "overlap" of old text/graphics behind the menu
    for (int r = 0; r < getHeight(); r++) {
      for (int c = 0; c < getWidth(); c++) {
        console.print(getRow() + r, getCol() + c, ' ', background, background);
      }
    }

    for (int c = 1; c < getInternalWidth() - 1; c++) {
      console.print(row, c + col, hLine, foreground, background);
      console.print(row + getInternalHeight() - 1, c + col, hLine, foreground, background);
    }

    for (int r = 1; r < getInternalHeight() - 1; r++) {
      console.print(row + r, col, vLine, foreground, background);
      console.print(row + r, col + getInternalWidth() - 1, vLine, foreground, background);
    }

    console.print(row, col, topLeft, foreground, background);
    console.print(row, col + getInternalWidth() - 1, topRight, foreground, background);
    console.print(row + getInternalHeight() - 1, col, bottomLeft, foreground, background);
    console.print(row + getInternalHeight() - 1, col + getInternalWidth() - 1, bottomRight, foreground, background);
  }

  protected int getInternalWidth() {
    return getWidth();
  }

  protected int getInternalHeight() {
    return getHeight();
  }

  protected int getInternalRow() {
    return getRow();
  }

  protected int getInternalCol() {
    return getCol();
  }

  @Override
  public final Coordinate getPosition() {
    return topLeftCorner;
  }

  @Override
  public final void setPosition(final Coordinate topLeftCorner) {
    this.topLeftCorner.setPosition(topLeftCorner);
  }

  @Override
  public final void setPosition(final int row, final int col) {
    this.topLeftCorner.setPosition(row, col);
  }

  @Override
  public final int getRow() {
    return topLeftCorner.getRow();
  }

  @Override
  public final int getCol() {
    return topLeftCorner.getCol();
  }

  @Override
  public final Color getForeground() {
    return foreground;
  }

  @Override
  public final void setForeground(final Color foreground) {
    this.foreground = foreground;
  }

  @Override
  public final Color getBackground() {
    return background;
  }

  @Override
  public final void setBackground(final Color background) {
    this.background = background;
  }

  @Override
  public final int getWidth() {
    return width;
  }

  @Override
  public final int getHeight() {
    return height;
  }

  @Override
  public void setHeight(final int height) {
    Validate.isTrue(height > 0, "Invalid height");
    this.height = height;
  }

  @Override
  public void setWidth(final int width) {
    Validate.isTrue(width > 0, "Invalid width");
    this.width = width;
  }
}
