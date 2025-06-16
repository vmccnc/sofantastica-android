# Sofantastica Android Code Skeleton

This project provides a starting point for a native Android application that mirrors all pages and features of the Sofantastica website.

## Project Structure
```
app
 └─ src/main/java/pl/sofantastica
    ├─ data        # network, db, repositories
    ├─ domain      # use cases
    ├─ di          # Hilt modules
    └─ ui          # Compose screens & view models
```

The architecture follows MVVM with Clean Architecture boundaries.

## Build Setup
Gradle dependencies include Jetpack Compose, Hilt, Retrofit/Moshi, Room, Firebase, and Coroutines. Add your Firebase config files and set `BASE_URL` inside `NetworkModule.kt` to your backend.

## Hilt Modules
- `NetworkModule` – provides Retrofit service.
- `DatabaseModule` – provides Room database.
- `RepositoryModule` – binds repositories.
- `FirebaseModule` – provides Firebase Auth/Firestore/Storage/RemoteConfig.

## Usage
Each feature has a placeholder ViewModel and Compose screen located under `ui/`. Implement logic inside the respective use cases and repositories.

## Testing
Unit tests live under `app/src/test`.  Run them with:

```bash
./gradlew test
```

Instrumentation tests are placed in `app/src/androidTest` and can be executed on an emulator or device with:

```bash
./gradlew connectedAndroidTest
```

The repository provides example tests for the data layer and domain use cases to serve as a template for additional coverage.

