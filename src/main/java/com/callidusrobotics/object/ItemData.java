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

package com.callidusrobotics.object;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.Identifier;
import com.callidusrobotics.util.TrueColor;

/**
 * POJO for marshalling and unmarshalling Item objects.
 *
 * @author Rusty
 * @since 0.0.1
 */
@XmlRootElement
public final class ItemData {
  @XmlAttribute(required = true, name = "id") String identifier;
  @XmlElement(required = true) String character;
  @XmlElement(required = true) String color;
  @XmlElement(required = true) InventoriableData inventoriableData;
  @XmlElement(required = false) UseableData useableData;
  @XmlElement(required = false) EquipableData equipableData;

  ItemData() { /* Required by JAXB */ }

  public ItemData(final String identifier, final char character, final TrueColor foreground, final InventoriableData inventoriableData, final UseableData useableData, final EquipableData equipableData) {
    this.identifier = identifier;
    this.character = Character.toString(character);
    this.color = foreground.toString();
    this.inventoriableData = inventoriableData;
    this.useableData = useableData;
    this.equipableData = equipableData;
  }

  public String getId() {
    return Identifier.string2identifier(identifier);
  }

  char getCharacter() {
    return character.charAt(0);
  }

  Color getForeground() {
    return new TrueColor(Integer.decode(color));
  }

  InventoriableData getInventoriableData() {
    return inventoriableData;
  }

  UseableData getUseableData() {
    if (useableData == null) {
      return new UseableData(false, false, -1, null, null, null, null);
    }

    return useableData;
  }

  EquipableData getEquipableData() {
    if (equipableData == null) {
      return new EquipableData(0, 0, null, null);
    }

    return equipableData;
  }

  public Item makeItem(final int quantity) {
    return new Item(identifier, getCharacter(), new ConsoleGraphic(getCharacter(), getForeground(), null), quantity, getInventoriableData(), getUseableData(), getEquipableData());
  }
}
