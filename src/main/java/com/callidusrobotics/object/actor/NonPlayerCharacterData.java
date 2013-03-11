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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.callidusrobotics.attribute.Named;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.object.GameObjectItemData;
import com.callidusrobotics.object.ItemFactory;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.Identifier;
import com.callidusrobotics.util.TrueColor;

/**
 * POJO for marshalling and unmarshalling NonPlayerCharacter objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see NonPlayerCharacter
 */
@XmlRootElement
public final class NonPlayerCharacterData implements Named {
  static class WoundDescriptions {
    @XmlElement(required = true) String fullHp;
    @XmlElement(required = true) String grazed;
    @XmlElement(required = true) String wounded;
    @XmlElement(required = true) String severelyWounded;
    @XmlElement(required = true) String nearlyDead;

    WoundDescriptions() { /* Required by JAXB */ }

    WoundDescriptions(final String fullHp, final String grazed, final String wounded, final String severelyWounded, final String nearlyDead) {
      this.fullHp = fullHp;
      this.grazed = grazed;
      this.wounded = wounded;
      this.severelyWounded = severelyWounded;
      this.nearlyDead = nearlyDead;
    }

    List<String> getDescriptions() {
      return Arrays.asList(nearlyDead, severelyWounded, wounded, grazed, fullHp);
    }
  }

  static class NameInfo {
    @XmlAttribute(required = true, name = "display") String displayName;
    @XmlAttribute(required = false) Boolean properNoun;

    NameInfo() { /* Required by JAXB */ }

    NameInfo(final String displayName, final Boolean properNoun) {
      this.displayName = displayName;
      this.properNoun = properNoun;
    }

    boolean isProperNoun() {
      if (properNoun == null) {
        return false;
      }

      return properNoun;
    }
  }

  static class GameDisplayInfo {
    @XmlElement(required = true) String character;
    @XmlElement(required = true) String foreground;
    @XmlElement(required = true, name = "name") NameInfo nameInfo;
    @XmlElement(required = false) String description;
    @XmlElement(required = true) String size;
    @XmlElement(required = false) WoundDescriptions woundDescriptions;

    GameDisplayInfo() { /* Required by JAXB */ }

    GameDisplayInfo(final char character, final TrueColor foreground, final String name, final boolean properNoun, final String description, final Size size, final WoundDescriptions woundDescriptions) {
      this.character = Character.toString(character);
      this.foreground = foreground.toString();
      this.nameInfo = properNoun ? new NameInfo(name, true) : new NameInfo(name, null);
      this.description = description;
      this.size = size.toString();
      this.woundDescriptions = woundDescriptions;
    }
  }

  // Flatten Coordinate/MutableCoordinate into a simple POJO for XML marshalling
  static class StartingPosition {
    @XmlAttribute(required = true) int row, col;

    StartingPosition() { /* Required by JAXB */ }

    StartingPosition(final Coordinate coordinate) {
      this.row = coordinate.getRow();
      this.col = coordinate.getCol();
    }
  }

  @XmlAttribute(required = true, name = "id") String identifier;
  @XmlElement(required = true) GameDisplayInfo displayInfo;
  @XmlElement(required = true) String faction;
  @XmlElement(required = true) StatBlock statBlock;
  @XmlElement(required = false) StartingPosition startingPosition;
  @XmlElement(required = false) Boolean leavesCorpse;
  @XmlElementWrapper(required = true) @XmlElement(name = "behavior") List<NpcAiData> behaviors;
  @XmlElementWrapper(required = false) @XmlElement(name = "message") List<String> messages;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> fixedItems;
  @XmlElementWrapper(required = false) @XmlElement(name = "item") List<GameObjectItemData> randomItems;

  NonPlayerCharacterData() { /* Required by JAXB */ }

  public NonPlayerCharacterData(final String identifier, final GameDisplayInfo gameDisplayInfo, final String faction, final StatBlock statBlock, final Coordinate startingPosition, final Boolean leavesCorpse, final List<AiStrategy> aiStrategies) {
    // The identifier must be unique, but the name we display in-game does not
    this.identifier = identifier == null ? gameDisplayInfo.nameInfo.displayName : identifier;
    this.displayInfo = gameDisplayInfo;
    this.faction = faction;
    this.statBlock = statBlock;
    this.startingPosition = startingPosition == null ? null : new StartingPosition(startingPosition);
    this.leavesCorpse = leavesCorpse;

    setAiStrategies(aiStrategies);
  }

  @Override
  public String getId() {
    return Identifier.string2identifier(identifier);
  }

  char getCharacter() {
    return displayInfo.character.charAt(0);
  }

  @Override
  public String getName() {
    if (displayInfo.nameInfo == null) {
      return "";
    }

    return displayInfo.nameInfo.displayName;
  }

  public boolean nameIsProperNoun() {
    if (displayInfo.nameInfo == null) {
      return false;
    }

    return displayInfo.nameInfo.isProperNoun();
  }

  @Override
  public String getDescription() {
    if (displayInfo.description == null) {
      return "";
    }

    return displayInfo.description;
  }

  public Size getSize() {
    return Size.valueOf(displayInfo.size);
  }

  public String getFaction() {
    return faction;
  }

  public Coordinate getStartingPosition() {
    if (startingPosition == null) {
      return null;
    }

    return new Coordinate(startingPosition.row, startingPosition.col);
  }

  Color getForeground() {
    return new TrueColor(Integer.decode(displayInfo.foreground));
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  void setAiStrategies(final List<AiStrategy> aiStrategies) {
    behaviors = new ArrayList<NpcAiData>(Behavior.values().length);
    for (final AiStrategy aiStrategy : aiStrategies) {
      final AbstractAiStrategy strategy = (AbstractAiStrategy) aiStrategy;
      behaviors.add(new NpcAiData(strategy.getBehavior().toString(), strategy.min, strategy.max, strategy.base, strategy.multiplier));
    }
  }

  public void setMessages(final List<String> messages) {
    this.messages = messages;
  }

  public void setFixedItems(final List<GameObjectItemData> fixedItems) {
    this.fixedItems = fixedItems;
  }

  public void setRandomItems(final List<GameObjectItemData> randomItems) {
    this.randomItems = randomItems;
  }

  public NonPlayerCharacter makeNpc(final Coordinate position) {
    final List<AiStrategy> aiStrategies = new ArrayList<AiStrategy>(Behavior.values().length);
    for (final NpcAiData aiData : behaviors) {
      final AiStrategy aiStrategy = aiData.getAiStrategy(null);
      if (aiStrategy != null) {
        aiStrategies.add(aiStrategy);
      }
    }

    final NonPlayerCharacter npc = new NonPlayerCharacter(getId(), new ConsoleGraphic(getCharacter(), getForeground(), null), aiStrategies, position, getName(), getDescription(), getSize(), faction, statBlock);
    ItemFactory.populateInventory(npc, fixedItems, randomItems);
    npc.setMessages(messages);
    npc.setIsProperNoun(nameIsProperNoun());

    if (displayInfo.woundDescriptions != null) {
      npc.setWoundDescriptions(displayInfo.woundDescriptions.getDescriptions());
    }

    if (leavesCorpse != null) {
      npc.setLeaveCorpse(leavesCorpse);
    }

    return npc;
  }
}
