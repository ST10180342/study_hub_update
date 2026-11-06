# AI Use Write-up 

**Tool used:** Claude , accessed through claude.ai.

**What it was used for**
1. **Requirements audit** – I supplied the Part 3 brief and PoE rubric and asked Claude to compare them with my project. It reported that SSO, a working Room database, push notification handling and language switching were only partly implemented.
2. **Code generation (reviewed and adapted by me)** – Claude drafted: `GoogleSignInHelper.kt` (Credential Manager Google sign-in), `OfflineRepository.kt` / `SyncWorker` (Room + WorkManager sync), `StudyHubMessagingService.kt` (FCM), `SettingsStore.kt`, `InputValidator.kt`, the extra language strings (isiZulu/Afrikaans) and updated unit tests.
3. **Debugging/config** – Fixing the misnamed `google-services.json`, adding the KSP Room compiler and the GitHub Actions artifact steps.
4. **Documentation** – Drafts of the release notes and Play Store checklist.

**How it was checked:** I read each generated file, ran the app on a physical phone, and ran the unit tests locally and in GitHub Actions. Translations should be verified by a first-language speaker.

**Citations**
- Anthropic. 2026. Claude (claude.ai), accessed November 2026.
- Google. Credential Manager "Sign in with Google": https://developer.android.com/identity/sign-in/credential-manager-siwg
- Firebase Cloud Messaging: https://firebase.google.com/docs/cloud-messaging [Accessed 03 November 2025]
- Android Room: https://developer.android.com/training/data-storage/room
- Supabase Kotlin Auth (ID-token sign-in): https://supabase.com/docs/reference/kotlin/auth-signinwithidtoken


