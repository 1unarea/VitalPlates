# VitalPlates (Modrinth / CurseForge Description)

VitalPlates is a client-side Fabric mod for Minecraft 26.3 that dynamically shifts the color of nametags and their background plates based on current health.

It offers a clean, non-intrusive alternative to bulky health bars and cluttered HUD widgets, allowing you to gauge health levels naturally at a glance.

---

## Highlights

- **Dynamic Color Gradient:** Nametags smoothly transition in color as health decreases.
- **Classic Transition (Default):** White (`#FFFFFF`) at full health smoothly shifts down to classic Minecraft &c Red (`#FF5555`) at low health. No intermediate yellow or green hues.
- **Spectrum Transition:** Optional classic rainbow gradient transitioning from Green through Yellow and Orange down to Red.
- **Plate Background Tinting:** The nametag background plate also subtly blushes red as health drops, improving text contrast and situational awareness.
- **Configurable Intensity:** Choose between Subtle, Balanced, or Pronounced plate tinting.
- **Target Filtering:** Independently enable or disable the effect for players and custom-named mobs.
- **Pure Client-Side:** Operates strictly on the client. Works seamlessly on vanilla servers, multiplayer networks, and singleplayer worlds without requiring server-side installation.
- **In-Game Settings (ModMenu):** Adjust all settings directly in-game with zero external GUI library dependencies.
- **Multilingual Support:** English (default/fallback) and Turkish included out of the box.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) for Minecraft 26.3.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api).
3. Place `vitalplates-0.1.0-fabric-26.3.jar` into your `.minecraft/mods` directory.
4. (Optional) Install [ModMenu](https://modrinth.com/mod/modmenu) to configure settings in-game.

---

## License

VitalPlates is licensed under the MIT License. You are free to include it in modpacks.
