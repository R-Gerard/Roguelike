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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.Message;
import com.callidusrobotics.attribute.Inventoriable;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.object.Size;
import com.callidusrobotics.swing.ConsoleGraphic;
import com.callidusrobotics.util.TrueColor;

/**
 * Implementation of Actor that uses a collection of AiStrategy objects to
 * control itself.
 *
 * @author Rusty
 * @since 0.0.1
 * @see AiStrategy
 */
public class NonPlayerCharacter extends AbstractActor {
  protected final Random random = new Random();
  protected List<String> messages;
  protected List<AiStrategy> aiStrategies;
  protected String identifier;

  public NonPlayerCharacter(final String identifier, final ConsoleGraphic consoleGraphic, final List<AiStrategy> aiStrategies, final Coordinate position, final String name, final String description, final Size size, final String faction, final StatBlock statBlock) {
    super(consoleGraphic, position, name, description, size, faction, statBlock);

    this.identifier = identifier;
    this.aiStrategies = aiStrategies;
    this.messages = Arrays.asList("\"...\"");

    Validate.notNull(aiStrategies);
    Validate.isTrue(!aiStrategies.isEmpty());

    for (final AiStrategy aiStrategy : aiStrategies) {
      ((AbstractAiStrategy) aiStrategy).setSelf(this);
    }
  }

  public List<String> getMessages() {
    return Collections.unmodifiableList(messages);
  }

  public void setMessages(final List<String> messages) {
    this.messages = messages == null ? Arrays.asList("\"...\"") : messages;
  }

  @Override
  public String getId() {
    // There can be more than one NonPlayerCharacter with the same display-name but different StatBlocks and AiStrategies
    return identifier;
  }

  @Override
  public String getDescription() {
    final String behavior = StringUtils.capitalize(aiStrategies.get(0).getBehavior().getDescription());
    if (!StringUtils.isBlank(behavior)) {
      return super.getDescription() + " (" + behavior + ")";
    }

    return super.getDescription();
  }

  @Override
  public boolean canAddItemToInventory(final Inventoriable item) {
    // NonPlayerCharacters have unlimited storage capacity
    return true;
  }

  @Override
  public Message processCommand(final DungeonLevel currentLevel, final Command command) {
    if (command == Command.CHAT && !messages.isEmpty()) {
      return new Message(command, null, messages.get(random.nextInt(messages.size())), getForeground(), TrueColor.BLACK);
    }

    return super.processCommand(currentLevel, command);
  }

  public Message act(final ActorFactionData factionData, final DungeonLevel currentLevel, final PlayerCharacter player) {
    // The GameMediator calls act on all NPCs, even if they are dead (dead NPCs are removed at the end of the turn)
    if (getCurrentStatBlock().getCurrentHp() < 1) {
      return new Message(Command.REST, null, null, null, null);
    }

    // Select the strategy that weighs itself highest given the current conditions
    int maxIndex = 0;
    int maxValue = aiStrategies.get(0).getWeight(factionData, currentLevel);

    for (int i = 1; i < aiStrategies.size(); i++) {
      final int weight = aiStrategies.get(i).getWeight(factionData, currentLevel);

      if (weight > maxValue) {
        maxValue = weight;
        maxIndex = i;
      }
    }

    // Reset all other strategies
    if (maxIndex > 0) {
      aiStrategies.add(0, aiStrategies.remove(maxIndex));
    }

    for (int i = 1; i < aiStrategies.size(); i++) {
      aiStrategies.get(i).init();
    }

    // Use the selected strategy to update this
    return aiStrategies.get(0).updateState(factionData, currentLevel);
  }

  @Override
  protected boolean isBarrier(final DungeonLevel currentLevel, final Coordinate position) {
    return !currentLevel.isVacantRelative(position);
  }
}
