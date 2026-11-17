# Weather App Migration to Kotlin Multiplatform (TP)

Migrate the native Android Weather app to Kotlin Multiplatform (KMP), sharing domain logic, data sources, ViewModel, theme, screens, and Navigation 3 across Android, Desktop (JVM), and iOS.

## Proposed Changes

### [Dependencies & Build]
#### [MODIFY] [build.gradle.kts](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/build.gradle.kts)
- Add kotlinx.serialization plugin (`kotlin("plugin.serialization")`)
- Add Ktor client dependencies (`ktor-client-core`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, `ktor-client-logging`, `ktor-client-okhttp` for android/jvm, `ktor-client-darwin` for ios)
- Add Compose Material Icons Extended
- Add Lifecycle ViewModel Compose
- Add Coil 3 (`coil-compose`, `coil-network-ktor3`)
- Add Navigation 3 (`navigation3-ui`)

### [Domain & Data]
#### [NEW] [Weather.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/domain/Weather.kt)
- Weather data classes annotated with `@Serializable`

#### [NEW] [WeatherDataSource.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/data/WeatherDataSource.kt)
- `WeatherApiDataSource` and `WeatherFakeDataSource` implementation using Ktor

### [ViewModel]
#### [NEW] [MainViewModel.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/viewmodel/MainViewModel.kt)
- Shared ViewModel managing weather state and search queries across platforms.

### [Theme]
#### [NEW] [Color.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/ui/theme/Color.kt)
#### [NEW] [Type.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/ui/theme/Type.kt)
#### [NEW] [Theme.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/ui/theme/Theme.kt)
- Material 3 theme without dynamicColors for cross-platform consistency.

### [Screens & Navigation]
#### [NEW] [SearchScreen.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/ui/SearchScreen.kt)
#### [NEW] [DetailScreen.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/ui/DetailScreen.kt)
#### [NEW] [Navigation.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/navigation/Navigation.kt)
- Navigation 3 setup with `SavedStateConfiguration`, polymorphic `NavKey` (`SearchRoute`, `DetailRoute`), and `AppNavigation()`.

#### [MODIFY] [App.kt](file:///C:/Users/ewanl/StudioProjects/DevMobile/shared/src/commonMain/kotlin/com/legaut/devmobile/App.kt)
- Wire up `AppTheme` and `AppNavigation()`.

### [Manifest & Resources]
#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/ewanl/StudioProjects/DevMobile/androidApp/src/main/AndroidManifest.xml)
- Add `android.permission.INTERNET`.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:build` to verify successful compilation across all targets (Android, JVM, iOS metadata).

### Manual Verification
- Deploy and run `:androidApp` and `:desktopApp` to verify weather search, detail screens, and navigation.
