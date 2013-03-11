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

import java.io.Serializable;

abstract class AbstractItemComparator implements ItemComparator, Serializable {
  private static final long serialVersionUID = -4217166793298420020L;

  @SuppressWarnings("PMD.CompareObjectsWithEquals")
  @Override
  public int compare(final Item item1, final Item item2) {
    if (item1 == item2) {
      return 0;
    }

    final int rank1 = getRank(item1);
    final int rank2 = getRank(item2);

    if (rank1 > rank2) {
      return 1;
    }

    if (rank2 > rank1) {
      return -1;
    }

    return 0;
  }
}
