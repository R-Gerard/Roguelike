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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.callidusrobotics.attribute.Equipable;
import com.callidusrobotics.object.actor.StatBlock;

/**
 * Helper POJO for ItemData.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Equipable
 */
public class EquipableData {
  @XmlElement(required = true) int range, damage;
  @XmlElement(required = false) StatBlock statBlock;
  @XmlElementWrapper(required = false) @XmlElement(name = "equipmentSlot") List<EquipmentSlot> equipmentSlots;

  EquipableData() { /* Required by JAXB */ }

  public EquipableData(final int range, final int damage, final StatBlock statBlock, final List<EquipmentSlot> equipmentSlots) {
    this.range = range;
    this.damage = damage;
    this.statBlock = statBlock;
    this.equipmentSlots = equipmentSlots;
  }

  public EquipableData(final EquipableData other) {
    range = other.range;
    damage = other.damage;
    statBlock = new StatBlock(other.statBlock);
    equipmentSlots = new ArrayList<EquipmentSlot>(EquipmentSlot.values().length);
    for (final EquipmentSlot slot : other.getEquipmentSlots()) {
      equipmentSlots.add(slot);
    }
  }

  public StatBlock getStatBlock() {
    if (statBlock == null) {
      return new StatBlock(0, 0, 0, 0, 0, 0, 0);
    }

    return statBlock;
  }

  public List<EquipmentSlot> getEquipmentSlots() {
    if (equipmentSlots == null) {
      return Collections.emptyList();
    }

    return equipmentSlots;
  }
}
