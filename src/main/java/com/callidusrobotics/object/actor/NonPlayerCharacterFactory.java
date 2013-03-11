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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.InvalidKeyException;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.XmlMarshaller;

/**
 * Factory for mapping unique identifiers to NonPlayerCharacterData objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see NonPlayerCharacterData
 */
public final class NonPlayerCharacterFactory {
  private static transient final Map<String, NonPlayerCharacterData> NPC_MAP = new HashMap<String, NonPlayerCharacterData>();

  private NonPlayerCharacterFactory() {
    throw new NotInstantiableError();
  }

  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized void clearFactory() {
    NPC_MAP.clear();
  }

  @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
  public static synchronized void initializeFactory(final String xmlFile) throws IOException {
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(NonPlayerCharacterList.class);
    final NonPlayerCharacterList npcList = (NonPlayerCharacterList) xmlMarshaller.unmarshalSystemResource(xmlFile);

    for (final NonPlayerCharacterData npcData : npcList.npcs) {
      if (NPC_MAP.containsKey(npcData.getId())) {
        throw new DuplicateKeyException("Encountered duplicate NonPlayerCharacterData ID (" + npcData.getId() + ") in " + xmlFile);
      }

      NPC_MAP.put(npcData.getId(), npcData);
    }
  }

  public static NonPlayerCharacterData getNonPlayerCharacterData(final String identifier) {
    if (!NPC_MAP.containsKey(identifier)) {
      throw new InvalidKeyException("Encountered unknown NonPlayerCharacter ID (" + identifier + ")");
    }

    return NPC_MAP.get(identifier);
  }

  public static NonPlayerCharacter makeNpc(final String identifier, final Coordinate position) {
    if (!NPC_MAP.containsKey(identifier)) {
      throw new InvalidKeyException("Encountered unknown NonPlayerCharacter ID (" + identifier + ")");
    }

    return NPC_MAP.get(identifier).makeNpc(position);
  }

  public static Set<String> getIdentifiers() {
    return NPC_MAP.keySet();
  }
}
