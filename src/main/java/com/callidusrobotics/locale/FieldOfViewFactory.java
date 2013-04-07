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

package com.callidusrobotics.locale;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.reflections.Reflections;

import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.Identifier;
import com.callidusrobotics.util.NotInstantiableError;

/**
 * Builds FieldOfViewStrategy objects to illuminate the current DungeonLevel.
 * <p>
 * Scans for the appropriate FieldOfViewStrategy object to instantiate based on the
 * FieldOfViewType.
 *
 * @author Rusty
 * @since 0.0.1
 * @see FieldOfViewStrategy
 * @see FieldOfViewType
 * @see Illuminates
 */
public final class FieldOfViewFactory {

  private static final Map<FieldOfViewType, FieldOfViewStrategy> ILLUMINATOR_MAP;

  static {
    final Map<FieldOfViewType, FieldOfViewStrategy> illuminatorMap = new HashMap<FieldOfViewType, FieldOfViewStrategy>();
    final Reflections reflections = Identifier.isDebugMode() ? new Reflections(FieldOfViewFactory.class.getPackage()) : Reflections.collect();
    final Set<Class<?>> builders = reflections.getTypesAnnotatedWith(Illuminates.class);

    for (final Class<?> clazz : builders) {
      final FieldOfViewType fovType = clazz.getAnnotation(Illuminates.class).value();
      if (illuminatorMap.containsKey(fovType)) {
        throw new DuplicateKeyException("FieldOfViewType '" + fovType + "' is registered by more than one " + FieldOfViewStrategy.class.getSimpleName() + ".");
      }

      // Create an instance now to verify that it is of the right type for later
      Object instance = null;
      try {
        instance = clazz.newInstance();
      } catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }

      if (!(instance instanceof AbstractFieldOfViewStrategy)) {
        throw new UnsupportedOperationException("Class '" + clazz.getSimpleName() + "' does not extend " + AbstractFieldOfViewStrategy.class.getSimpleName() + ".");
      }

      // Store the class so we can generate new instances on demand later
      illuminatorMap.put(fovType, (FieldOfViewStrategy) instance);
    }

    ILLUMINATOR_MAP = Collections.unmodifiableMap(illuminatorMap);
  }

  private FieldOfViewFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Creates a new instance of the appropriate FieldOfViewStrategy object for use by the
   * calling DungeonLevel.
   *
   * @param type The desired illumination behavior
   * @return The desired FieldOfViewStrategy, never null
   */
  public static FieldOfViewStrategy makeFovStrategy(final FieldOfViewType type) {
    Validate.notNull(type);

    if (!ILLUMINATOR_MAP.containsKey(type)) {
      if (Identifier.isDebugMode()) {
        throw new UnsupportedOperationException(FieldOfViewStrategy.class.getSimpleName() + " for type " + type + " is not implemented.");
      } else {
        return ILLUMINATOR_MAP.get(FieldOfViewType.ALL);
      }
    }

    return ILLUMINATOR_MAP.get(type);
  }

  /**
   * Notifies all FieldOfViewStrategy instances that the PlayerCharacter has
   * moved to another DungeonLevel.
   *
   * @see FieldOfViewStrategy#playerExitLevel()
   */
  public static void playerExitLevel() {
    for (final FieldOfViewStrategy strategy : ILLUMINATOR_MAP.values()) {
      strategy.playerExitLevel();
    }
  }
}
