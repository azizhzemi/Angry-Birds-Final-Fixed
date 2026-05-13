# Angry Birds — libGDX + Box2D

A 2D Angry Birds-style game built in Java with **libGDX** and **Box2D**.
This project is being built **phase by phase**.

## Phase 1 — Project Setup ✅

What is in place right now:

- A Gradle-based libGDX desktop project (LWJGL3 backend, Box2D dependency wired in).
- The main `Game` class — `AngryBirdsGame` — that drives screen switching.
- Three screens, navigable from the keyboard:
  - `MenuScreen` — title + Play / Level Select / Exit
  - `LevelSelectScreen` — five level cards, only Level 1 unlocked for now
  - `GameScreen` — placeholder that will host the Box2D world in Phase 2
- A desktop launcher (`DesktopLauncher`) with a 1280×720 window.

Everything renders with `SpriteBatch` + `ShapeRenderer` and the default `BitmapFont`.
No textures or sound effects yet — those land in later phases.

## Requirements

- JDK 17 or newer.

You do **not** need Gradle pre-installed — the project includes the Gradle Wrapper.

## Run it

```bash
cd angry-birds-libgdx
./gradlew run
```

On Windows: `gradlew.bat run`

## Controls

**Menu**
- `SPACE` — start playing (Level 1)
- `L` — open the level selector
- `ESC` — quit

**Level Select**
- `1` … `5` — pick a level (only `1` is unlocked right now)
- `B` or `ESC` — back to the menu

**Game**
- `ESC` — back to the menu
- `R` — restart the level

## Project layout

```
angry-birds-libgdx/
├── build.gradle              (libGDX + Box2D dependencies)
├── settings.gradle
├── gradle.properties
├── assets/                   (textures + sounds will live here from Phase 8)
└── src/main/java/com/angrybirds/
    ├── DesktopLauncher.java  (LWJGL3 entry point)
    ├── AngryBirdsGame.java   (extends Game, handles screen switching)
    └── screens/
        ├── MenuScreen.java
        ├── LevelSelectScreen.java
        └── GameScreen.java
```

## What is coming next

| Phase | Theme                          |
|-------|--------------------------------|
| 2     | Box2D world: ground, bird, target |
| 3     | Slingshot drag + impulse launch   |
| 4     | Dotted trajectory prediction      |
| 5     | Collisions, destruction, particles|
| 6     | Smooth follow camera              |
| 7     | Real UI (Scene2D), score, pause   |
| 8     | Textures, sound, parallax background |
