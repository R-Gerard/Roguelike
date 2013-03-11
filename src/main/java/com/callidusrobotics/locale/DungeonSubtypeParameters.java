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

import java.util.Random;

/**
 * Interface defining data for specific dungeon generation algorithms.
 *
 * @author Rusty
 * @since 0.0.1
 * @see DungeonBuilder
 * @see DungeonMapData
 */
interface DungeonSubtypeParameters {

  void normalizeParameters();

  RoomType getRandomRoomType(final Random random);

  int getMinSize();

  int getMaxSize();
}
