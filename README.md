# Студентський Асистент (Student Assistant)

A native Android app for students of Zhytomyr Polytechnic State University — class schedule, personal notes, and one-click practice report generation (Word/PDF), built with 100% Jetpack Compose.

## Features

- **Schedule** — weekly class schedule grouped by day, swipeable with `HorizontalPager`, day picker via `ScrollableTabRow`. Backed by a Retrofit `ZtuApi` interface (currently served through a local mock interceptor, ready to be pointed at a real endpoint).
- **Notes** — a Room-backed notebook for saving per-subject notes, with add/delete and an empty-state screen.
- **Reports** — generates a formatted internship/practice report as a `.docx` (Apache POI) or `.pdf` (Android `PdfDocument`) from a short form, and opens the result directly via a `FileProvider`.

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Local storage | Room |
| Networking | Retrofit + OkHttp + Gson |
| Documents | Apache POI (`.docx`), Android `PdfDocument` (`.pdf`) |
| Build | Gradle Kotlin DSL, version catalog (`libs.versions.toml`) |

**Min SDK:** 26 · **Target/Compile SDK:** 37

## Project structure

```
app/src/main/java/com/artem/myapplication/
├── MainActivity.kt          # App shell: Scaffold, top bar, bottom navigation, Notes & Reports screens
├── ScheduleScreen.kt        # Schedule tab: day tabs + swipeable class list
├── ScheduleViewModel.kt     # Loads schedule state from the network layer
├── NotesViewModel.kt        # Room-backed notes CRUD
├── PdfGenerator.kt          # .pdf report generation
├── WordGenerator.kt         # .docx report generation
├── PracticeReport.kt        # Report data model
├── db/                      # Room entities, DAO, database
├── network/                 # Retrofit client, API interface, DTOs
└── ui/theme/                # Color palette, typography, spacing, Material3 theme
```

## Design system

The UI runs on a single deliberate color palette (`ui/theme/Color.kt`) — no default template colors, no dynamic per-device theming — with full light/dark Material 3 color roles, a tuned type scale (`ui/theme/Type.kt`), and an 8dp spacing/radius scale (`ui/theme/Dimens.kt`) used consistently across all three screens.

## Getting started

1. Clone the repo and open it in Android Studio (Ladybug or newer recommended).
2. Let Gradle sync — dependencies are pinned via `gradle/libs.versions.toml`.
3. Run the `app` configuration on a device/emulator with API 26+.

### Build from the command line

```bash
./gradlew :app:assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/`.

## Permissions

- `INTERNET` — for fetching the schedule.
- A `FileProvider` is configured to share generated report files with other apps (e.g. a PDF/Word viewer) without exposing raw file paths.
