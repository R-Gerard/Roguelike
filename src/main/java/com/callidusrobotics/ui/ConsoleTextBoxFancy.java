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

import com.callidusrobotics.swing.Console;

final class ConsoleTextBoxFancy extends AbstractConsoleTextBox {
  public ConsoleTextBoxFancy(final DrawStyleStrategy drawStyleStrategy, final int row, final int col, final int height, final int width, final Color foreground, final Color background) {
    super(drawStyleStrategy, row, col, height, width, foreground, background);

    Validate.isTrue(height > 2, "Invalid height");
    Validate.isTrue(width > 2, "Invalid width");
  }

  @Override
  protected int getInternalWidth() {
    return getWidth() - 2;
  }

  @Override
  protected int getInternalHeight() {
    return getHeight() - 2;
  }

  @Override
  protected int getInternalRow() {
    return getRow() + 1;
  }

  @Override
  protected int getInternalCol() {
    return getCol() + 1;
  }

  @Override
  public void setHeight(final int height) {
    Validate.isTrue(height > 2, "Invalid height");
    this.height = height;
  }

  @Override
  public void setWidth(final int width) {
    Validate.isTrue(width > 2, "Invalid width");
    this.width = width;
  }

  @Override
  protected void draw(final Console console, final char hLine, final char vLine, final char topLeft, final char topRight, final char bottomLeft, final char bottomRight, final char intersection) {
    super.draw(console, hLine, vLine, intersection, intersection, intersection, intersection, intersection);

    drawCorners(console, topLeft, topRight, bottomLeft, bottomRight);
  }

  protected void drawCorners(final Console console, final char topLeft, final char topRight, final char bottomLeft, final char bottomRight) {
    final int row = getRow();
    final int col = getCol();
    final int height = getHeight();
    final int width = getWidth();

    // Top-Left corner
    console.print(row, col, topLeft, foreground, background);
    console.print(row, col + 1, topRight, foreground, background);
    console.print(row + 1, col, bottomLeft, foreground, background);

    // Top-Right corner
    console.print(row, col + width - 1, topRight, foreground, background);
    console.print(row, col + width - 2, topLeft, foreground, background);
    console.print(row + 1, col + width - 1, bottomRight, foreground, background);

    // Bottom-Left corner
    console.print(row + height - 1, col, bottomLeft, foreground, background);
    console.print(row + height - 1, col + 1, bottomRight, foreground, background);
    console.print(row + height - 2, col, topLeft, foreground, background);

    // Bottom-Right corner
    console.print(row + height - 1, col + width - 1, bottomRight, foreground, background);
    console.print(row + height - 1, col + width - 2, bottomLeft, foreground, background);
    console.print(row + height - 2, col + width - 1, topRight, foreground, background);
  }
}
