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

package com.callidusrobotics.object.actor;

import java.awt.Color;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.ItemFactory;
import com.callidusrobotics.util.TrueColor;

/**
 * POJO for marshalling and unmarshalling PlayerCharacter objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see PlayerCharacter
 */
@XmlRootElement
public final class PlayerCharacterData {
  @XmlElement(required = true) String foreground;
  @XmlElement(required = true) StatBlock statBlock;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> fixedItems;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> randomItems;

  PlayerCharacterData() { /* Required by JAXB */ }

  public PlayerCharacterData(final TrueColor foreground, final StatBlock statBlock, final List<GameObjectItemData> fixedItems, final List<GameObjectItemData> randomItems) {
    this.foreground = foreground.toString();
    this.statBlock = statBlock;
    this.fixedItems = fixedItems;
    this.randomItems = randomItems;
  }

  Color getForeground() {
    return new TrueColor(Integer.decode(foreground));
  }

  public PlayerCharacter makePlayerCharacter(final Coordinate position) {
    final PlayerCharacter player = new PlayerCharacter(getForeground(), statBlock, position);
    ItemFactory.populateInventory(player, fixedItems, randomItems);

    return player;
  }
}
