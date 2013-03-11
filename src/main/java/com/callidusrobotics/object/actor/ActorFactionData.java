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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

import com.callidusrobotics.util.XmlMarshaller;

/**
 * Maintains mappings of NonPlayerCharacter factions with respect to other
 * NonPlayerCharacter factions and the PlayerCharacter.
 *
 * @author Rusty
 * @since 0.0.1
 */
@XmlRootElement
public final class ActorFactionData {
  public static final String PLAYER_FACTION = "player";
  public static final String UNIQUE_FACTION = "UNIQUE";

  @SuppressWarnings("PMD.AvoidDuplicateLiterals")
  static class ActorFactionElement {
    @XmlAttribute(name = "id") String factionName;
    @XmlElement(name = "faction") @XmlElementWrapper(required = false) private List<String> enemies;
    @XmlElement(name = "faction") @XmlElementWrapper(required = false) private List<String> neutrals;
    @XmlElement(name = "faction") @XmlElementWrapper(required = false) private List<String> allies;

    ActorFactionElement() { /* Required by JAXB */ }

    ActorFactionElement(final String faction, final List<String> enemies, final List<String> neutrals, final List<String> allies) {
      this.factionName = faction;
      this.enemies = enemies;
      this.neutrals = neutrals;
      this.allies = allies;
    }

    List<String> getEnemies() {
      if (enemies == null) {
        enemies = Collections.emptyList();
      }

      return enemies;
    }

    List<String> getNeutrals() {
      if (neutrals == null) {
        neutrals = Collections.emptyList();
      }

      return neutrals;
    }

    List<String> getAllies() {
      if (allies == null) {
        allies = Collections.emptyList();
      }

      return allies;
    }
  }

  // The sparse matrix
  @XmlElementWrapper(required = true) @XmlElement(name = "faction") List<ActorFactionElement> factions;

  // The full matrix
  private transient Map<String, Map<String, Relationship>> factionMap;

  /**
   * Marshalls a relationship map from an XML file.
   *
   * @param xmlFile
   *          The file to marshall, not null
   * @return The relationship map, never null
   * @throws IOException
   *           If the xmlFile can not be marshalled
   */
  public static ActorFactionData fromFile(final String xmlFile) throws IOException {
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(ActorFactionData.class);

    return (ActorFactionData) xmlMarshaller.unmarshalSystemResource(xmlFile);
  }

  /**
   * Generates a new, unique faction name.
   *
   * @return The new faction name, never null
   */
  public String getUniqueFaction() {
    final UUID uuid = UUID.randomUUID();
    final String faction = "faction_" + uuid.toString();

    setRelationship(faction, faction, Relationship.ALLY);
    return faction;
  }

  /**
   * Initializes the internal state. Call this method before the first use.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void init() {
    factionMap = new HashMap<String, Map<String, Relationship>>();

    final String[] keySet = keySet();
    for (final String faction : keySet) {
      final Map<String, Relationship> relationships = new HashMap<String, Relationship>();
      factionMap.put(faction, relationships);

      // All factions are neutral to each other by default
      for (final String faction2 : keySet) {
        relationships.put(faction2, Relationship.NEUTRAL);
      }

      // Members of the same faction are always allies
      relationships.put(faction, Relationship.ALLY);
    }

    for (final ActorFactionElement element : factions) {
      for (final String enemy : element.getEnemies()) {
        Validate.isTrue(!element.factionName.equals(enemy), "members of faction '" + enemy + "' can not be enemies with each other");
        factionMap.get(element.factionName).put(enemy, Relationship.ENEMY);
        factionMap.get(enemy).put(element.factionName, Relationship.ENEMY);
      }

      for (final String ally : element.getAllies()) {
        factionMap.get(element.factionName).put(ally, Relationship.ALLY);
        factionMap.get(ally).put(element.factionName, Relationship.ALLY);
      }
    }

    validateFactions();
  }

  /**
   * Relationship accessor.
   *
   * @param faction1
   *          The first faction to test, not null
   * @param faction2
   *          The second faction to test, not null
   * @return The relationship between the two factions, never null
   */
  public Relationship getRelationship(final String faction1, final String faction2) {
    Validate.notNull(faction1);
    Validate.notEmpty(faction1);
    Validate.notNull(faction2);
    Validate.notEmpty(faction2);

    if (factionMap.containsKey(faction1) && factionMap.containsKey(faction2)) {
      return factionMap.get(faction1).get(faction2);
    }

    return Relationship.NEUTRAL;
  }

  /**
   * Tests if two factions are enemies.
   *
   * @param faction1
   *          The first faction to test, not null
   * @param faction2
   *          The second faction to test, not null
   * @return True if the factions are enemies
   */
  public boolean areEnemies(final String faction1, final String faction2) {
    return getRelationship(faction1, faction2) == Relationship.ENEMY;
  }

  /**
   * Tests if two factions are allies.
   *
   * @param faction1
   *          The first faction to test, not null
   * @param faction2
   *          The second faction to test, not null
   * @return True if the factions are allies
   */
  public boolean areAllies(final String faction1, final String faction2) {
    return getRelationship(faction1, faction2) == Relationship.ALLY;
  }

  /**
   * Tests if two factions are neutral.
   *
   * @param faction1
   *          The first faction to test, not null
   * @param faction2
   *          The second faction to test, not null
   * @return True if the factions are neutral
   */
  public boolean areNeutrals(final String faction1, final String faction2) {
    return getRelationship(faction1, faction2) == Relationship.NEUTRAL;
  }

  /**
   * Relationship mutator.
   *
   * @param faction1
   *          The first faction to set, not null
   * @param faction2
   *          The second faction to set, not null
   * @param relationship
   *          The new relationship between the factions, not null
   */
  public void setRelationship(final String faction1, final String faction2, final Relationship relationship) {
    Validate.notNull(faction1);
    Validate.notEmpty(faction1);
    Validate.notNull(faction2);
    Validate.notEmpty(faction2);

    Validate.notNull(relationship);

    if (!factionMap.containsKey(faction1)) {
      factionMap.put(faction1, new HashMap<String, Relationship>());
    }

    if (!factionMap.containsKey(faction2)) {
      factionMap.put(faction2, new HashMap<String, Relationship>());
    }

    factionMap.get(faction1).put(faction2, relationship);
    factionMap.get(faction2).put(faction1, relationship);
  }

  private String[] keySet() {
    final Set<String> keySet = new HashSet<String>();
    for (final ActorFactionElement element : factions) {
      Validate.notBlank(element.factionName, "NpcFactionElement ID must not be empty");
      keySet.add(element.factionName);

      for (final String enemy : element.getEnemies()) {
        Validate.notBlank(enemy, "NpcFactionElement enemies list must not contain blank entries");
        keySet.add(enemy);
      }

      for (final String neutral : element.getNeutrals()) {
        Validate.notBlank(neutral, "NpcFactionElement neutrals list must not contain blank entries");
        keySet.add(neutral);
      }

      for (final String ally : element.getAllies()) {
        Validate.notBlank(ally, "NpcFactionElement allies list must not contain blank entries");
        keySet.add(ally);
      }
    }

    return keySet.toArray(new String[keySet.size()]);
  }

  private void validateFactions() {
    // Validate that the sparse matrix of faction data is internally consistent
    for (final String identifier : NonPlayerCharacterFactory.getIdentifiers()) {
      final String faction = NonPlayerCharacterFactory.getNonPlayerCharacterData(identifier).getFaction();
      Validate.isTrue(factionMap.containsKey(faction), "Encountered unknown faction '" + faction + "'.");
    }
  }
}
