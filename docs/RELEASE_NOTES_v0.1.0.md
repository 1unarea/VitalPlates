# VitalPlates v0.1.0 - Initial Release (Minecraft 26.3)

VitalPlates v0.1.0 is the first official release of VitalPlates, built from scratch for Minecraft 26.3 on Fabric.

---

## What's New

### Core Features
- **Dynamic Nametag Gradient:** Nametags smoothly change color based on the entity's remaining health percentage.
- **Classic Transition Mode:** Defaults to a clean gradient moving from pure White (`#FFFFFF`) to classic Minecraft &c Red (`#FF5555`) with no intermediate rainbow colors.
- **Spectrum Transition Mode:** Configurable multi-color mode passing from Green through Yellow and Orange down to Red.
- **Plate Background Reddening:** Dynamically tints the nametag background plate crimson as health drops, with configurable intensity (Subtle, Balanced, Pronounced).
- **Target Selection:** Individual toggles for players and custom-named living entities.

### Client-Side Integration
- **Pure Client-Side Operation:** Functions seamlessly on vanilla multiplayer servers, singleplayer, and modded setups without requiring server-side plugins or mods.
- **Zero Heavy GUI Dependencies:** In-game configuration screen integrated with ModMenu using vanilla Minecraft Screen components.
- **Full Localization (i18n):** Native support for English (`en_us`) as default/fallback and Turkish (`tr_tr`).

---

## Compatibility
- Target Game Version: Minecraft 26.3
- Mod Loader: Fabric Loader >= 0.19.3
- Java: 25+
- Dependencies: Fabric API (>= 0.161.0+26.3)
