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

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.ItemUtil;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.XmlMarshaller;

public class PlayerCharacterDataTest {
  final XmlMarshaller marshaller = new XmlMarshaller(PlayerCharacterData.class);
  PlayerCharacterData data;

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final PlayerCharacterData data2 = (PlayerCharacterData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    System.out.println(xml);
  }

  @Test
  public void makeDemoPlayerCharacter() throws Exception {
    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makeBreadcrumb().getId(), 450, null, 100),
        new GameObjectItemData(ItemUtil.make38Ammo().getId(), 45, null, 10),
        new GameObjectItemData(ItemUtil.makeServiceRevolver().getId(), 1, null, null));

    data = new PlayerCharacterData(TrueColor.STEEL_BLUE,
        new StatBlock(10, 10, 10, 10, 10, 10, 100),
        fixedItems,
        null);
  }

  @Test
  public void make7drlPlayerCharacter() throws Exception {
    final List<GameObjectItemData> fixedItems = Arrays.asList(
        new GameObjectItemData(ItemUtil.makePocketKnife().getId(), 1, null, null),
        new GameObjectItemData(ItemUtil.makeBreadcrumb().getId(), 450, null, 100),
        new GameObjectItemData(ItemUtil.make38Ammo().getId(), 45, null, 10),
        new GameObjectItemData(ItemUtil.makeServiceRevolver().getId(), 1, null, null),
        new GameObjectItemData(ItemUtil.makeJournal().getId(), 1, null, null));

    data = new PlayerCharacterData(TrueColor.STEEL_BLUE,
        new StatBlock(30, 30, 15, 15, 15, 15, 100),
        fixedItems,
        null);
  }
}
