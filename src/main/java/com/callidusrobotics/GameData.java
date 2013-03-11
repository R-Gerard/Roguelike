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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.callidusrobotics.util.TrueColor;

/**
 * POJO for the root GameData XML file to bootstrap the game.
 *
 * @author Rusty
 * @since 0.0.1
 */
@XmlRootElement
public final class GameData {
  @XmlElement(required = true) String loadingScreen;
  @XmlElement(required = true) String splashScreen;
  @XmlElement(required = true) String introduction;
  @XmlElement(required = true) String legalNotice;
  @XmlElement(required = true) String instructions;
  @XmlElement(required = true) String playerFile;
  @XmlElement(required = true) String startingAreaFile;
  @XmlElement(required = true) String factionDataFile;
  @XmlElementWrapper(required = true) @XmlElement(name = "itemListFile") List<String> itemListFiles;
  @XmlElementWrapper(required = true) @XmlElement(name = "npcListFile") List<String> npcListFiles;
  @XmlElement(required = true) String splashForeground, splashBackground, boxForeground, boxBackground, fontForeground, fontBackground, selectForeground, selectBackground;

  GameData() { /* Required by JAXB */ }

  TrueColor getSplashForeground() {
    return new TrueColor(Integer.decode(splashForeground));
  }

  TrueColor getSplashBackground() {
    return new TrueColor(Integer.decode(splashBackground));
  }

  TrueColor getBoxForeground() {
    return new TrueColor(Integer.decode(boxForeground));
  }

  TrueColor getBoxBackground() {
    return new TrueColor(Integer.decode(boxBackground));
  }

  TrueColor getFontForeground() {
    return new TrueColor(Integer.decode(fontForeground));
  }

  TrueColor getFontBackground() {
    return new TrueColor(Integer.decode(fontBackground));
  }

  TrueColor getHighlightForeground() {
    return new TrueColor(Integer.decode(selectForeground));
  }

  TrueColor getHighlightBackground() {
    return new TrueColor(Integer.decode(selectBackground));
  }
}
