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

package com.callidusrobotics;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.command.Command;
import com.callidusrobotics.command.CommandFactory;
import com.callidusrobotics.command.CommandMapper;
import com.callidusrobotics.locale.AreaMapData;
import com.callidusrobotics.locale.Coordinate;
import com.callidusrobotics.locale.Direction;
import com.callidusrobotics.locale.DungeonLevel;
import com.callidusrobotics.locale.Tile;
import com.callidusrobotics.object.ItemFactory;
import com.callidusrobotics.object.actor.AbstractActor;
import com.callidusrobotics.object.actor.ActorFactionData;
import com.callidusrobotics.object.actor.NonPlayerCharacter;
import com.callidusrobotics.object.actor.NonPlayerCharacterFactory;
import com.callidusrobotics.object.actor.PlayerCharacter;
import com.callidusrobotics.object.actor.PlayerCharacterData;
import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleFactory;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.XmlMarshaller;

/**
 * Driver class that maintains the game state and coordinates messages between
 * game objects.
 *
 * @author Rusty
 * @since 0.0.1
 * @see ConsoleColleague
 * @see CommandMapper
 */
public class GameMediator {
  private boolean gameRunning = true;
  private PlayerCharacter player;
  private DungeonLevel currentLevel;
  private final GameData gameData;

  private CommandFactory commandFactory;
  private ActorFactionData factionData;
  private final ConsoleColleague consoleColleague;
  private final List<Coordinate> updateList = new LinkedList<Coordinate>();

  public GameMediator(final Console console, final GameData gameData) {
    Validate.notNull(console);
    Validate.notNull(gameData);

    this.gameData = gameData;
    consoleColleague = new ConsoleColleague(console, null, gameData.getBoxForeground(), gameData.getBoxBackground(), gameData.getFontForeground(), gameData.getFontBackground(), gameData.getHighlightForeground(), gameData.getHighlightBackground());
  }

  /**
   * Displays the main menu until the user selects "New Game" or "Quit" (to exit
   * the program).
   *
   * @throws IOException
   *           If initialization fails
   */
  public void start() throws IOException {
    consoleColleague.printRaw(gameData.splashScreen, gameData.getSplashForeground(), gameData.getSplashBackground());
    consoleColleague.waitForKeyPress();
    consoleColleague.clear();
    int index = -1;
    while (index != 0) {
      index = consoleColleague.select("Main Menu", Arrays.asList(" New Game ", " Instructions ", " Legal Notice ", " Quit "), false, false);

      // Help
      if (index == 1) {
        consoleColleague.splash(gameData.instructions);
      }

      // Legal notice
      if (index == 2) {
        consoleColleague.splash(gameData.legalNotice);
      }

      // Exit program
      if (index == 3) {
        gameRunning = false;
        return;
      }
    }

    // New game
    init();
  }

  private final void init() throws IOException {
    consoleColleague.clear();
    consoleColleague.printRaw(gameData.loadingScreen, gameData.getSplashForeground(), gameData.getSplashBackground());

    // Load all of the assets we need at the start of the game
    ItemFactory.clearFactory();
    for (final String itemListFile : gameData.itemListFiles) {
      ItemFactory.initializeFactory(itemListFile);
    }

    NonPlayerCharacterFactory.clearFactory();
    for (final String npcListFile : gameData.npcListFiles) {
      NonPlayerCharacterFactory.initializeFactory(npcListFile);
    }

    final Coordinate playerPosition = ConsoleColleague.getPosition(null);
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(PlayerCharacterData.class);
    player = ((PlayerCharacterData) xmlMarshaller.unmarshalSystemResource(gameData.playerFile)).makePlayerCharacter(playerPosition);

    factionData = ActorFactionData.fromFile(gameData.factionDataFile);
    factionData.init();

    commandFactory = new CommandFactory(this);

    currentLevel = AreaMapData.fromFile(gameData.startingAreaFile).buildLevel(0);
    currentLevel.setIsVisited(true);

    // Done loading assets
    consoleColleague.splash(gameData.introduction);

    consoleColleague.setPlayer(player);
    currentLevel.draw(ConsoleFactory.getInstance());
    consoleColleague.drawLeftPane(currentLevel);
    currentLevel.setPlayer(player);
    consoleColleague.draw(player);
    for (final NonPlayerCharacter npc : currentLevel.getNonPlayerCharacters()) {
      consoleColleague.draw(npc);
    }
  }

  /**
   * Converts keyboard input into relevant game commands.
   *
   * @param input
   *          The character corresponding to a console keypress event
   * @see CommandMapper#getCommand(KeyEvent)
   */
  public void processInput(final KeyEvent input) {
    Command command = CommandMapper.getCommand(input);
    updateList.clear();

    final Message message = commandFactory.performCommand(command);
    if (!StringUtils.isBlank(message.getDetails())) {
      consoleColleague.printMessageLogEntry(message.getDetails());
    }

    command = message.getAction();
    updateGameState(command);
  }

  private void updateGameState(final Command command) {
    Validate.notNull(command);

    final boolean updateNpcs = player.hasMoved() || !CommandMapper.NO_UPDATE_CMD_SET.contains(command);

    consoleColleague.draw(currentLevel.getTileRelative(player.getPosition()));
    consoleColleague.draw(currentLevel.getTileRelative(player.getLastPosition()));

    final Map<Integer, List<AbstractActor>> actors = currentLevel.getAbstractActorsRankedByDistance(player.getPosition(), false);
    actors.remove(0);
    for (final int distance : actors.keySet()) {
      final List<AbstractActor> actorList = actors.get(distance);
      for (final AbstractActor actor : actorList) {
        if (updateNpcs && actor instanceof NonPlayerCharacter) {
          final NonPlayerCharacter npc = (NonPlayerCharacter) actor;
          // TODO: Allow NPCs with speed > player speed to act multiple times
          final Message message = npc.act(factionData, currentLevel, player);

          processNpcMessage(npc, message);
        }

        consoleColleague.draw(currentLevel.getTileRelative(actor.getPosition()));
        consoleColleague.draw(currentLevel.getTileRelative(actor.getLastPosition()));
      }
    }

    for (final Coordinate position : updateList) {
      consoleColleague.draw(currentLevel.getTileRelative(position));
    }

    for (final NonPlayerCharacter npc : currentLevel.getNonPlayerCharacters()) {
      consoleColleague.draw(npc);
    }

    // Draw the player last in case it is in targeting mode
    consoleColleague.draw(player);

    consoleColleague.render();
  }

  private void processNpcMessage(final NonPlayerCharacter npc, final Message message) {
    if (message.getAction().toString().contains("ATTACK")) {
      for (final Coordinate position : message.getTargets()) {
        final AbstractActor defender = currentLevel.getAbstractActorAtPosition(position);
        if (defender != null) {
          processAttack(npc, defender, message.getAction());
        }
      }
    } else if (!StringUtils.isBlank(message.getDetails())) {
      consoleColleague.printMessageLogEntry(message.getDetails(), message.getForeground(), message.getBackground());
    }
  }

  /**
   * Updates the game state when combat ensues.
   *
   * @param attacker
   *          The attacker
   * @param defender
   *          The attacker's target
   * @param command
   *          The command issued by the attacker that caused the attack
   */
  @SuppressWarnings("PMD.CompareObjectsWithEquals")
  public void processAttack(final AbstractActor attacker, final AbstractActor defender, final Command command) {
    Validate.notNull(command);

    // If there are multiple attackers and one has already killed the defender, there's no need to continue attacking a corpse
    if (defender.getCurrentStatBlock().getCurrentHp() < 1) {
      return;
    }

    // Actually process the attack
    final Message message = CombatColleague.attack(attacker, defender, command, currentLevel);
    consoleColleague.printMessageLogEntry(message.getDetails(), message.getForeground(), message.getBackground());

    // Handle death of defender
    if (defender.getCurrentStatBlock().getCurrentHp() < 1) {
      if (defender == player) {
        processPlayerDeath();
      } else {
        processNpcDeath(attacker, defender);
      }
    }
  }

  private void processPlayerDeath() {
    consoleColleague.alert("You have died.");
    consoleColleague.printMessageLogEntry("Press any key.");
    player.setConsoleGraphic(player.makeCorpse().getConsoleGraphic());
    gameRunning = false;
  }

  private void processNpcDeath(final AbstractActor attacker, final AbstractActor defender) {
    final String attackerName = StringUtils.capitalize(attacker.getNameThirdPerson());
    final Color foreground = attacker.getForeground();
    final String defenderName = defender.getNameThirdPerson();
    final String text = attackerName + " killed " + defenderName + ".";

    consoleColleague.printMessageLogEntry(text, foreground, TrueColor.BLACK);
    currentLevel.getNonPlayerCharacters().remove(defender);
    updateList.add(defender.getPosition());
    currentLevel.getTileRelative(defender.getPosition()).addItemsToInventory(defender.removeAllItemsFromInventory());
    currentLevel.getTileRelative(defender.getPosition()).addItemToInventory(defender.makeCorpse());
  }

  /**
   * Changes the current dungeonLevel, renders it in the console display, and updates
   * the game state.
   */
  public void playerEnterLevel() {
    final Tile currentTile = currentLevel.getTileRelative(player.getPosition());

    if (currentTile.getExit() != null) {
      currentLevel = currentLevel.getNeighbor(currentTile.getExit());

      final Direction oppositeDirection = currentTile.getExit().opposite();
      final Coordinate nextPosition = currentLevel.getExitPositionRelative(oppositeDirection);
      currentLevel.setPlayer(player);

      // Set the position twice here to clear the last position, otherwise the coordinates will map to a dungeonLevel that is no longer in the view
      player.setPosition(nextPosition);
      player.setPosition(nextPosition);

      currentLevel.draw(ConsoleFactory.getInstance());
      consoleColleague.drawLeftPane(currentLevel);

      // Assign a new random position for every NPC already in the dungeonLevel (to approximate the passage of time)
      for (final NonPlayerCharacter npc : currentLevel.getNonPlayerCharacters()) {
        Coordinate position = NonPlayerCharacterFactory.getNonPlayerCharacterData(npc.getId()).getStartingPosition();
        if (position == null || !currentLevel.isVacantRelative(position) && !position.equals(npc.getPosition())) {
          position = currentLevel.getRandomCoordinate();
        }

        if (position != null) {
          // Set the position twice here in case the NPC came through an exit from another dungeonLevel
          npc.setPosition(position);
          npc.setPosition(position);
        }
      }

      // Add the monsters last so that we don't accidentally drop one on the player
      currentLevel.addRandomMonstersToLevel();
      currentLevel.setIsVisited(true);
    }
  }

  /**
   * Displays the instruction text from the GameData file.
   */
  public void displayInstructions() {
    consoleColleague.splash(gameData.instructions);
  }

  /**
   * Returns the current state of the game (i.e. if the PlayerCharacter is alive
   * and the user has not quit).
   *
   * @return If the game is running
   */
  public boolean isGameRunning() {
    return gameRunning;
  }

  /**
   * Sets the current state of the game (i.e. if the user wishes to quit).
   *
   * @param gameRunning
   *          The new state of the game
   */
  public void setGameRunning(final boolean gameRunning) {
    this.gameRunning = gameRunning;
  }

  /**
   * PlayerCharacter accessor.
   *
   * @return The player
   */
  public PlayerCharacter getPlayer() {
    return player;
  }

  /**
   * Current dungeonLevel accessor.
   *
   * @return The dungeonLevel currently in the game state
   */
  public DungeonLevel getCurrentLevel() {
    return currentLevel;
  }

  /**
   * ConsoleColleague accessor.
   *
   * @return The ConsoleColleague used to manage console objects
   */
  public ConsoleColleague getConsoleColleague() {
    return consoleColleague;
  }

  /**
   * ActorFactionData accessor.
   *
   * @return The map of actor faction relationships
   */
  public ActorFactionData getActorFactionData() {
    return factionData;
  }
}
