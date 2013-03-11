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

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.callidusrobotics.swing.Console;
import com.callidusrobotics.swing.ConsoleFactory;
import com.callidusrobotics.swing.FontFactory;
import com.callidusrobotics.util.NotInstantiableError;
import com.callidusrobotics.util.TrueColor;
import com.callidusrobotics.util.XmlMarshaller;
import com.callidusrobotics.util.XmlParseException;

/**
 * The entry point for the executable.
 *
 * @author Rusty
 * @since 0.0.1
 */
public final class Roguelike {
  public static final String FONT_SIZE = "font_size_points";
  public static final String ANIMATION_SPEED = "animation_speed_milliseconds";

  private Roguelike() {
    throw new NotInstantiableError();
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static void main(final String[] args) throws IOException {
    final Configuration properties = getProperties(Roguelike.class.getSimpleName() + ".properties");
    final float fontSize = properties.getFloat(FONT_SIZE);
    final int animationSpeed = properties.getInt(ANIMATION_SPEED);

    final Font font = FontFactory.makeTrueTypeFontResource("/fonts/custom/commodore64.ttf", fontSize);
    ConsoleFactory.initInstance(font, 50, 80, animationSpeed);

    final Console console = ConsoleFactory.getInstance();
    console.setTitle(getImplementationVersion());

    final GameData gameData = getGameData(args);
    if (gameData == null) {
      return;
    }

    console.clear();
    final GameMediator mediator = new GameMediator(console, gameData);
    mediator.start();
    while (mediator.isGameRunning()) {
      try {
        console.render();
        final KeyEvent input = console.getKeyPress();
        mediator.processInput(input);
      } catch (final RuntimeException e) {
        writeLogMessage(ExceptionUtils.getStackTrace(e));
      }
    }

    console.getKeyPress();
    console.exit();
  }

  private static String getImplementationVersion() {
    final Package myPackage = Roguelike.class.getPackage();

    if (myPackage.getImplementationTitle() == null || myPackage.getImplementationVersion() == null) {
      return "Roguelike Debug Version";
    }

    return myPackage.getImplementationTitle() + " " + myPackage.getImplementationVersion();
  }

  private static GameData getGameData(final String[] args) {
    final XmlMarshaller xmlMarshaller = new XmlMarshaller(GameData.class);

    String gameName = "DEMO";
    if (args.length > 0) {
      gameName = args[0];
    }

    // TODO: Add support for external GameData files
    final String fileName = "/data/" + gameName + "/gameData.xml";
    final InputStream resource = Roguelike.class.getResourceAsStream(fileName);
    String details;
    if (resource == null) {
      details = "The resource does not exist.";
    } else {
      try {
        resource.close();
      } catch (final IOException e) {
        details = "The resource is unusable";
      }

      try {
        return (GameData) xmlMarshaller.unmarshalSystemResource(fileName);
      } catch (final XmlParseException e) {
        details = e.toString();
      }
    }

    printLineAndExit("Unable to load game data '" + gameName + "'.\n" + details + "\n\nPress any key.");
    return null;
  }

  private static void printLineAndExit(final String line) {
    final Console console = ConsoleFactory.getInstance();
    console.print(1, 1, line, TrueColor.RED, TrueColor.BLACK);
    console.render();
    console.getKeyPress();
    console.exit();
  }

  private static void writeLogMessage(final String message) throws IOException {
    // We could use a logger, but this is the only spot in the code that writes log lines to a file, and we don't really care about most logging features
    final File errorLog = new File("errors.log");
    final String timestamp = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss]", Locale.getDefault()).format(new Date());
    FileUtils.write(errorLog, timestamp + " " + message, true);
  }

  private static Configuration getProperties(final String fileName) {
    final Properties defaultProperties = new Properties();
    defaultProperties.setProperty(FONT_SIZE, Float.toString(8.0f));
    defaultProperties.setProperty(ANIMATION_SPEED, Integer.toString(1000));

    final PropertiesConfiguration properties = new PropertiesConfiguration();
    final PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout(properties);

    layout.setHeaderComment(Roguelike.class.getSimpleName() + " properties file\nThis file will be automatically regenerated if you delete it.");
    layout.setComment(FONT_SIZE, "For best viewing results set the font size to a multiple of 8.0.\n8.0 or 16.0 is recommended depending on screen resolution.");
    layout.setComment(ANIMATION_SPEED, "The delay between animation steps, in milliseconds. (1000 = 1 second)");
    layout.setBlancLinesBefore(ANIMATION_SPEED, 1);

    properties.setFileName(fileName);
    properties.setAutoSave(true);
    properties.setThrowExceptionOnMissing(false);
    properties.setLayout(layout);

    final File file = new File(fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (final IOException e) {
        // We don't have write permission
        properties.setAutoSave(false);
      }
    }

    try {
      properties.load();
    } catch (final ConfigurationException e) {
      // We don't have read permission
      properties.setAutoSave(false);
    }

    try {
      properties.getFloat(FONT_SIZE);
    } catch (final ConversionException | NoSuchElementException e) {
      properties.setProperty(FONT_SIZE, defaultProperties.getProperty(FONT_SIZE));
    }

    try {
      properties.getInt(ANIMATION_SPEED);
    } catch (final ConversionException | NoSuchElementException e) {
      properties.setProperty(ANIMATION_SPEED, defaultProperties.getProperty(ANIMATION_SPEED));
    }

    return properties;
  }
}
