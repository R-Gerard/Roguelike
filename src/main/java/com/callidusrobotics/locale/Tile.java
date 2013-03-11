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

package com.callidusrobotics.locale;

import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.object.AbstractGameObject;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.swing.MutableConsoleGraphic;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.Unicode;

/**
 * The basic building-block of DungeonLevel objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonLevel
 */
public class Tile extends AbstractGameObject {
  public static final String INV_BARRIER_NAME = "Invisible Barrier";
  public static final String WALL_NAME = "Wall";
  public static final String FLOOR_NAME = "Floor";
  private static final String UNEXPLORED_MSG = "You can't see there.";

  protected boolean barrier;
  protected boolean alwaysOnTop;
  protected Direction exit = null;
  protected MutableConsoleGraphic unexploredGraphic = null;
  protected MutableConsoleGraphic outOfSightGraphic = null;

  public static Tile makeDefaultInvisibleBarrier() {
    return new Tile(new ConsoleGraphic(' ', TrueColor.BLACK, TrueColor.BLACK), INV_BARRIER_NAME, UNEXPLORED_MSG, true, true);
  }

  public static Tile makeDefaultWall() {
    return new Tile(new ConsoleGraphic(Unicode.SHADE_SOLID_100, TrueColor.GRAY, TrueColor.GRAY), WALL_NAME, "Stone wall.", true, true);
  }

  public static Tile makeDefaultFloor() {
    return new Tile(new ConsoleGraphic('.', TrueColor.GRAY, TrueColor.GRAY.darker()), FLOOR_NAME, "Stone floor.", false, false);
  }

  public Tile(final Tile other) {
    super(other.consoleGraphic, other.name, other.description);

    this.barrier = other.barrier;
    this.exit = other.exit;
  }

  public Tile(final ConsoleGraphic consoleGraphic, final String name, final String description, final boolean barrier, final boolean alwaysOnTop) {
    super(consoleGraphic, name, description);

    this.barrier = barrier;
    this.alwaysOnTop = alwaysOnTop;
  }

  public Tile(final ConsoleGraphic consoleGraphic, final String name, final String description, final boolean barrier, final boolean alwaysOnTop, final Direction exit) {
    super(consoleGraphic, name, description);

    this.barrier = barrier;
    this.alwaysOnTop = alwaysOnTop;
    this.exit = exit;
  }

  @Override
  public boolean canAddItemToInventory(final Inventoriable item) {
    return true;
  }

  @Override
  public String getDescription() {
    if (inventory.isEmpty() || alwaysOnTop) {
      return super.getDescription();
    }

    String surface;
    if (exit == null) {
      surface = "ground.";
    } else {
      surface = super.getDescription().toLowerCase();
    }

    if (inventory.size() == 1) {
      return "A " + inventory.get(0).getName() + " lying on the " + surface;
    }

    return "A pile of items lying on the " + surface;
  }

  @Override
  public int getInventoryValue() {
    if (alwaysOnTop) {
      return 0;
    }

    return super.getInventoryValue();
  }

  @Override
  public void draw(final Console console) {
    // Draw the tile
    console.print(getRow(), getCol(), consoleGraphic);

    // Layer the top item over the tile
    if (!inventory.isEmpty() && !alwaysOnTop) {
      inventory.get(0).setPosition(getPosition());
      inventory.get(0).draw(console);
    }
  }

  /**
   * ConsoleGraphic accessor. Returns a mutable copy of the original graphic so
   * that others can safely change the appearance of this temporarily.
   *
   * @return A mutable copy of the ConsoleGraphic attribute
   */
  @Override
  public MutableConsoleGraphic getConsoleGraphic() {
    if (inventory.isEmpty() || alwaysOnTop) {
      return new MutableConsoleGraphic(consoleGraphic);
    }

    final MutableConsoleGraphic hybrid = new MutableConsoleGraphic(inventory.get(0).getConsoleGraphic());
    hybrid.setBackground(consoleGraphic.getBackground());
    return hybrid;
  }

  protected MutableConsoleGraphic getUnexploredGraphic() {
    if (unexploredGraphic == null) {
      unexploredGraphic = new MutableConsoleGraphic(consoleGraphic);
      unexploredGraphic.setForeground(TrueColor.BLACK);
      unexploredGraphic.setBackground(TrueColor.BLACK);
    }

    return unexploredGraphic;
  }

  protected MutableConsoleGraphic getOutOfSightGraphic() {
    if (outOfSightGraphic == null) {
      outOfSightGraphic = new MutableConsoleGraphic(consoleGraphic);
      outOfSightGraphic.setForeground(outOfSightGraphic.getForeground().darker());
      outOfSightGraphic.setBackground(outOfSightGraphic.getBackground().darker());
    }

    return outOfSightGraphic;
  }

  public boolean isBarrier() {
    return barrier;
  }

  public boolean isAlwaysOnTop() {
    return alwaysOnTop;
  }

  public Direction getExit() {
    return exit;
  }
}
