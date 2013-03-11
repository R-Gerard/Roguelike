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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.reflections.Reflections;

import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.Identifier;
import com.callidusrobotics.util.InvalidKeyException;
import com.callidusrobotics.util.NotInstantiableError;

/**
 * Builds AiStrategy objects for use by NonPlayerCharacter objects.
 * <p>
 * Scans for the appropriate AiStrategy object to instantiate based on the
 * Behavior type.
 *
 * @author Rusty
 * @since 0.0.1
 * @see AiStrategy
 * @see Behavior
 * @see Behaves
 */
public final class AiStrategyFactory {

  private static final Map<Behavior, Class<?>> BEHAVIOR_MAP;

  static {
    final Map<Behavior, Class<?>> behaviorMap = new HashMap<Behavior, Class<?>>();
    final Reflections reflections = Identifier.isDebugMode() ? new Reflections(AiStrategyFactory.class.getPackage()) : Reflections.collect();
    final Set<Class<?>> behaviors = reflections.getTypesAnnotatedWith(Behaves.class);

    for (final Class<?> clazz : behaviors) {
      final Behavior behavior = clazz.getAnnotation(Behaves.class).value();
      if (behaviorMap.containsKey(behavior)) {
        throw new DuplicateKeyException("Behavior '" + behavior + "' is registered by more than one " + AiStrategy.class.getSimpleName() + ".");
      }

      // Create an instance now to verify that it is of the right type for later
      Object instance = null;
      try {
        instance = clazz.newInstance();
      } catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }

      if (!(instance instanceof AbstractAiStrategy)) {
        throw new UnsupportedOperationException("Class '" + clazz.getSimpleName() + "' does not extend " + AbstractAiStrategy.class.getSimpleName() + ".");
      }

      // Store the class so we can generate new instances on demand later
      behaviorMap.put(behavior, clazz);
    }

    BEHAVIOR_MAP = Collections.unmodifiableMap(behaviorMap);
  }

  private AiStrategyFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Creates a new instance of the appropriate AiStrategy object for use by the
   * calling NonPlayerCharacter.
   *
   * @param behavior
   *          The desired behavior of the AiStrategy object
   * @param npc
   *          The NonPlayerCharacter that the AiStrategy will control
   * @param min
   *          The minimum efficacy of the behavior
   * @param max
   *          The maximum efficacy of the behavior
   * @param base
   *          The base efficacy of the behavior
   * @param multiplier
   *          The situational efficacy multiplier of the behavior
   * @return The desired AiStrategy object, or null
   */
  public static AiStrategy makeAiStrategy(final Behavior behavior, final NonPlayerCharacter npc, final int min, final int max, final int base, final int multiplier) {
    Validate.notNull(behavior);

    if (!BEHAVIOR_MAP.containsKey(behavior)) {
      throw new InvalidKeyException("Encountered unknown Behavior '" + behavior + "'.");
    }

    try {
      final AbstractAiStrategy instance = (AbstractAiStrategy) BEHAVIOR_MAP.get(behavior).newInstance();

      instance.setSelf(npc);
      instance.setMin(min);
      instance.setMax(max);
      instance.setBase(base);
      instance.setMultiplier(multiplier);

      return instance;
    } catch (final ReflectiveOperationException e) {
      return null;
    }
  }
}
