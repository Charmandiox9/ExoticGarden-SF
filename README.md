# ExoticGarden (1.21.X Edition) 🌿✨

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://adoptium.net/)
[![Paper 1.21.X](https://img.shields.io/badge/Paper-1.21.X-blue.svg?style=flat-square&logo=curseforge)](https://papermc.io/)
[![Slimefun4](https://img.shields.io/badge/Slimefun4-RC--37%2B-brightgreen.svg?style=flat-square)](https://github.com/Slimefun/Slimefun4)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)
[![CI & Release](https://img.shields.io/badge/GitHub_Actions-CI_%26_Release-blueviolet.svg?style=flat-square&logo=githubactions)](.github/workflows/build-and-release.yml)

**ExoticGarden** es una expansión integral de agricultura, botánica mágica, gastronomía e industria agrícola para **[Slimefun 4](https://github.com/Slimefun/Slimefun4)**. Añade decenas de nuevos cultivos, árboles frutales, recetas de cocina, jugos, un sistema completo de **Plantas Mágicas Multi-Tier (Tier I - IV)** y **maquinaria eléctrica automatizada** para cosechar, fertilizar y condensar recursos.

Esta edición ha sido completamente modernizada para **Minecraft / Paper / Purpur 1.21.X** bajo **Java 21**, garantizando compatibilidad binaria total con las versiones más recientes de **Slimefun4 (RC-37 / RC-38+)**.

---

## 🌟 Características Principales

### 🌾 1. Agricultura & Gastronomía
- **Cultivos & Frutos Silvestres:** Bayas (Arándano, Fresa, Frambuesa, Mora...), vegetales (Tomate, Lechuga, Cebolla, Ajo, Repollo, Patata dulce...) y especias.
- **Árboles Frutales Personalizados:** Manzanas, naranjas, peras, melocotones, cocos y más, con generadores de esquemas en el mundo.
- **Estación de Cocina (Kitchen):** Preparación de tartas, sándwiches, ensaladas y platos combinados.
- **Bebidas & Jugos:** Batidos y jugos naturales con efectos dinámicos de saturación y regeneración.

---

### 🔮 2. Botánica Mágica Multi-Tier (Tier I a IV)
Cultiva recursos de Minecraft y Slimefun mediante esencias mágicas que crecen sobre brotes colocados en tierra o césped:

- **22 Recursos Mágicos:** Carbón, Hierro, Oro, Diamante, Esmeralda, Netherita, Redstone, Lapis, Ender Pearl, Cuarzo, Glowstone, Obsidiana, Slime, Silicio, Zinc, Magnesio, Sulfato, Uranio, Acero, Duraluminio, Bronce, Latón, Aluminio, Cobre, Estaño, Plata, Plomo, Redstone Alloy, Ferrosilicon, Electro Magnet, Carbonado, Diamante Sintético, Hielo del Nether y Blistering Ingot.
- **Rendimiento Progresivo:**
  - **Tier I:** 1x Esencia por cosecha *(15 Niveles de EXP)*.
  - **Tier II:** 2x Esencias por cosecha *(22 Niveles de EXP)*.
  - **Tier III:** 4x Esencias por cosecha *(30 Niveles de EXP)*.
  - **Tier IV (Mítico):** 8x Esencias por cosecha = ¡Crafteo instantáneo del lingote/recurso! *(40 Niveles de EXP)*.
- **Crecimiento Versátil:** Crecen con polvo de hueso, bloques de hueso o fertilizante de Slimefun, tanto manualmente como mediante mecanismos de Dispensadores y Redstone vanilla.

---

### ⚡ 3. Maquinaria Agrícola & Automatización Industrial
Máquinas eléctricas con interfaz gráfica, almacenamiento de energía e integración con la red de **Slimefun Cargo**:

| Máquina | Tier | Área | Consumo / Capacidad | Descripción |
| :--- | :---: | :---: | :---: | :--- |
| **Cosechador Mágico Automático** | I | 5x5 | 24 J/s &bull; 128 J | Cosecha automáticamente los brotes maduros sin romper la planta. |
| **Cosechador Mágico Automático** | II | 9x9 | 48 J/s &bull; 256 J | Área ampliada y mayor velocidad de escaneo. |
| **Fertilizador Mágico Automático** | I | 5x5 | 32 J/s &bull; 128 J | Consume polvo/bloques de hueso o fertilizante para acelerar todos los cultivos en su área. |
| **Fertilizador Mágico Automático** | II | 9x9 | 64 J/s &bull; 256 J | Cobertura industrial de 9x9 para macrogranjas. |
| **Condensador de Esencias Mágicas** | — | — | 20 J/s &bull; 256 J | Sintetiza automáticamente 8 esencias en sus lingotes y materiales finales. |

> **Nota de Automatización de Cargo:**
> - Usa un **`Cargo Input Node`** en el Cosechador para extraer las esencias hacia la red de cables.
> - Usa un **`Cargo Output Node`** en el Fertilizador o en cofres de destino para alimentar o almacenar ítems.

---

### 📚 4. Árbol de Investigaciones con Experiencia

| Investigación | ID | Costo EXP | Contenido Desbloqueado |
| :--- | :---: | :---: | :--- |
| **Botánica Mágica (Tier I)** | `601` | 15 Niveles | 22 plantas mágicas Tier I y esencias base. |
| **Botánica Mágica (Tier II)** | `602` | 22 Niveles | 22 plantas mejoradas Tier II y esencias dobles. |
| **Botánica Mágica (Tier III)** | `603` | 30 Niveles | 22 plantas superiores Tier III y esencias cuádruples. |
| **Botánica Mágica (Tier IV)** | `604` | 40 Niveles | 22 plantas míticas Tier IV y esencias de síntesis rápida. |
| **Maquinaria Agrícola Básica** | `605` | 18 Niveles | Cosechador Mágico T1 y Fertilizador Mágico T1. |
| **Automatización e Industria Mágica** | `606` | 32 Niveles | Cosechador Mágico T2, Fertilizador Mágico T2 y Condensador. |
| **Cocina (Kitchen)** | `600` | 30 Niveles | Estación de Cocina para recetas gastronómicas. |

---

## 📋 Requisitos

| Requisito | Versión Soportada |
| :--- | :--- |
| **Servidor** | [Paper](https://papermc.io/), [Purpur](https://purpurmc.org/) (1.21, 1.21.1, 1.21.3, 1.21.4+) |
| **Java Runtime** | Java 21 (LTS) o superior |
| **Dependencia Principal** | [Slimefun 4](https://github.com/Slimefun/Slimefun4) (RC-37 o más reciente) |

---

## 🚀 Instalación

1. Descarga la versión compilada más reciente desde la pestaña **[Releases](https://github.com/Charmandiox9/ExoticGarden-SF/releases)**.
2. Coloca `Slimefun4.jar` y `ExoticGarden.jar` dentro de la carpeta `plugins/` de tu servidor.
3. Inicia o reinicia el servidor.

---

## 🛠️ Compilación Local y Pruebas

### 📦 Compilar el Jar del Plugin

```bash
# Windows
.\gradlew.bat build

# Linux / macOS
chmod +x gradlew
./gradlew build
```

El archivo JAR sombreado resultante se generará en: `build/libs/ExoticGarden-1.21.0-SNAPSHOT.jar`.

### 🎮 Iniciar Servidor de Pruebas Integrado

Para probar el plugin en un servidor Paper 1.21.4 limpio con Slimefun precargado:

```bash
# Windows
.\gradlew.bat runServer

# Linux / macOS
./gradlew runServer
```

---

## 📜 Licencia & Créditos

- **Autor Original:** [TheBusyBiscuit](https://github.com/TheBusyBiscuit)
- **Modernización 1.21.X, Botánica Mágica & Maquinaria:** [Charmandiox9](https://github.com/Charmandiox9)
- **Licencia:** Distribuido bajo la [GNU General Public License v3.0](LICENSE).
