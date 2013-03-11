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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.XmlMarshaller;

public class ItemListTest {
  final XmlMarshaller marshaller = new XmlMarshaller(ItemList.class);
  ItemList data;

  @Before
  public void before() throws Exception {
    data = new ItemList();
  }

  @After
  public void after() throws Exception {
    System.out.println(marshaller.marshal(data));

    // Load all of the ItemDatas into a map to check for duplicate IDs
    final Map<String, ItemData> itemMap = new HashMap<String, ItemData>();
    for (final ItemData itemData : data.items) {
      if (itemMap.containsKey(itemData.getId())) {
        throw new DuplicateKeyException("Encountered duplicate ItemData ID (" + itemData.getId() + ")");
      }

      itemMap.put(itemData.getId(), itemData);
    }

    // Generate one of every item to ensure that there are no marshalling errors
    for (final ItemData itemData : itemMap.values()) {
      itemData.makeItem(1);
    }
  }

  @Test
  public void makeDemoList() throws Exception {
    data.identifier = "demo";
    data.items = Arrays.asList(
        ItemUtil.makeBreadcrumb(),
        ItemUtil.makeMoney(),
        ItemUtil.makePocketKnife(),
        ItemUtil.makeServiceRevolver(),
        ItemUtil.make38Ammo(),
        ItemUtil.makeHealingStone());
  }
}
