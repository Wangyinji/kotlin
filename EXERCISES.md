# Student exercise — Room + SharedPreferences

Estimated time: 30–45 minutes. The UI and Room schema are provided. Your goal is
to replace temporary state with real local storage. No new libraries are needed.

## UP-1 — Read the local cache (20%)

File: `ListingRepository.kt`, method `getListings()`.

- Replace the `preview` read and Kotlin sorting with the DAO's database query.
- Return all persisted listings, sorted by monthly rent then ID.
- An empty database must return an empty list, not crash or generate samples.

Hint: find the `@Query` returning a list in `ListingDao`.

## UP-2 — Cache sample listings (25%)

File: `ListingRepository.kt`, method `cacheSamples()`.

- Save `SampleData.create()` through the DAO, replacing the memory-only logic.
- The button represents receiving a server response and caching it locally.
- Importing twice must still leave exactly six rows.
- Reimporting must preserve any existing favorite values.
- After UP-1 and UP-2, load samples, force-stop, relaunch **without pressing
  Load samples**: all six listings must still appear.

Hint: inspect the supplied insert's conflict strategy. Do not use REPLACE here.

## UP-3 — Persist favorites (25%)

File: `ListingRepository.kt`, method `setFavorite()`.

- Replace mutation of `preview` objects with the DAO's update operation.
- Update only the row with the requested ID.
- Saving/removing a favorite must survive force-stop and relaunch.
- Once all three Room tasks work, remove the unused `preview` field/imports.

Hint: the UI already reloads rows after each write. A UI-only toggle is insufficient.

## UP-4 — Restore user preferences (30%)

File: `UserPreferences.kt`, constructor and method `save()`.

Use the provided `SharedPreferences storage` with these exact keys and defaults:

| Key | Type | Default |
| --- | --- | --- |
| `language` | String | `en` |
| `favorites_only` | boolean | `false` |
| `budget_only` | boolean | `false` |

- UP-4a: restore the fields in the constructor. Only accept `en` / `fr`; use
  `en` for any other saved language value.
- UP-4b: keep the in-memory assignments and add persistent writes using an
  editor and `apply()`.
- Language and both filters must survive force-stop, relaunch and rotation.

Hint: choose `getString`/`getBoolean` for reads and the matching editor methods
for writes. Do not store structured listing records in SharedPreferences.

## Acceptance checklist

Use **Settings → Apps → LocalNest UP → Force stop**, or the command below.
Swiping away the recent-app card alone is not a reliable process-death test.

```powershell
adb shell am force-stop edu.isep.localnest.up
adb shell am start -n edu.isep.localnest.up/edu.isep.localnest.MainActivity
```

- [ ] Fresh install: empty state, English UI, both filters off.
- [ ] Load samples twice: six unique rows, ascending prices (520 → 1120).
- [ ] Favorite one item, reload samples: its favorite remains.
- [ ] Force-stop and relaunch: the six rows and favorite remain without reimport.
- [ ] Favorites-only shows just saved items; removing the last one shows an empty state.
- [ ] Budget-only shows four items with rent ≤ €800, with favorites-only disabled.
- [ ] Both filters combine using AND.
- [ ] Switch to French, enable budget-only, force-stop and relaunch: both remain.
- [ ] Turn off Wi-Fi/mobile data: all local actions still work.
- [ ] Rotate device: settings, rows and favorites restore from local storage.
- [ ] No main-thread database access and no crash.

## Optional automated check

`StorageContractTest.kt` contains two `@Ignore` persistence tests. They are
deliberately skipped in the starter because it is unfinished. When you implement
the tasks, remove those annotations and run `connectedDebugAndroidTest`.
`StarterSmokeTest` and `RoomDaoTest` should pass before any exercise changes.

## What to hand in

Commit your completed TODOs and a short explanation of why Room and
SharedPreferences are used for different data. Include evidence of a restart test
and name one difference between temporary memory and persistent local storage.
