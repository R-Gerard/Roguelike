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

import org.apache.commons.lang3.StringUtils;

import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.attribute.Useable;
import com.callidusrobotics.object.actor.StatBlock;

/**
 * Helper POJO for ItemData.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Useable
 */
public class UseableData {
  @XmlElement(required = true) boolean disposable, loadable;
  @XmlElement(required = true) int maximumCapacity;
  @XmlElement(required = false) Integer reloadSpeed;
  @XmlElement(required = true) String useMessage;
  @XmlElement(required = false) String ammunitionType;
  @XmlElement(required = false) StatBlock statModifiers;

  private transient Inventoriable ammoRef = null;

  UseableData() { /* Required by JAXB */ }

  public UseableData(final boolean disposable, final boolean loadable, final int maximumCapacity, final Integer reloadSpeed, final String useMessage, final String ammunitionType, final StatBlock statModifiers) {
    this.disposable = disposable;
    this.loadable = loadable;
    this.useMessage = StringUtils.isBlank(useMessage) ? "You don't know how to use this." : useMessage;
    this.maximumCapacity = maximumCapacity;
    this.reloadSpeed = reloadSpeed;
    this.ammunitionType = ammunitionType;
    this.statModifiers = statModifiers;
  }

  public UseableData(final UseableData other) {
    disposable = other.disposable;
    loadable = other.loadable;
    useMessage = other.useMessage;
    maximumCapacity = other.maximumCapacity;
    reloadSpeed = other.reloadSpeed;
    ammunitionType = other.ammunitionType;
    statModifiers = new StatBlock(other.statModifiers);
  }

  public int getReloadSpeed() {
    if (!loadable) {
      return 0;
    }

    if (reloadSpeed == null || reloadSpeed == 0) {
      return 1;
    }

    return reloadSpeed;
  }

  public StatBlock getStatBlock() {
    if (statModifiers == null) {
      statModifiers = new StatBlock(0, 0, 0, 0, 0, 0, 0);
    }

    return statModifiers;
  }

  public Inventoriable getAmmunitionType() {
    if (!StringUtils.isBlank(ammunitionType) && ammoRef == null) {
      ammoRef = ItemFactory.makeItem(ammunitionType, 0);
    }

    return ammoRef;
  }
}
