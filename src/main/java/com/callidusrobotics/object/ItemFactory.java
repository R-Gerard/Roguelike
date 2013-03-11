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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.InvalidKeyException;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.XmlMarshaller;

public final class ItemFactory {
  private static transient final Map<String, ItemData> ITEM_MAP = new HashMap<String, ItemData>();

  private ItemFactory() {
    throw new NotInstantiableError();
  }

  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized void clearFactory() {
    ITEM_MAP.clear();
  }

  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized void initializeFactory(final String xmlFile) throws IOException {
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(ItemList.class);
    final ItemList itemList = (ItemList) xmlMarshaller.unmarshalSystemResource(xmlFile);

    for (final ItemData itemData : itemList.items) {
      if (ITEM_MAP.containsKey(itemData.getId())) {
        throw new DuplicateKeyException("Encountered duplicate ItemData ID (" + itemData.getId() + ") in " + xmlFile);
      }

      ITEM_MAP.put(itemData.getId(), itemData);
    }
  }

  public static Item makeItem(final String identifier, final int quantity) {
    if (!ITEM_MAP.containsKey(identifier)) {
      throw new InvalidKeyException("Encountered unknown Item (" + identifier + ")");
    }

    return ITEM_MAP.get(identifier).makeItem(quantity);
  }

  public static void populateInventory(final AbstractGameObject object, final List<GameObjectItemData> fixedItems, final List<GameObjectItemData> randomItems) {
    if (fixedItems != null) {
      for (final GameObjectItemData fixedItemData : fixedItems) {
        object.addItemToInventory(ItemFactory.makeItem(fixedItemData.getId(), fixedItemData.getQuantity()));
      }
    }

    if (randomItems != null) {
      final Random random = new Random();
      for (final GameObjectItemData randomItemData : randomItems) {
        if (random.nextInt(100) < randomItemData.getRandomProbability()) {
          object.addItemToInventory(ItemFactory.makeItem(randomItemData.getId(), randomItemData.getQuantity()));
        }
      }
    }
  }
}
