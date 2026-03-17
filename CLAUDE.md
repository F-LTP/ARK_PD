# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**RogueNights (Arrrknights)** is a modified version of Shattered Pixel Dungeon, which itself is based on the original Pixel Dungeon by Watabou. This is a roguelike RPG with randomly generated levels, items, enemies, and traps, built on LibGDX for cross-platform support (Android and Desktop). It is a fan game of Arknights by Hypergryph.

- **Package**: `com.shatteredpixel.tomorrowpixel`
- **Language**: Java 11
- **Build System**: Gradle with multi-module setup
- **Platforms**: Android (min SDK 21, target SDK 35) and Desktop (via LibGDX/LWJGL3)
- **Version info**: `appVersionCode` and `appVersionName` are defined in root `build.gradle`

## Build Commands

```bash
./gradlew desktop:debug           # Run game in debug mode (primary dev workflow)
./gradlew desktop:release         # Build release JAR -> /desktop/build/libs
./gradlew android:assembleDebug   # Build debug APK
./gradlew android:assembleRelease # Build release APK with R8 optimization
./gradlew clean                   # Clean all build artifacts
```

There are no tests in this project.

## Architecture

### Multi-Module Gradle Structure

1. **core/** - Platform-independent game logic (nearly all code lives here)
   - Package: `com.shatteredpixel.shatteredpixeldungeon`
   - Assets in `core/src/main/assets/`

2. **SPD-classes/** - Base LibGDX/Noosa rendering framework
   - Package: `com.watabou.*`
   - Low-level rendering, input, and utility classes

3. **android/** and **desktop/** - Platform launchers

4. **services/** - Modular service implementations (update checkers, news feeds)

### Core Architecture Patterns

#### Scene-Based Flow
- **TomorrowRogueNight.java** - Main game class (LibGDX `Game` subclass)
- **Dungeon.java** - Core dungeon state manager (save/load, floor transitions)
- Scenes in `scenes/`: TitleScene, GameScene, InterlevelScene, AlchemyScene, etc.

#### Actor System (Turn-Based)
- **Actor.java** → **Char.java** → Hero / Mob hierarchy
- **actors/hero/** - Player character with 7 classes: WARRIOR, MAGE, ROGUE, HUNTRESS, ROSECAT, NEARL, CHEN (each has 3 subclasses)
- **actors/mobs/** - Enemies and NPCs
- **actors/buffs/** - Status effects and temporary modifiers
- **actors/blobs/** - Area-of-effect entities (fire, gas, etc.)

#### Item System
- Standard SPD categories: armor, weapons, artifacts, potions, scrolls, wands, rings
- Custom mod categories: `Gunaccessories/`, `Skill/`, `NewGameItem/`, `wands/SP/`
- **Generator.java** - Procedural item generation and loot tables

#### Procedural Level Generation
- Area types: SewerLevel, PrisonLevel, CavesLevel, CityLevel, HallsLevel
- Custom boss levels: SeaBossLevel1/2, GavialBossLevel1/2, NewPrisonBossLevel
- Custom areas: RhodesLevel, SiestaLevel
- `levels/builders/` - Room layout algorithms
- `levels/rooms/` - Room type definitions

### Key Patterns for Development

#### Serialization (Bundle System)
All persistent game objects implement `Bundlable` with `storeInBundle(Bundle)` and `restoreFromBundle(Bundle)`. When adding new fields to any Actor, Item, Buff, or Level, you must:
1. Define a `private static final String` key constant
2. Store the field in `storeInBundle()`
3. Restore it in `restoreFromBundle()`
Save files track `Game.versionCode` for migration compatibility.

#### Localization
Properties files in `core/src/main/assets/messages/` using hierarchical dot-notation keys:
- Pattern: `<category>.<fully.qualified.classname>.<property>=Value`
- Example: `items.wands.sp.staffofbreeze.name=Staff of Breeze`
- Accessed via: `Messages.get(this, "key")` or `Messages.get(ClassName.class, "key")`
- Base file (Korean) + `_en.properties` for English translations
- Each class that has display text needs corresponding entries in the properties files

#### Adding New Items/Actors
When creating a new item or actor class:
1. Create the Java class extending the appropriate parent
2. Add localization strings (name, desc) to the matching `.properties` files (both base and `_en`)
3. If it should spawn naturally, register it in `Generator.java`
4. Add a sprite or reuse an existing one via the sprite classes
5. Implement `storeInBundle`/`restoreFromBundle` if it has custom state

### Key Files

- `core/src/main/java/.../TomorrowRogueNight.java` - Application entry point
- `core/src/main/java/.../Dungeon.java` - Central game state, save/load
- `core/src/main/java/.../actors/Actor.java` - Base actor class
- `core/src/main/java/.../actors/hero/HeroClass.java` - Playable class definitions
- `core/src/main/java/.../items/Generator.java` - Item generation system
- `build.gradle` - Root build configuration with version numbers

## Important Notes

- **No Tests**: The project has no test suite. Verify changes by running the game via `./gradlew desktop:debug`.
- **No Pull Requests**: This repository does not accept PRs.
- **License**: GPLv3 - any modifications must be open-sourced if distributed.
- **ProGuard**: R8 full mode is disabled in gradle.properties; enabled for release builds.
- **Documentation**: See `/docs` for compilation guides and recommended changes for creating mods.
