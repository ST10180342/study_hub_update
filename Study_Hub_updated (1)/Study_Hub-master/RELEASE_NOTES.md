# Release Notes

## Version 1.0 (Final PoE) – changes since the prototype

**Innovative / new features added in this version**
- **Single Sign-On (SSO):** "Sign in with Google" using Android Credential Manager, exchanged for a Supabase session.
- **Offline mode with sync:** Assignments created without internet are saved in a Room (SQLite) database and uploaded automatically by WorkManager when the connection returns. Saved assignments are also shown offline.
- **Real-time notifications:** Firebase Cloud Messaging push notifications (topic `study_hub_updates`), with Android 13+ permission handling and an in-app on/off setting.
- **Multi-language support:** English, isiZulu and Afrikaans, selectable in Settings and applied immediately.
- **Persistent settings:** notifications, reminders, dark mode and language are now saved between sessions (dark mode is applied).

**Improvements**
- Shared `InputValidator` (email format, password rules, assignment fields) so invalid input never reaches the network or crashes the app.
- Logging (`Log.d/w/e`, tag `StudyHub`) for authentication, sync and push events.
- Real unit tests of the validation logic; GitHub Actions now uploads the test report and debug APK.
- Release signing configuration for Play Store builds; see `PLAY_STORE_PREPARATION.md`.

**Fixes**
- `google-services.json` renamed (stray space in filename) so Firebase initialises.
- Room annotation processing (KSP) enabled; it was previously missing so the offline database could not compile.
- Login state now survives screen recreation (language/theme change).
