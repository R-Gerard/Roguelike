# MythosRL: A game of Lovecraftian Horror

Copyright (c) 2013 Rusty Gerard

> The halls of Miskatonic University have been transformed into a weird, perilous labyrinth. You must enter the dungeon, find the Necronomicon, and return safely back to Arkham.
> Refer to the in-game help menu for further instructions.


# Tips For Beginners

Here are a few tips on how to stay alive:

* The dungeon is non-Euclidean. It is easy to get lost, so drop breadcrumbs by the exits you have visited to mark your path.
* Monsters will continue to spawn even after you have cleared rooms, so try to avoid backtracking.
* The Sheriff carries a better gun than what you start out with (longer range, greater damage, but slow to reload), so if you kill him you can loot his corpse.
* Don’t waste precious ammunition on the Rat Things. They are hard to hit but are relatively harmless, so kill them in melee with your pocket knife, a cavalry saber, or even an empty revolver.
* More than one of the items you find in the dungeon are multi-purpose, so try equipping them in various slots or 'u'sing them.


# System Requirements

* 1024 x 768 or larger color display.
* Java Runtime Environment (JRE) version 7.
* If you do not have a JRE installed, you can download the latest JRE for your computer here: www.java.com/getjava/


# Known Issues

* Linux + OpenJDK: The console will sometimes have sporadic issues when the program starts. Typically the window will load incorrectly and the top and bottom rows will be missing. If this happens, restart the program and try again.


# Version History

## 0.0.3 (internal release)
* Added an examine command to display in-game descriptions of NPCs and the environment.
* Small items like bullets now have a maximum quantity per item slot.
* NPCs can now attack each other.
* Added new AI strategies for greedy and cowardly NPCs.
* Added support for field-of-view algorithms.

## 0.0.2 (March 23, 2013)
* JRE 7 is now required.
* Minor bug fixes with items and equipment.
* When aiming a weapon, spacebar now toggles between potential targets.
* When walking, spacebar now toggles between the standard display and the extended message log.
* Grab and drop behavior is now more intuitive.

## 0.0.1 (March 17, 2013)
* Initial release for the 2013 7 Day Roguelike challenge (7DRL).
