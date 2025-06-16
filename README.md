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
Add unit tests under `src/test` using repository mocks and Compose tests under `src/androidTest`.

