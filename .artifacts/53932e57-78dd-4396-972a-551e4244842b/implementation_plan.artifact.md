# Fix Kotlin KAPT Incompatibility with AGP 9.0

The project is currently using the `kotlin-kapt` plugin, which is incompatible with the "built-in Kotlin" support enabled by default in Android Gradle Plugin 9.0+. The recommended solution is to migrate from KAPT to KSP (Kotlin Symbol Processing).

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///E:/MyApplication/gradle/libs.versions.toml)
- Add KSP version `2.2.10-2.0.2` (matching the Kotlin version).
- Add `ksp` plugin definition.

#### [MODIFY] [build.gradle.kts](file:///E:/MyApplication/build.gradle.kts) (Top-level)
- Add the KSP plugin to the `plugins` block with `apply false`.

#### [MODIFY] [app/build.gradle.kts](file:///E:/MyApplication/app/build.gradle.kts)
- Replace `id("kotlin-kapt")` with `alias(libs.plugins.ksp)`.
- Replace `kapt(...)` with `ksp(...)` for the Room compiler dependency.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` (via `gradle_build`) to verify that the project compiles correctly with KSP.
- Run a Gradle sync to ensure the IDE recognizes the changes.
