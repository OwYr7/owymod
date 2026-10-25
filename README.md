# OWYMOD

Eigenstaendige clientseitige Cheat-/Utility-Mod fuer **Minecraft 1.21.11 (Fabric)**.

* 18 Module in 3 Kategorien (PvP / Survival / Fun & Movement)
* Eigenes Modul-Framework mit Toggle, Slidern und Config-Persistenz
* Menue per rechter Umschalttaste (frei belegbar)
* Rein clientseitig - auf dem Server muss nichts installiert werden

## Build

Der Build laeuft automatisch per GitHub Actions (siehe `.github/workflows/build.yml`).

Manuell:

    gradle build

Die fertige JAR liegt danach in `build/libs/owymod-1.0.0.jar`.

## Installation

1. Fabric Loader fuer 1.21.11 installieren
2. Fabric API fuer 1.21.11 herunterladen (modrinth.com/mod/fabric-api/versions)
3. Beide JARs nach `.minecraft/mods/` kopieren
4. Fabric-Profil starten

## Steuerung

| Aktion | Taste |
| --- | --- |
| Menue oeffnen/schliessen | Rechte Umschalttaste / ESC |
| Modul umschalten | Klick auf den Schalter oder eigene Taste |
| Einstellungen | Modul anklicken -> rechts Regler |

## Lizenz

MIT
