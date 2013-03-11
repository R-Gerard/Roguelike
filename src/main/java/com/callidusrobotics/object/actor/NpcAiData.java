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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Helper POJO for NonPlayerCharacterData objects to encapsulate AiStrategy
 * behaviors.
 *
 * @author Rusty
 * @since 0.0.1
 * @see NonPlayerCharacterData
 * @see AiStrategy
 */
class NpcAiData {
  @XmlAttribute(required = true, name = "type") String behavior;
  @XmlAttribute(required = true) int min;
  @XmlAttribute(required = true) int max;
  @XmlAttribute(required = true) int base;
  @XmlAttribute(required = true) int multiplier;

  NpcAiData() { /* Required by JAXB */ }

  NpcAiData(final String behavior, final int min, final int max, final int base, final int multiplier) {
    this.behavior = behavior;
    this.min = min;
    this.max = max;
    this.base = base;
    this.multiplier = multiplier;
  }

  Behavior getBehavior() {
    return Behavior.valueOf(behavior);
  }

  AiStrategy getAiStrategy(final NonPlayerCharacter npc) {
    return AiStrategyFactory.makeAiStrategy(getBehavior(), npc, min, max, base, multiplier);
  }
}
