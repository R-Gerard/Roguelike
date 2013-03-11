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

import com.callidusrobotics.attribute.Named;
import com.callidusrobotics.util.Identifier;

/**
 * An enumerated type of behaviors used to describe what AiStrategy
 * implementations do.
 *
 * @author Rusty
 * @since 0.0.1
 */
public enum Behavior implements Named {
  MELEE,
  WANDERING;

  @Override
  public String getId() {
    return Identifier.string2identifier(toString());
  }

  @Override
  public String getName() {
    return toString();
  }

  @Override
  public String getDescription() {
    return "";
  }
}
