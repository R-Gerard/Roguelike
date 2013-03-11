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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO for maintaining statistics of in-game objects, typically AbstractActor
 * objects, but also Item objects (i.e. how they change an AbstractActor when
 * used or equipped).
 *
 * @author Rusty
 * @since 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StatBlock {
  @XmlElement(required = true) protected int maxHp, currentHp, strength, agility, intelligence, defense, speed;

  StatBlock() { /* Required by JAXB */ }

  public StatBlock(final StatBlock other) {
    if (other == null) {
      this.maxHp = this.currentHp = this.strength = this.speed = this.agility = this.intelligence = this.defense = this.speed = 0;
    } else {
      maxHp = other.maxHp;
      currentHp = other.currentHp;
      strength = other.strength;
      agility = other.agility;
      intelligence = other.intelligence;
      defense = other.defense;
      speed = other.speed;
    }
  }

  public StatBlock(final int maxHp, final int currentHp, final int strength, final int agility, final int intelligence, final int defense, final int speed) {
    this.maxHp = maxHp;
    this.currentHp = currentHp;
    this.strength = strength;
    this.agility = agility;
    this.intelligence = intelligence;
    this.defense = defense;
    this.speed = speed;
  }

  public StatBlock combine(final StatBlock other) {
    final StatBlock result = new StatBlock(this);

    if (other != null) {
      result.maxHp = Math.max(1, maxHp + other.maxHp);
      result.currentHp = currentHp + other.currentHp;
      result.strength = Math.max(1, strength + other.strength);
      result.agility = Math.max(1, agility + other.agility);
      result.intelligence = Math.max(1, intelligence + other.intelligence);
      result.defense = Math.max(1, defense + other.defense);
      result.speed = Math.max(1, speed + other.speed);
    }

    return result;
  }

  public int getMaxHp() {
    return maxHp;
  }

  public void setMaxHp(final int maxHp) {
    this.maxHp = maxHp;
  }

  public int getCurrentHp() {
    return currentHp;
  }

  public void setCurrentHp(final int currentHp) {
    this.currentHp = currentHp;
  }

  public int getStrength() {
    return strength;
  }

  public void setStrength(final int strength) {
    this.strength = strength;
  }

  public int getAgility() {
    return agility;
  }

  public void setAgility(final int agility) {
    this.agility = agility;
  }

  public int getIntelligence() {
    return intelligence;
  }

  public void setIntelligence(final int intelligence) {
    this.intelligence = intelligence;
  }

  public int getDefense() {
    return defense;
  }

  public void setDefense(final int defense) {
    this.defense = defense;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(final int speed) {
    this.speed = speed;
  }

  @Override
  public String toString() {
    return "\n" +
        " HP:  " + currentHp + "/" + maxHp + "\n\n" +
        " STR: " + strength + "\n" +
        " AGI: " + agility + "\n" +
        " INT: " + intelligence + "\n\n" +
        " DEFENSE: " + defense + "\n" +
        " SPEED:   " + speed + "\n";
  }
}
