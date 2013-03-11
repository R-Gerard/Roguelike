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

import java.util.Arrays;
import java.util.Collections;

import javax.xml.bind.annotation.XmlElement;

/**
 * Helper POJO for DungeonParameters.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonMapData
 */
class NpcPopulationParameters {
  static class MinMaxTuple {
    @XmlElement int min, max;
  }

  @XmlElement(required = true) int populationCap;
  @XmlElement(required = true) MinMaxTuple spawnParameters = new MinMaxTuple();
  @XmlElement(required = true) MinMaxTuple respawnParameters = new MinMaxTuple();

  NpcPopulationParameters() { /* Required by JAXB */ }

  NpcPopulationParameters(final int npcCap, final int spawnMin, final int spawnMax, final int respawnMin, final int respawnMax) {
    this.populationCap = npcCap;
    spawnParameters.min = Math.max(0, Math.min(spawnMin, spawnMax));
    spawnParameters.max = Collections.max(Arrays.asList(0, spawnMin, spawnMax));
    respawnParameters.min = Math.max(0, Math.min(respawnMin, respawnMax));
    respawnParameters.max = Collections.max(Arrays.asList(0, respawnMin, respawnMax));
  }
}
