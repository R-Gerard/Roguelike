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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.callidusrobotics.attribute.Colored;
import com.callidusrobotics.command.Command;
import com.callidusrobotics.locale.Coordinate;

/**
 * Class that encapsulates events between the <code>PlayerCharacter</code> and
 * <code>NonPlayerCharacter</code>s for use by the <code>GameMediator</code>.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class Message implements Colored {
  private final Command action;
  private final List<Coordinate> targets;
  private final String details;
  private final Color foreground, background;

  public Message(final Command action, final List<Coordinate> targets, final String details, final Color foreground, final Color background) {
    this.action = action;
    this.targets = targets;
    this.details = details;
    this.foreground = foreground;
    this.background = background;

    Validate.notNull(action, "Commands must not be null.");
    if (action.toString().contains("ATTACK")) {
      Validate.notNull(targets, "At least one target must be specified when attacking.");
      Validate.isTrue(!targets.isEmpty(), "At least one target must be specified when attacking.");
    }

    // Often times the GameMediator will ignore the foreground and background colors, but specify the foreground anyway to prevent unintended effects
    if (!StringUtils.isBlank(details)) {
      Validate.notNull(foreground, "Foreground color must be specified for details string.");
    }
  }

  /**
   * Action accessor.
   * <p>
   * The action to be performed. This could either be the result of a keypress
   * event from the user or a decision made by a NonPlayerCharacter.
   *
   * @return The command that caused this message
   */
  public Command getAction() {
    return action;
  }

  /**
   * Targets accessor.
   *
   * @return The coordinates of the target(s) of the action, never null
   */
  public List<Coordinate> getTargets() {
    if (targets == null) {
      return Collections.emptyList();
    }

    return Collections.unmodifiableList(targets);
  }

  /**
   * Details accessor.
   *
   * @return The description of the action performed, or null
   */
  public String getDetails() {
    return details;
  }

  @Override
  public String toString() {
    return action + " " + details;
  }

  @Override
  public Color getForeground() {
    return foreground;
  }

  @Override
  public Color getBackground() {
    return background;
  }
}
