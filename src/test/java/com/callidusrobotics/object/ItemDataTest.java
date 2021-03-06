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

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.callidusrobotics.util.XmlMarshaller;

public class ItemDataTest {
  final XmlMarshaller marshaller = new XmlMarshaller(ItemData.class);
  ItemData data;

  @After
  public void after() throws Exception {
    final String xml = marshaller.marshal(data);
    final ItemData data2 = (ItemData) marshaller.unmarshal(xml);

    Assert.assertNotNull(data2);
    //System.out.println(xml);
  }

  @Test
  public void makeItemData() throws Exception {
    data = ItemUtil.makeBreadcrumb();
  }
}
