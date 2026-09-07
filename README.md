# CRATEShop 1.0.0

CRATE SMP shop plugin for Purpur 26.2 / Java 25.

## Features
- `/shop` opens the GUI.
- `/crateshop npc` spawns an invulnerable, non-moving shop Villager at your position.
- Right-click the NPC to open the shop.
- Left click buys; right click sells; Shift + click uses x16.
- Prices and slots are fully configurable in `config.yml`.
- Uses Vault economy when Vault + an economy provider are installed.
- `/crateshop reload` reloads prices/config without restarting.

## Build
Requires JDK 25 and Maven 3.9+.
Run: `mvn clean package`
Output: `target/CRATEShop-1.0.0.jar`

## Server dependencies
- Purpur 26.2
- Vault
- An economy provider compatible with Vault (for example EssentialsX Economy)
