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

package com.callidusrobotics.util;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * Static methods for converting strings into identifiers for factories.
 *
 * @author Rusty
 * @since 0.0.1
 */
public final class Identifier {
  private Identifier() {
    throw new NotInstantiableError();
  }

  public static String string2identifier(final String identifier) {
    return StringUtils.lowerCase(StringUtils.replace(StringUtils.trim(identifier), " ", "_"), Locale.ENGLISH);
  }

  public static boolean isDebugMode() {
    final Package myPackage = Identifier.class.getPackage();
    return (myPackage.getImplementationTitle() == null || myPackage.getImplementationVersion() == null);
  }
}
