# ExoticGarden (1.21.X Edition) 🌿

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.X](https://img.shields.io/badge/Paper-1.21.X-blue.svg?style=flat-square&logo=curseforge)](https://papermc.io/)
[![Slimefun4](https://img.shields.io/badge/Slimefun4-RC--37-brightgreen.svg?style=flat-square)](https://github.com/Slimefun/Slimefun4)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)
[![CI & Release](https://img.shields.io/badge/GitHub_Actions-CI_%26_Release-blueviolet.svg?style=flat-square&logo=githubactions)](.github/workflows/build-and-release.yml)

**ExoticGarden** is an extensive farming, agriculture, and culinary addon for **[Slimefun 4](https://github.com/Slimefun/Slimefun4)**. It expands Minecraft with dozens of new plants, fruit trees, custom food recipes, juices, smoothies, magical essences, and kitchen machinery.

This version has been updated and modernized for **Minecraft / Paper / Purpur 1.21.X** running on **Java 21**.

---

## 🌟 Features

* 🌾 **Custom Crops & Bushes:** Harvest berries, vegetables, and exotic plants that integrate seamlessly into the Slimefun guide.
* 🌳 **Custom Fruit Trees:** Grow apples, oranges, pears, peaches, and more using custom in-game schematic generators.
* 🍳 **The Kitchen Machine:** Cook elaborate custom dishes, jelly sandwiches, and fruit pies.
* 🍹 **Juices & Smoothies:** Blend refreshing drinks that restore hunger and apply dynamic saturation effects.
* ✨ **Magical Plants:** Cultivate magical essences to produce rare resources.
* 🛠️ **Modern 1.21.X Compatibility:** Full support for `SHORT_GRASS`, modern particle systems, Paper Brigadier commands, and asynchronous block operations.
* ⚡ **Dual Build System:** Supports both **Maven** (`pom.xml`) and **Gradle** (`build.gradle.kts`) with shadow jar packaging.

---

## 📋 Requirements

| Requirement | Supported Version |
| :--- | :--- |
| **Server Software** | [Paper](https://papermc.io/), [Purpur](https://purpurmc.org/) or derivatives (1.21, 1.21.1, 1.21.3, 1.21.4+) |
| **Java Runtime** | Java 21 (LTS) or higher |
| **Core Dependency** | [Slimefun 4](https://github.com/Slimefun/Slimefun4) (RC-37 or newer) |

---

## 🚀 Installation

1. Download the latest `ExoticGarden.jar` from the **[Releases](https://github.com/Charmandiox9/ExoticGarden-SF/releases)** page.
2. Ensure **Slimefun 4** is installed in your server's `plugins/` directory.
3. Place `ExoticGarden.jar` into your `plugins/` folder.
4. Restart your server.

---

## 🛠️ Building from Source

You can build ExoticGarden using either Gradle or Maven:

### Option A: Using Gradle (Recommended)
```bash
# Windows
.\gradlew.bat build

# Linux / macOS
chmod +x gradlew
./gradlew build
```
The compiled `.jar` will be generated in `build/libs/`.

### Option B: Using Maven
```bash
mvn clean package
```
The compiled `.jar` will be generated in `target/`.

---

## 🤖 CI / CD & Automated Releases

This repository includes a modern GitHub Actions workflow ([`build-and-release.yml`](.github/workflows/build-and-release.yml)):
* **Automated Builds:** Validates every push and pull request with JDK 21.
* **Auto-Releases:** Creating a tag (e.g. `v1.21.0`) or pushing to `master`/`main` automatically creates a GitHub Release with formatted changelogs and standalone JAR assets.

---

## 📜 License & Credits

* **Original Author:** [TheBusyBiscuit](https://github.com/TheBusyBiscuit)
* **1.21.X Modernization:** [Charmandiox9](https://github.com/Charmandiox9) ([DevPingers](https://github.com/Charmandiox9))
* **License:** Licensed under the [GNU General Public License v3.0](LICENSE).
