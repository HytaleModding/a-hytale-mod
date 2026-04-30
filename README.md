# A-Hytale-Mod

A Hytale mod where anyone can commit changes, as long as the project compiles.

This repository follows the structure from
[realBritakee/hytale-template-plugin](https://github.com/realBritakee/hytale-template-plugin):

- Gradle Java project with the Shadow plugin
- Java 25 toolchain
- Hytale Maven repositories
- Plugin manifest in `src/main/resources/manifest.json`
- Minimal Java plugin entrypoint in `src/main/java`
- GitHub Actions build and release workflow

## Requirements

- Java 25 JDK
- Git
- Hytale install, only needed for local `runServer`

## Build

```bash
./gradlew shadowJar
```

The plugin jar is written to `build/libs/A-Hytale-Mod-1.0.0.jar`.

## Run Locally

If Hytale is installed in the default location, run:

```bash
./gradlew runServer
```

For a custom install location:

```bash
./gradlew runServer -Phytale_home=/path/to/Hytale
```

## Project Rules

- You can commit again once at least 5 other people have committed.
- Changes to readme, workflows, or build files do not count.
- Do not remove any lines of code. Formatting is okay, but keep it reasonable.
- Any change must compile and the game must still be playable with the mod loaded.
- Merge conflicts must be resolved keeping the spirit of the previous change, or with agreement from the author of the conflicting code.
