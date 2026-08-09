# Weather2 Compat

Makes Minecraft use Weather2's (specifically [Weather Storms & Tornadoes](https://modrinth.com/mod/weather-storms-tornadoes)'s) weather checking system.

For example, farmland will grow if exposed to the sky during a rainstorm, bees will hide when it's raining, and lightning rods will sparkle during a thunderstorm.

[![NeoForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/supported/neoforge_vector.svg)](https://modrinth.com/mod/weather2-compat/versions?l=neoforge)

## Warning
Some Minecraft blocks use a different rain checking system. Compatibility with Weather2 cannot be easily added to it, nor can it easily support mods that use that system, but I am working to manually add compatibility.

## Probably unnoticeable changes
Strictly prevents sleeping to make thunderstorms pass (Weather2 breaks it anyway).

Makes (almost) all vanilla `LootParams.Builder` store their `LootContextParams.ORIGIN`.

This mod will not crash if Weather2 is not present, but the above two will still be applied.

## Downloads
[![Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/project/weather2-compat)
[![Github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/github_vector.svg)](https://github.com/ninjaguardian/Weather2Compat/releases)

## Links
[![Changelog](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/documentation/changelog_vector.svg)](https://modrinth.com/mod/weather2-compat/changelog)
[![GitHub](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/social/github-singular_vector.svg)](https://github.com/ninjaguardian)
[![Repository](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/git_vector.svg)](https://github.com/ninjaguardian/Weather2Compat)
[![Issues](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/documentation/issues_vector.svg)](https://github.com/ninjaguardian/Weather2Compat/issues)

[![MIT License](https://img.shields.io/badge/license-MIT-33c706.svg)](https://raw.githubusercontent.com/ninjaguardian/Weather2Compat/refs/heads/main/LICENSE)
