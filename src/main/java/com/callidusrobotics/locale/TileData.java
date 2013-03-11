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

import java.awt.Color;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.ItemFactory;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.TrueColor;

/**
 * POJO for marshalling and unmarshalling Tile objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Tile
 */
@XmlRootElement
final class TileData {
  @XmlAttribute(name = "id", required = true) String identifier;
  @XmlElement(required = true) String character;
  @XmlElement(required = true) String foreground;
  @XmlElement(required = false) String background;
  @XmlElement(required = true) boolean barrier;
  @XmlElement(required = false) Boolean alwaysOnTop;
  @XmlElement(required = false) String name;
  @XmlElement(required = false) String description;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> fixedItems;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> randomItems;

  TileData() { /* Required by JAXB */ }

  TileData(final char identifier, final Tile tile) {
    this(identifier, tile.getConsoleGraphic().getCharacter(), new TrueColor(tile.getForeground().getRGB()), new TrueColor(tile.getBackground().getRGB()), tile.isBarrier(), tile.isAlwaysOnTop(), tile.getName(), tile.getDescription());
  }

  TileData(final char identifier, final char character, final TrueColor foreground, final TrueColor background, final boolean barrier, final Boolean alwaysOnTop, final String name, final String description) {
    this.identifier = Character.toString(identifier);
    this.character = Character.toString(character);
    this.foreground = foreground.toString();
    this.background = background == null ? null : background.toString();
    this.barrier = barrier;
    this.alwaysOnTop = alwaysOnTop;
    this.name = name;
    this.description = description;
  }

  char getId() {
    return identifier.charAt(0);
  }

  char getCharacter() {
    return character.charAt(0);
  }

  Color getForeground() {
    return new TrueColor(Integer.decode(foreground));
  }

  Color getBackground() {
    if (background == null) {
      return getForeground().darker();
    }

    return new TrueColor(Integer.decode(background));
  }

  String getName() {
    if (name == null) {
      return "";
    }

    return name;
  }

  String getDescription() {
    if (description == null) {
      return "";
    }

    return description;
  }

  boolean isBarrier() {
    return barrier;
  }

  boolean isAlwaysOnTop() {
    if (alwaysOnTop == null) {
      return false;
    }

    return alwaysOnTop;
  }

  Tile makeTile() {
    final Tile tile = new Tile(new ConsoleGraphic(getCharacter(), getForeground(), getBackground()), getName(), getDescription(), isBarrier(), isAlwaysOnTop());
    ItemFactory.populateInventory(tile, fixedItems, randomItems);

    return tile;
  }
}
