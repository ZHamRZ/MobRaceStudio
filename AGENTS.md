# Mob Race Studio — AGENTS.md

## Stack
- Minecraft 1.20.1, Fabric Loader 0.14.22, Fabric API 0.92.9
- Java 17, Gradle (Fabric Loom 1.5-SNAPSHOT, Yarn mappings)
- No mixins currently; no commands (GUI-only)

## Run from IntelliJ
- **File → Open** `C:\Users\zamha\AppData\Roaming\.minecraft\MobRaceStudio`
- Gradle sync → `Tasks → fabric → runClient`

## Key entrypoints
| Role | Class |
|------|-------|
| Server init | `com.mobracestudio.MobRaceStudio` |
| Client init | `com.mobracestudio.MobRaceStudioClient` |

## In-game access
- Press **M** to open the main menu (keybinding registered in `ClientEvents.java`)
- All configuration is GUI-only; no commands exist

## Package map
| Package | Responsibility |
|---------|---------------|
| `registry/` | Entity auto-discovery via `Registries.ENTITY_TYPE` — scans all mod entities at startup in `EntityRegistry.discoverAllEntities()` |
| `arena/` | Data model (`Arena`, `Checkpoint`, `SpawnPoint`, etc.) + `ArenaManager` (CRUD) + `ArenaStorage` (JSON persistence) |
| `config/` | `MobConfig` (15+ properties per mob), `MobConfigManager` |
| `entity/` | `RaceMobSpawner` — spawns existing mob types and injects custom AI |
| `ai/` | Custom waypoint-based AI: `SmartMovement` orchestrates `CheckpointGoal`, `ObstacleDetection`, `JumpDetection`, `WaterRecoveryGoal`, `LadderGoal`, `AntiStuckGoal` |
| `race/` | State machine: `RaceManager` (IDLE→COUNTDOWN→RACING→FINISHED→STOPPED), `RaceTimer`, `LapManager`, `Leaderboard`, `RankingSystem` |
| `network/` | 3 packet channels: `arena_sync`, `race_state`, `mob_config` |
| `gui/widget/` | 13 custom widgets — do NOT add vanilla Minecraft widgets (all UI uses `Modern*` classes) |
| `gui/screen/` | 7 screens: `MainMenuScreen`, `ArenaEditorScreen`, `MobConfigScreen`, `RaceControlScreen`, etc. |
| `editor/` | In-world editor with 12 tools (`EditorTool` enum) |
| `storage/` | `StorageManager` + `JsonHelper` — JSON in `world/data/mobracestudio/` |
| `event/` | `ClientEvents` (keybind M), `ServerEvents` (auto-load/save on server start/stop) |

## Storage
- Arena files → `<world>/data/mobracestudio/arenas/<id>.json`
- Mob configs → `<world>/data/mobracestudio/mobconfigs/mob_configs.json`
- Uses Gson (bundled with Minecraft), serializes `Vec3d` via custom adapter in `ArenaStorage`
- Arena ID is UUID prefix (8 chars); name must be unique

## GUI framework rules
- `Identifier.of()` does NOT exist in 1.20.1 → use `new Identifier(ns, path)` instead
- All `Modern*` widgets are custom; `Screen` methods work as normal
- `init()` has no `DrawContext` — rendering code goes in `render()` only
- `GuiRenderHelper` provides shared constants and utilities

## Entity scanning
- Called in both `MobRaceStudio.onInitialize()` and `MobRaceStudioClient.onInitializeClient()`
- Skips re-scan if already discovered
- Categorises entities as HOSTILE, PASSIVE, BOSS, VILLAGER, or OTHER
- Use `Identifier.tryParse()` for string→entity lookup; `Registries.ENTITY_TYPE.get(id)` to resolve

## Build notes
- `fabric.mod.json` has `${...}` placeholders expanded by Gradle `processResources`
- Mods directory at `%userprofile%/AppData/Roaming/.minecraft/mods` is used for `modCompileOnly` references (not shipped with mod)
- No Gradle wrapper checked in; IntelliJ creates it on import or run `gradle wrapper`
- `loom_version=1.5-SNAPSHOT`, `loader_version=0.14.22`, `fabric_version=0.92.9+1.20.1`
