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

import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.StringUtils;

import com.callidusrobotics.util.Identifier;

/**
 * Helper POJO for describing inventory items in an AbstractGameObject.
 *
 * @author Rusty
 * @since 0.0.1
 * @see AbstractGameObject
 */
public class GameObjectItemData {
  @XmlAttribute(required = true, name = "id") String identifier;
  @XmlAttribute(required = true) int fixedQuantity;
  @XmlAttribute(required = false) String randomProbability;
  @XmlAttribute(required = false) String randomQuantity;

  GameObjectItemData() { /* Required by JAXB */ }

  public GameObjectItemData(final String identifier, final int fixedQuantity, final Integer randomProbability, final Integer randomQuantity) {
    this.identifier = identifier;
    this.randomProbability = randomProbability == null ? null : Integer.toString(randomProbability);
    this.randomQuantity = randomQuantity == null ? null : Integer.toString(randomQuantity);
    this.fixedQuantity = fixedQuantity;
  }

  public String getId() {
    return Identifier.string2identifier(identifier);
  }

  public int getRandomProbability() {
    if (StringUtils.isBlank(randomProbability)) {
      return -1;
    }

    return Integer.parseInt(randomProbability);
  }

  public int getQuantity() {
    final int max = ItemFactory.makeItem(identifier, 1).getMaxPerSlot();
    int quantity = fixedQuantity;

    if (!StringUtils.isBlank(randomQuantity)) {
      final int num = Integer.parseInt(randomQuantity);
      final Random random = new Random();
      quantity += random.nextInt(Math.max(1, num));
    }

    if (max > 0) {
      return Math.min(max, quantity);
    } else {
      return quantity;
    }
  }
}
