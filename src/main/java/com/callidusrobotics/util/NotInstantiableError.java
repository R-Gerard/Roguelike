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

/**
 * AssertionError to be thrown by the private default constructor of a class
 * that is not meant to be instantiated.
 * 
 * @author Rusty
 * @since 0.0.1
 */
public class NotInstantiableError extends AssertionError {
  private static final long serialVersionUID = -4270408063509602942L;

  public NotInstantiableError() {
    super("Do not instantiate this class.");
  }
}
