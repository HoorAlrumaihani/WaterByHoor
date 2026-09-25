<div align="center">

# 💧 WaterByHoor

**A tiny, private, offline water-tracking widget for Android.**
No accounts. No ads. No tracking. No backend. Just a home screen widget and one tap.

[![Build APK](https://github.com/YOUR-USERNAME/WaterByHoor/actions/workflows/build-apk.yml/badge.svg)](https://github.com/YOUR-USERNAME/WaterByHoor/actions/workflows/build-apk.yml)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Glance-4285F4?logo=jetpackcompose&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-26-informational)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

</div>

---

## Overview

WaterByHoor is deliberately small. The **home screen widget is the app** — you glance at
your progress, tap an amount, tap **add**, and optionally send your daily summary to your
coach on WhatsApp. That's it. There's no history screen, no charts, no notifications, no
sign-in, and no network permission — the app never talks to the internet on its own.

```
👀 Open home screen  →  💧 See 1750 / 2500 ml  →  🔢 Pick 500 ml  →  ➕ Add  →  📲 Send to coach
```

## Features

- **Home screen widget as the primary UI** — add water without ever opening the app
- **One shared source of truth** — the widget and the main app read/write the exact same data
- **Automatic daily reset at 5:00 AM** — no need to open the app for the day to roll over
- **Adjustable daily goal & default amount** — simple `[-] / [+]` steppers, no settings maze
- **One-tap WhatsApp summary** — pre-filled message, opens straight to your coach's chat
- **Fully offline** — zero `INTERNET` permission; only a `VIEW` intent to `wa.me` when you tap send
- **Light/Dark mode** — follows the system theme automatically
- **No ads, accounts, Firebase, analytics, or cloud sync** — and never will be

## Tech stack

| Layer            | Choice                                             |
|-------------------|-----------------------------------------------------|
| Language          | Kotlin                                             |
| Main app UI       | Jetpack Compose (Material 3)                       |
| Widget UI         | [Jetpack Glance](https://developer.android.com/jetpack/androidx/releases/glance) for AppWidgets |
| Storage           | `SharedPreferences` — a single, simple source of truth shared by app & widget |
| Build             | Gradle (Kotlin DSL), Android Gradle Plugin 8.5.2   |
| CI                | GitHub Actions — builds a debug APK on every push  |

## Why Glance, and why buttons instead of a slider?

Home screen widgets are backed by Android's `RemoteViews`, which has no interactive slider
component. Jetpack Glance (the current, official Compose-style toolkit for widgets) doesn't
expose one either. Instead of a fragile workaround, the amount picker uses a row of preset
chips (`10 / 50 / 100 / 200 / 250 / 300 / 330 ml`) with the active one highlighted — one tap,
instant feedback, no compromise on reliability.

## Project structure

```
WaterByHoor/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/hoor/waterbyhoor/
│       │   ├── WaterRepository.kt      # single source of truth (SharedPreferences)
│       │   ├── DateUtils.kt            # "logical day" that rolls over at 5:00 AM
│       │   ├── WhatsAppHelper.kt       # builds & sends the daily summary via wa.me
│       │   ├── MainActivity.kt         # minimal Compose screen (goal / default amount)
│       │   └── widget/
│       │       ├── WaterWidget.kt          # the Glance widget UI
│       │       ├── WaterWidgetReceiver.kt  # system entry point
│       │       └── actions/                # AddWater / SelectAmount / SendWhatsApp
│       └── res/                        # icons, colors (incl. dark mode), widget metadata
└── .github/workflows/build-apk.yml     # CI: builds & releases a debug APK
```

## Getting a build

### Option A — GitHub Actions (no Android Studio needed)

Every push to `main` triggers [`build-apk.yml`](.github/workflows/build-apk.yml), which
compiles a debug APK on GitHub's runners and attaches it to a new
[Release](../../releases) — downloadable straight from a phone browser.

1. Push (or upload) this repo to GitHub.
2. Open the **Actions** tab and wait for the run to finish.
3. Grab `app-debug.apk` from **Releases** or the workflow's **Artifacts**.
4. Install it (enable "install from this source" the first time Android asks).

### Option B — Android Studio

```bash
git clone https://github.com/YOUR-USERNAME/WaterByHoor.git
```

Open the folder in Android Studio, let it sync (it will generate the Gradle wrapper
automatically), then **Run ▶** on a device or emulator.

## Adding the widget

Long-press an empty spot on your home screen → **Widgets** → **WaterByHoor** → drag it in.

## Privacy

WaterByHoor requests **no `INTERNET` permission**. All data lives in local
`SharedPreferences` on your device. The only outbound action the app ever performs is
opening WhatsApp (or a browser fallback) via a standard `Intent` when you explicitly tap
**"Send to coach"** — at which point WhatsApp, not this app, handles the network request.

## License

MIT — feel free to adapt for personal use.
