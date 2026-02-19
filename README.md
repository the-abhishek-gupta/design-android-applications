# MovieMania

MovieMania is an Android application that demonstrates a media-focused app built with modern Android libraries: Jetpack Compose, Hilt (with KSP), Room, Firebase Auth & Firestore, WorkManager, Media3 and Paging.

## Tech stack
- Kotlin
- Android (Gradle Kotlin DSL)
- Jetpack Compose
- Hilt (DI) with KSP
- Room (with KSP)
- Firebase Authentication & Firestore
- AndroidX Paging, WorkManager, Media3
- Google Identity / Credential Manager

## Prerequisites
- Android Studio
- JDK 11
- Android SDK / compileSdk 36
- Gradle wrapper included in the repository

## Quickstart
1. Clone the repo:

```bash
git clone <repo-url>
cd MovieMania
```

2. Open the project in Android Studio.

3. Firebase configuration
- The project expects `google-services.json` to be in the `app/` folder. (A `google-services.json` is already present under `app/` in this repo — verify it matches your Firebase project.)
- A Google Web Client ID is set in `app/build.gradle.kts` as `BuildConfig.WEB_CLIENT_ID`.

4. Build and run

```bash
# Assemble debug APK
./gradlew assembleDebug

# Install to connected device
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests on connected device
./gradlew connectedAndroidTest
```

## Notes
- KSP is used for annotation processing (Hilt and Room). Use the Gradle wrapper (`./gradlew`) to ensure processors run correctly.
- Application ID / package: `com.labs.systemdesignandroid` (see `app/build.gradle.kts`).
- Signing: configure signing in `app/build.gradle.kts` for `release` builds before running `assembleRelease`.

## Troubleshooting
- If Gradle or Android Studio behaves unexpectedly:
  - Run `./gradlew clean` and rebuild.
  - In Android Studio: File → Invalidate Caches / Restart.
  - Ensure `google-services.json` matches the Firebase project used by the app.

## Useful Gradle commands
- Clean: `./gradlew clean`
- Assemble release: `./gradlew assembleRelease`
- Lint: `./gradlew lint`

## Contributing
Contributions are welcome. Please open issues or PRs. Add tests for new behavior where applicable.

## License
TBD - add a license (MIT / Apache-2.0 / Proprietary) as appropriate.

