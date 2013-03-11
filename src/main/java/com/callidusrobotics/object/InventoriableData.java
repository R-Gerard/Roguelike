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

import javax.xml.bind.annotation.XmlElement;

import com.callidusrobotics.attribute.Inventoriable;

/**
 * Helper POJO for ItemData.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Inventoriable
 * @see ItemData
 */
public class InventoriableData {
  @XmlElement(required = true) protected String name, description;
  @XmlElement(required = true) protected int maxPerSlot;
  @XmlElement(required = true) protected int value;

  InventoriableData() { /* Required by JAXB */ }

  public InventoriableData(final String name, final String description, final int maxPerSlot, final int value) {
    this.name = name;
    this.description = description;
    this.maxPerSlot = maxPerSlot;
    this.value = value;
  }

  public InventoriableData(final InventoriableData other) {
    name = other.name;
    description = other.description;
    maxPerSlot = other.maxPerSlot;
    value = other.value;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getMaxPerSlot() {
    return maxPerSlot;
  }

  public int getValue() {
    return value;
  }
}
