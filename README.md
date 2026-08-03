# ExoticGarden (1.21.X Edition) 🌿

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.X](https://img.shields.io/badge/Paper-1.21.X-blue.svg?style=flat-square&logo=curseforge)](https://papermc.io/)
[![Slimefun4](https://img.shields.io/badge/Slimefun4-RC--37%2B-brightgreen.svg?style=flat-square)](https://github.com/Slimefun/Slimefun4)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)
[![CI & Release](https://img.shields.io/badge/GitHub_Actions-CI_%26_Release-blueviolet.svg?style=flat-square&logo=githubactions)](.github/workflows/build-and-release.yml)

**ExoticGarden** is an extensive farming, agriculture, and culinary addon for **[Slimefun 4](https://github.com/Slimefun/Slimefun4)**. It expands Minecraft with dozens of new plants, fruit trees, custom food recipes, juices, smoothies, magical essences, and kitchen machinery.

This version has been completely refactored and modernized for **Minecraft / Paper / Purpur 1.21.X** running on **Java 21**, with full binary compatibility for the latest **Slimefun4 builds (RC-37+)**.

---

## 🌟 Features

- 🌾 **Custom Crops & Bushes:** Harvest berries, vegetables, and exotic plants that integrate seamlessly into the Slimefun guide.
- 🌳 **Custom Fruit Trees:** Grow apples, oranges, pears, peaches, and more using custom in-game schematic generators.
- 🍳 **The Kitchen Machine:** Cook elaborate custom dishes, jelly sandwiches, and fruit pies.
- 🍹 **Juices & Smoothies:** Blend refreshing drinks that restore hunger and apply dynamic saturation effects.
- ✨ **Magical Plants:** Cultivate magical essences to produce rare resources.
- 🛡️ **Modern Slimefun4 RC-37+ Compatibility:** Fully patched for Dough/Slimefun `SlimefunItemStack` decoupling, eliminating all runtime bytecode verification errors (`VerifyError`).
- 🛠️ **Modern 1.21.X Support:** Native support for `SHORT_GRASS`, modern particle systems, Paper Brigadier commands, and asynchronous world operations.
- ⚡ **Integrated Test Server:** Includes PaperMC's `run-paper` Gradle environment to test changes in an isolated server with 1 command.

---

## 📋 Requirements

| Requirement         | Supported Version                                                                             |
| :------------------ | :-------------------------------------------------------------------------------------------- |
| **Server Software** | [Paper](https://papermc.io/), [Purpur](https://purpurmc.org/) (1.21, 1.21.1, 1.21.3, 1.21.4+) |
| **Java Runtime**    | Java 21 (LTS) or higher                                                                       |
| **Core Dependency** | [Slimefun 4](https://github.com/Slimefun/Slimefun4) (RC-37 or newer)                          |

---

## 🚀 Installation

1. Download the latest `ExoticGarden.jar` from the **[Releases](https://github.com/Charmandiox9/ExoticGarden-SF/releases)** tab.
2. Place `Slimefun4.jar` and `ExoticGarden.jar` inside your server's `plugins/` directory.
3. Start or restart your server.

---

## 🛠️ Building and Testing Locally

### 📦 Building the Plugin Jar

```bash
# Windows
.\gradlew.bat build

# Linux / macOS
chmod +x gradlew
./gradlew build
```

The resulting shaded JAR file will be in `build/libs/ExoticGarden-1.21.0-SNAPSHOT.jar`.

### 🎮 Running the Local Test Server

You can launch a self-contained Paper 1.21.4 test server with Slimefun and ExoticGarden loaded:

```bash
# Windows
.\gradlew.bat runServer

# Linux / macOS
./gradlew runServer
```

---

## 📜 License & Credits

- **Original Author:** [TheBusyBiscuit](https://github.com/TheBusyBiscuit)
- **1.21.X Modernization & Compatibility Maintenance:** [Charmandiox9](https://github.com/Charmandiox9)
- **License:** Licensed under the [GNU General Public License v3.0](LICENSE).
