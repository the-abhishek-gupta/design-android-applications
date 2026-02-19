Contributing to MovieMania

Thanks for wanting to contribute! This guide explains how to run the project locally, add features, and run tests.

Quick setup

1. Clone the repo

```bash
git clone <repo-url>
cd MovieMania
```

2. Open in Android Studio and let Gradle sync.
3. Ensure `google-services.json` is present under `app/` if you plan to test Firebase-backed features.
4. Use the Gradle wrapper for reproducible builds: `./gradlew`.

Branching & pull requests

- Create a feature branch off `main` named `feature/your-feature`.
- Keep PRs small and focused. Include screenshots for UI changes.
- Add tests for new logic when possible.

Code style & architecture

- Kotlin & Compose idioms.
- Use Hilt for dependency injection in new modules.
- Add database migrations for schema changes (Room).

Running and testing

- Build and run on device/emulator via Android Studio or:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

- Run unit tests:

```bash
./gradlew test
```

- Run instrumented tests (on a connected device/emulator):

```bash
./gradlew connectedAndroidTest
```

Adding a feature

- Add UI in `feature/yourfeature`, follow the project's pattern for composables.
- Add business logic in `domain/usecase` and expose it via DI in `di/module`.
- Persist required data in Room under `data/local` and add DAOs + Entities.
- Wire remote interactions in `data/remote` and update `MovieRepositoryImpl` if the feature alters sync semantics.

Sync & background

- WorkManager jobs are registered via Hilt worker factory (`App : Application`). Add workers under `feature/sync/worker` and register DI modules accordingly.

Questions

Open an issue or tag the maintainers in a PR with a short description and screenshots. Thanks!
