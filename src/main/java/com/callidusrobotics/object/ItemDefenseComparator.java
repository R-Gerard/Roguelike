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

import com.callidusrobotics.object.actor.StatBlock;

/**
 * Compares Item instances by their combat defense.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Item#getStatBlock()
 * @see StatBlock#getDefense()
 */
public class ItemDefenseComparator extends AbstractItemComparator {
  private static final long serialVersionUID = 6665297797323574455L;

  @Override
  public int getRank(final Item item) {
    if (item == null) {
      return Integer.MIN_VALUE;
    }

    final StatBlock statBlock = item.getStatBlock();
    return statBlock.getDefense();
  }
}
