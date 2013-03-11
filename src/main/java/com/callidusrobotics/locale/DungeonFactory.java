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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.reflections.Reflections;

import com.callidusrobotics.util.DuplicateKeyException;
import com.callidusrobotics.util.Identifier;
import com.callidusrobotics.util.InvalidKeyException;
import com.callidusrobotics.util.NotInstantiableError;

/**
 * Builds dungeons (collections of DungeonLevel objects) using DungeonMapData
 * objects and the corresponding DungeonBuilder.
 * <p>
 * Scans for the appropriate DungeonBuilder based on the DungeonType set for the
 * DungeonMapData.
 *
 * @author Rusty
 * @since 0.0.1
 * @see Builds
 * @see DungeonType
 * @see DungeonBuilder
 */
public final class DungeonFactory {

  private static final Map<DungeonType, Class<?>> BUILDER_MAP;

  static {
    final Map<DungeonType, Class<?>> builderMap = new HashMap<DungeonType, Class<?>>();
    final Reflections reflections = Identifier.isDebugMode() ? new Reflections(DungeonFactory.class.getPackage()) : Reflections.collect();
    final Set<Class<?>> builders = reflections.getTypesAnnotatedWith(Builds.class);

    for (final Class<?> clazz : builders) {
      final DungeonType dungeonType = clazz.getAnnotation(Builds.class).value();
      if (builderMap.containsKey(dungeonType)) {
        throw new DuplicateKeyException("DungeonType '" + dungeonType + "' is registered by more than one " + DungeonBuilder.class.getSimpleName() + ".");
      }

      // Create an instance now to verify that it is of the right type for later
      Object instance = null;
      try {
        instance = clazz.newInstance();
      } catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }

      if (!(instance instanceof AbstractDungeonBuilder)) {
        throw new UnsupportedOperationException("Class '" + clazz.getSimpleName() + "' does not extend " + AbstractDungeonBuilder.class.getSimpleName() + ".");
      }

      // Store the class so we can generate new instances on demand later
      builderMap.put(dungeonType, clazz);
    }

    BUILDER_MAP = Collections.unmodifiableMap(builderMap);
  }

  private DungeonFactory() {
    throw new NotInstantiableError();
  }

  /**
   * Generates a random serialized dungeon.
   *
   * @param dungeonMapData
   *          The dungeon specification, not null
   * @return A base-64 encoded String representing the serialized dungeon, never
   *         null
   * @throws IOException
   *           If {@link DungeonBuilder#setDungeonMapData(DungeonMapData)} fails
   * @throws ReflectiveOperationException
   *           If the desired DungeonBuilder can not be instantiated
   * @see DungeonBuilder#buildRandomDungeonString()
   */
  public static String generateSerializedDungeon(final DungeonMapData dungeonMapData) throws IOException, ReflectiveOperationException {
    final DungeonBuilder builder = getDungeonBuilder(dungeonMapData);
    return builder.buildRandomDungeonString();
  }

  /**
   * De-serializes a dungeon.
   *
   * @param dungeonMapData
   *          The dungeon specification, not null
   * @param serializedDungeon
   *          The serialized dungeon String, not null
   * @return The entrance to the dungeon, never null
   * @throws IOException
   *           If {@link DungeonBuilder#setDungeonMapData(DungeonMapData)} fails
   * @throws ReflectiveOperationException
   *           If the desired DungeonBuilder can not be instantiated
   * @see DungeonBuilder#fromString(String)
   */
  public static DungeonLevel fromString(final DungeonMapData dungeonMapData, final String serializedDungeon) throws IOException, ReflectiveOperationException {
    final DungeonBuilder builder = getDungeonBuilder(dungeonMapData);
    builder.fromString(serializedDungeon);
    final List<DungeonLevel> dungeonLevels = builder.getLevels();
    return dungeonLevels.get(0);
  }

  /**
   * Generates a random dungeon.
   *
   * @param dungeonMapData
   *          The dungeon specification, not null
   * @return The entrance to the dungeon, never null
   * @throws IOException
   *           If {@link DungeonBuilder#setDungeonMapData(DungeonMapData)} fails
   * @throws ReflectiveOperationException
   *           If the desired DungeonBuilder can not be instantiated
   */
  public static DungeonLevel buildDungeon(final DungeonMapData dungeonMapData) throws IOException, ReflectiveOperationException {
    final DungeonBuilder builder = getDungeonBuilder(dungeonMapData);
    builder.fromString(builder.buildRandomDungeonString());
    final List<DungeonLevel> dungeonLevels = builder.getLevels();
    return dungeonLevels.get(0);
  }

  private static DungeonBuilder getDungeonBuilder(final DungeonMapData dungeonMapData) throws IOException, ReflectiveOperationException {
    final DungeonType dungeonType = dungeonMapData.getDungeonType();

    Validate.notNull(dungeonType);
    if (!BUILDER_MAP.containsKey(dungeonType)) {
      throw new InvalidKeyException("Encountered unknown DungeonType '" + dungeonType + "'.");
    }

    final DungeonBuilder builder = (DungeonBuilder) BUILDER_MAP.get(dungeonType).newInstance();
    builder.setDungeonMapData(dungeonMapData);

    return builder;
  }
}
