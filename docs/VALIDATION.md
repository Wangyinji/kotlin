# Verification

Date: 2026-11-16 (Asia/Taipei).

## Environment

- Windows; JDK 17.0.18 (Eclipse Adoptium).
- Gradle Wrapper 9.4.1, Android Gradle Plugin 9.2.1.
- AGP built-in Kotlin; KSP 2.3.9; Room 2.8.5.
- Compile/target SDK 37, minimum SDK 23, Build-Tools 36.0.0.
- Pixel_7 emulator running Android 15 (API 35).

## Results

```powershell
.\gradlew.bat assembleDebug lintDebug connectedDebugAndroidTest --console=plain
```

**BUILD SUCCESSFUL**. App and instrumentation tests compile from Kotlin sources.

- Debug APK generated successfully and installed/launched on the emulator.
- Android Lint: **0 errors, 2 warnings**. The warnings are a newer available
  Gradle version and the recommendation for newer Android backup rules.
  The pinned toolchain is intentional; app backup is disabled.
- Instrumentation: **6 tests, 4 passed, 2 skipped, 0 failures/errors**.
- `RoomDaoTest`: sorted/idempotent sample import and favorite update/preservation.
- `StarterSmokeTest`: launcher controls, importing six previews and toggling a favorite.
- `StorageContractTest`: two tests are explicitly ignored until students complete
  the Room and SharedPreferences TODOs. Their skips are not persistence passes.
- Emulator interaction check: load six samples, favorite one, verify favorites-only
  shows one item, disable favorites-only and enable the budget filter with French
  UI, verify four items, then force-stop/relaunch and verify an empty English
  starter. This confirms the intentional missing persistence is visible to students.
- Preview screenshot visually inspected; refreshed from the Kotlin APK at
  `docs/images/up-preview.png`.

## Limits

The UP intentionally does **not** persist its UI data or settings. The Room DAO
has been tested, but the repository does not call it until the exercise is done.
The SharedPreferences file is provided but its reads/writes are TODOs.
No successful persistent-restart behavior is claimed for this starter.

Android Studio import instructions are provided for a standard Gradle project;
verification used the same Gradle Wrapper from the command line and an emulator.
The IDE itself was not driven during verification. API 23 behavior was checked
by lint; the runtime test device was API 35.

Generated reports are local and excluded from Git:

- `app/build/reports/lint-results-debug.html`
- `app/build/reports/androidTests/connected/debug/index.html`
- `app/build/outputs/androidTest-results/connected/debug/`
