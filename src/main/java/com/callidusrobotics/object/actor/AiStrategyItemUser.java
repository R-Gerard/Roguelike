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

import org.apache.commons.lang.StringUtils;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.Item;

/**
 * Defines behavior for a NonPlayerCharacter that is able to use items.
 *
 * @author Rusty
 * @since 0.0.3
 */
@Behaves(Behavior.ITEM_USER)
public class AiStrategyItemUser extends AbstractAiStrategy {
  private Item healingItem = null;

  @Override
  protected Message updateStateDelegate(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    if (healingItem != null) {
      healingItem.use(self, currentLevel);
      return new Message(Command.USE, null, StringUtils.capitalize(self.getNameThirdPerson()) + " used a " + healingItem.getName() + ".", self.getForeground(), null);
    }

    // TODO: Use other items when they are advantageous
    return new Message(Command.REST, null, null, null, null);
  }

  @Override
  protected int getAbsoluteWeight(final ActorFactionData factionData, final DungeonLevel currentLevel) {
    healingItem = getHealingItem();
    if (healingItem != null) {
      final int percentHp = getPercentHpRemaining();
      if (percentHp <= 25) {
        return max;
      }

      return base + multiplier * (100 - percentHp);
    }

    return min;
  }

  private Item getHealingItem() {
    final int hpRequired = getHpRequired();

    for (final Inventoriable inventoriable : self.getInventory()) {
      final Item item = (Item) inventoriable;
      final int hpBoost = item.isDisposable() ? item.getStatBlock().getCurrentHp() : 0;

      if (hpBoost > 0 && hpBoost <= hpRequired) {
        return item;
      }
    }

    return null;
  }

  private int getHpRequired() {
    final int percentHp = getPercentHpRemaining();

    // If we are critically low on HP, any healing item will do
    if (percentHp <= 25) {
      return Integer.MAX_VALUE;
    }

    // Be frugal and use a healing item that only heals the amount we require
    final StatBlock statBlock = self.getCurrentStatBlock();
    return statBlock.getMaxHp() - statBlock.getCurrentHp();
  }
}
