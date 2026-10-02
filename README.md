# VitalPlates

VitalPlates is a client-side Fabric mod for Minecraft 26.3 that dynamically adjusts nametag text colors and plate background tint based on remaining health percentage.

It provides immediate, clean visual feedback for the health status of players and custom-named entities without cluttering the screen with health bars or custom HUD overlays.

---

## Features

### Dynamic Health Color Transition
Nametags change color smoothly and continuously in response to an entity's remaining health fraction:
- **Classic Mode (Default):** A clean transition from standard White (`#FFFFFF`) at full health directly down to classic Minecraft &c Red (`#FF5555`) at critical health. There are no distracting intermediate rainbow colors.
- **Spectrum Mode:** A multi-step gradient transitioning from Green (`#00FF00`) at full health through Yellow (`#FFFF00`) and Orange (`#FF8000`) to Red (`#FF5555`).

### Plate Background Tinting
Unlike traditional mods that only alter the text color, VitalPlates also subtly tints the translucent nametag background plate red as health declines. This enhances legibility and gives a natural indicator of damage.
- The background plate remains standard translucent black at full health and smoothly blends into a darker crimson tint at low health.
- Configurable intensity levels:
  - Subtle (40%)
  - Balanced (65% - Default)
  - Pronounced (90%)

### Target Filtering
Independently toggle whether nametag coloring applies to:
- Players
- Custom-named living entities (mobs with nametags)

### Pure Client-Side Operation
VitalPlates runs entirely on the client:
- Does not send or require custom network packets.
- Fully compatible with standard vanilla servers, multiplayer networks, and singleplayer worlds.
- Requires no server-side mod installation.

### In-Game Configuration (ModMenu)
VitalPlates integrates with ModMenu to provide a native configuration screen built entirely with vanilla Minecraft UI components:
- Zero heavy third-party configuration library dependencies.
- Changes are saved instantly to `.minecraft/config/vitalplates.json`.
- Full reset-to-defaults button included.

### Localization (i18n)
- English (`en_us`) is the default and fallback language.
- Turkish (`tr_tr`) is included out of the box.
- Automatically follows the client's selected game language.

---

## Requirements

- Minecraft 26.3
- Fabric Loader 0.19.3 or newer
- Fabric API 0.161.0+26.3 or newer
- Java 25 or newer
- *(Optional)* ModMenu for accessing the in-game settings screen

---

## Configuration

Settings can be modified either via the ModMenu in-game interface or by editing `.minecraft/config/vitalplates.json`:

```json
{
  "enabled": true,
  "transitionMode": "CLASSIC",
  "tintBackground": true,
  "backgroundIntensity": "NORMAL",
  "affectPlayers": true,
  "affectNamedMobs": true
}
```

### Options Description

| Key | Default | Description |
| --- | --- | --- |
| `enabled` | `true` | Master toggle for all nametag color modifications. |
| `transitionMode` | `CLASSIC` | Color mode: `CLASSIC` (White to Red) or `SPECTRUM` (Green to Red). |
| `tintBackground` | `true` | Toggles dynamic reddening of the nametag background plate. |
| `backgroundIntensity` | `NORMAL` | Intensity multiplier for plate tint (`LOW`, `NORMAL`, `HIGH`). |
| `affectPlayers` | `true` | Apply color shifts to player nametags. |
| `affectNamedMobs` | `true` | Apply color shifts to custom-named living entities. |

---

## Building from Source

To compile the mod yourself:

```bash
git clone https://github.com/aegeada/vitalplates.git
cd vitalplates
./gradlew build
```

The compiled JAR file will be located in `build/libs/`.

To run the automated unit test suite:

```bash
./gradlew test
```

---

## Clean-Room Implementation Notice

VitalPlates was designed and authored independently from scratch for Minecraft 26.3. It contains no proprietary, leaked, or copied source code from prior nametag modifications.

---

## License

VitalPlates is released under the [MIT License](LICENSE).
