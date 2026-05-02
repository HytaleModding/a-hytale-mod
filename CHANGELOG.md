# Changelog

All notable changes to this project will be documented in this file.

## 0.0.2-SNAPSHOT

### Added

- Added configurable chaotic server messages backed by a custom JSON asset type.
- Added the default chaotic message set under `Server/ChaoticMessages/Default.json`.
- Added global or player-only delivery settings for each message category.
- Added configurable rare join messages and periodic server broadcasts.
- Added Asset Editor visibility support for AHytaleMod's embedded asset pack.
- Simplified chaotic message asset categories to `Join`, `JoinRare`, and `Periodic`.

### Fixed

- Enabled the bundled asset pack so the Gaia body pillow item, model, texture, icon, and language assets can load in game.
- Added a target server version declaration for `2026.03.26-89796e57b`.
