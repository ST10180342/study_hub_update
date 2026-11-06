# Play Store Preparation Checklist

- [x] Unique application ID, versionCode 1 / versionName 1.0
- [x] App icon (all densities + adaptive) and `ic_launcher-playstore.png` (512×512)
- [x] Release signing config (`release.*` keys in `local.properties`)
- [x] Privacy-relevant permissions declared: INTERNET, ACCESS_NETWORK_STATE, POST_NOTIFICATIONS
- [ ] **Generate signed bundle/APK:** Android Studio → Build → Generate Signed Bundle / APK → keep the `.jks` safe (do NOT commit)
- [ ] **Add to repo/submission:** signed APK, ≥2 phone screenshots (1080×1920), feature graphic 1024×500, short (80 chars) and full description
- [ ] Screenshot of the app uploaded in Google Play Console (internal testing track)
- [ ] Privacy policy URL, content rating questionnaire, data-safety form
- [ ] Change `com.example.*` application ID before a real public release

Short description: *Plan, learn and focus – timetable, assignments, flashcards and quizzes in one app.*
