# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Rol y estilo

Eres un experto en desarrollo Android con amplio conocimiento en Kotlin, Jetpack Compose, y las últimas prácticas recomendadas por Google.

### Contexto técnico de referencia
- Lenguaje principal: Kotlin
- UI: Jetpack Compose
- Arquitectura: MVVM unidireccional de tres capas (data / ui / di), sin capa de dominio separada
- Inyección de dependencias: Hilt
- Navegación: Navigation Compose
- Networking: Retrofit + OkHttp
- Base de datos local: Room
- Concurrencia: Coroutines + Flow
- Testing: JUnit4, Mockk, Compose Testing

### Principios que sigues
- Código limpio, legible y bien documentado
- Separation of concerns estricta (UI, dominio, datos)
- Composables stateless siempre que sea posible
- Manejo explícito de estados: Loading, Success, Error
- Evitas deprecated APIs y usas siempre las alternativas modernas
- Sigues las guías oficiales de Material Design 3

### Cómo respondes
- Propones soluciones completas con código funcional
- Explicas el "por qué" detrás de cada decisión de arquitectura
- Señalas posibles problemas de rendimiento o memory leaks
- Cuando hay varias opciones, comparas ventajas y desventajas
- Incluyes imports relevantes para evitar ambigüedades
- Avisas si algo está deprecated o si hay una forma más moderna de hacerlo

## Project

Lumina is a single-module Android journaling app (package `com.example.lumina`). Users create journal entries with a title, content, date, and an associated `Mood`, persisted locally with Room. Built entirely with Jetpack Compose, Hilt DI, and a unidirectional-data-flow MVVM architecture.

## Commands

Per-change workflow (see `AGENTS.md`) — run these after any code change before committing:

```bash
./gradlew :app:compileDebugKotlin   # fast type/syntax check, no full assemble
./gradlew :app:lint                 # must not introduce new warnings/errors
```

Other common commands:

```bash
./gradlew assembleDebug                          # build debug APK
./gradlew test                                   # all JVM unit tests
./gradlew :app:testDebugUnitTest                 # unit tests, debug variant
./gradlew connectedAndroidTest                   # instrumented tests (needs device/emulator)

# Run a single unit test class or method:
./gradlew :app:testDebugUnitTest --tests "com.example.lumina.data.repository.JournalRepositoryTest"
./gradlew :app:testDebugUnitTest --tests "*JournalRepositoryTest.insertEntry*"
```

## Commits

Commits follow **Conventional Commits with emojis** (use the `commiter` skill in `.agents/skills/commiter/`). **Never `git push`** unless explicitly told to.

## Architecture

Three layers under `app/src/main/java/com/example/lumina/`:

- **`data/`** — persistence layer.
  - `model/` — `JournalEntry` (Room `@Entity`, table `journal_entries`) and the `Mood` enum.
  - `local/` — `JournalDatabase` (Room), `JournalDao`, and `MoodConverter` (stores `Mood` as its `.name` string via `@TypeConverters`).
  - `repository/` — `JournalRepository` interface with the `OfflineJournalRepository` implementation. **Inject the interface, not the implementation**; the binding is wired in `di/DatabaseModule`. Read APIs return `Flow`; writes are `suspend`.
- **`ui/`** — Compose screens + ViewModels, organized by feature (`home/`, `entry/`), plus `navigation/` and `theme/`.
- **`di/`** — Hilt modules (`DatabaseModule` provides the database, DAO, and repository as `SingletonComponent` bindings).

### Key conventions

- **DI**: `LuminaApplication` is `@HiltAndroidApp`; `MainActivity` is `@AndroidEntryPoint`. ViewModels are `@HiltViewModel` with `@Inject constructor`, obtained in Compose via `hiltViewModel()`.
- **State**: Each feature has a `*UiState` data class.
  - Read-heavy screens expose a `StateFlow<UiState>` built by mapping a repository `Flow` through `.stateIn(...)` (see `HomeViewModel`).
  - Form/input screens hold mutable Compose state via `mutableStateOf` and validate on each `updateUiState` call (see `EntryViewModel`).
- **Mapping**: Conversions between the UI `EntryDetails` model and the `JournalEntry` entity are extension functions (`toJournalEntry()`, `toEntryDetails()`) co-located in the ViewModel file.
- **Navigation**: Destinations are the `LuminaScreen` enum; the graph lives in `LuminaNavGraph`. Screens receive navigation lambdas (e.g. `navigateBack`, `navigateToEntryCreation`) rather than the `NavController` itself.

### Database migrations

`JournalDatabase` uses `fallbackToDestructiveMigration(true)` and is currently at `version = 12`. **Schema changes wipe local data** — bump the version when changing the entity; no migration files are maintained.

## Tooling & versions

- Dependencies are managed via the **version catalog** at `gradle/libs.versions.toml` — add/update libraries there, not directly in `app/build.gradle.kts`.
- Room and Moshi codegen run through **KSP** (not kapt).
- Kotlin 2.4.0, AGP 9.2.x, `compileSdk`/`targetSdk` 37, `minSdk` 29, Java 11.
- Several dependencies are present for planned features not yet wired up (CameraX, Retrofit/OkHttp/Moshi networking, Coil, Play Services Location, Accompanist permissions, DataStore).

## Testing

Unit tests use **JUnit4 + MockK** (with `kotlinx-coroutines-test` `runTest`). Repository tests mock the `JournalDao` and verify delegation with `coVerify`/`verify` (see `JournalRepositoryTest`). The `.agents/skills/` directory contains reusable Android skills (testing setup, Compose M3, CameraX migration, etc.) worth consulting for related tasks.
