# Study Hub

## Group Members

| Name | Student Number | 
|---|---|
| Refilwe Phore | ST10458818 |
| Ntembeko Ndayi | ST10180342 |

Study Hub is an Android study and productivity application designed to help students organise their academic work, learning materials and study sessions in one place.
## 1. Purpose of the Application

The purpose of Study Hub is to combine academic planning, learning and productivity features into one simple Android application.

The application was designed based on the research completed in Part 1 of the assessment. The main ideas were influenced by existing study applications such as MyStudyLife, Quizlet and Forest.

Study Hub combines these ideas into one application rather than requiring students to use several separate applications.

## 2. Main Features

Study Hub currently includes:

- User registration
- User login and authentication
- Secure password handling through Supabase Auth
- Timetable management
- Assignment Manager
- Exam Planner
- Flashcards
- Quiz creation
- Practice quizzes
- Quiz scoring
- Focus Timer
- Study session recording
- Settings
- User logout
- Input validation and error handling
- **Single Sign-On** with Google (Supabase Auth ID-token flow)
- **Offline mode with sync** (Room/SQLite + WorkManager)
- **Real-time push notifications** (Firebase Cloud Messaging)
- **Multi-language** support: English, isiZulu, Afrikaans
- Persistent settings (notifications, reminders, dark mode, language)

## 3. Technology Used

The application was developed using:

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Supabase
- Supabase Auth
- Supabase PostgREST REST API
- PostgreSQL database
- GitHub
- GitHub Actions
- Room (SQLite), WorkManager
- Firebase Cloud Messaging
- Android Credential Manager (Google SSO)
- JUnit

## 4. Application Design

The application uses Jetpack Compose to create the user interface.

The design focuses on keeping the application simple and easy to navigate. The Home screen provides access to the main study features, while individual screens are used for timetable management, assignments, exams, flashcards, quizzes, the focus timer and settings.

The application uses ViewModels to separate application logic from the user interface. This helps keep database operations and authentication logic organised.

Input validation is also included to prevent invalid information from being submitted and to reduce the possibility of application crashes.

## 5. Authentication and Security

Study Hub uses Supabase Auth for user registration and login.

Passwords are not stored directly by the application. Authentication and password storage are handled by Supabase's authentication system.

The application communicates with Supabase over the network and uses a publishable client key. Private database passwords and service-role keys are not included in the application source code.

The project also uses Row Level Security (RLS) policies so that authenticated users can access their own stored study information.

## 6. REST API and Database

Study Hub uses the Supabase PostgREST API to communicate with the hosted database.

The application stores information such as:

- Timetable entries
- Assignments
- Exams
- Flashcards
- Quiz questions
- Completed study sessions

Each user's records are associated with their authenticated user ID.

This allows the application to retrieve and store study information through the hosted REST API rather than relying only on data stored locally on the phone.

## 6b. Offline Mode, SSO, Notifications and Languages

- **SSO:** `GoogleSignInHelper` obtains a Google ID token; `AuthViewModel.loginWithGoogle` signs in to Supabase with it. Requires `google.web.client.id` in `local.properties` and the Google provider enabled in Supabase.
- **Offline:** if saving an assignment fails (no internet) it is stored in Room (`OfflineRepository`) and `SyncWorker` uploads it when the network returns. Cached assignments are shown while offline.
- **Notifications:** `StudyHubMessagingService` shows FCM pushes. Send a test from Firebase Console → Messaging → target topic `study_hub_updates`.
- **Languages:** choose English / isiZulu / Afrikaans in Settings (`values`, `values-zu`, `values-af`).
- **Logging:** all key events are logged with tag `StudyHub`.

## 7. GitHub and Version Control

The project is maintained in a GitHub repository.

Git was used throughout development to keep track of changes and provide version control.

The project has been committed and pushed to GitHub during development, including the application source code, automated tests and GitHub Actions workflow.

## 8. Automated Testing

Automated testing was implemented using JUnit.

The project includes tests for registration validation, including:

- Valid registration information
- Empty name validation
- Password length validation
- Password confirmation validation

The tests are executed locally and through GitHub Actions.

The GitHub Actions workflow automatically:

1. Checks out the project.
2. Sets up the Java environment.
3. Sets up the Android environment.
4. Runs the unit tests.
5. Builds the debug APK.

The GitHub Actions workflow has been successfully executed and passed.

## 9. Error Handling

The application includes validation for common invalid inputs.

For example, the registration system checks that:

- A name has been entered.
- An email address has been entered.
- The password meets the minimum length requirement.
- The password and confirmation password match.

The login system also checks for missing email and password information.

Database and authentication operations use error handling so that failures can be displayed to the user rather than causing the application to crash.

## 10. Running the Application

To run the project:

1. Clone or download the repository from GitHub.
2. Open the project in Android Studio.
3. Allow Gradle to synchronise.
4. Configure the required Supabase connection properties.
5. Connect an Android device or start an Android emulator.
6. Run the application using Android Studio.

The Supabase URL and publishable key are kept outside the main source code using the project's local configuration.

## 11. Testing Device

The prototype was tested on an Android smartphone.

Testing included:

- Registration
- Login
- Logout
- Timetable
- Assignment Manager
- Exam Planner
- Flashcards
- Quiz creation
- Practice quizzes
- Focus Timer
- Settings
- Database saving and retrieval
- Invalid input handling

## 12. Demonstration Video

A demonstration video will be provided showing the Study Hub prototype running on an Android device.

The demonstration will cover:

- User registration
- User login
- Application navigation
- Timetable
- Assignment Manager
- Exam Planner
- Flashcards
- Quiz creation
- Practice Quiz
- Focus Timer
- Settings
- REST API/database functionality
- Logout

**Video link:** To be added before final submission.

## 13. Project Structure

The project is organised into different Kotlin files for the application's screens and ViewModels.

Examples include:

- `MainActivity.kt`
- `AuthViewModel.kt`
- `TimetableViewModel.kt`
- `AssignmentViewModel.kt`
- `ExamViewModel.kt`
- `FlashcardViewModel.kt`
- `QuizViewModel.kt`
- `StudySessionViewModel.kt`
- `SettingsScreen.kt`
- `TakeQuizScreen.kt`

The project also contains the automated testing and GitHub Actions workflow directories.

## 14. Conclusion

Study Hub provides a single Android application for managing academic planning, learning and study productivity.

The prototype demonstrates authentication, database connectivity, REST API communication, study management features, automated testing and version control. The application was developed iteratively and tested throughout the development process to identify and correct errors.

## 15. Release Notes (Final PoE Version)

### Version 1.0 – Final Portfolio of Evidence Release

**New and Improved Features since Prototype:**

- **Multi-language support**: English, Afrikaans and isiZulu. Language selector available in Settings.
- **Offline Mode foundation**: Room database entities and DAO for Assignments, Timetable and Flashcards with sync flags.
- **Settings enhancements**: Language selector, notifications toggle, study reminders and dark-mode switch.
- **GitHub Actions CI**: Automated unit tests + debug APK build on every push.
- **App icon and launcher assets** prepared for Play Store submission.

**Innovative features highlighted for this version:**
1. Integrated study ecosystem (Planner + Learn + Focus) in one app.
2. Supabase-backed authentication and REST API storage.
3. Localisation for South African languages (Afrikaans & isiZulu).
4. Offline-first data layer foundation using Room.

## 16. Preparation for Google Play Store Publication

- App icon and adaptive launcher icons present.
- versionCode = 1, versionName = "1.0".
- Target SDK 37, min SDK 26.
- INTERNET permission declared.
- Signed release APK can be generated after keystore configuration.
- Device screenshots ready for store listing.

## 17. Demonstration Video

**Video link:** [To be added – YouTube unlisted]

The video will demonstrate registration, login, settings (including language), REST API usage, offline foundations, and all core features.

## 18. AI Tools Usage (Final Write-up)

AI tools were used only as a limited supplementary aid for conceptual explanations, error diagnosis suggestions and design inspiration. All final code, architecture decisions, testing and documentation were completed by the group members (Refilwe Phore and Ntembeko Ndayi). AI did not generate complete features or replace student work.

## 19. Final Commit Tag

The final submission commit is tagged `finalPoE`.


## 11. Demonstration Video

Video link: **ADD YOUR UNLISTED YOUTUBE LINK HERE**

The video must show (with voice-over): register/login, SSO, settings, REST API data, offline mode + sync, a real-time notification, language change, and the data stored in Supabase/Firebase – recorded on a physical phone.

## 12. Release Notes, Play Store and AI Use

- [Release notes](RELEASE_NOTES.md)
- [Play Store preparation](PLAY_STORE_PREPARATION.md)
- [AI use write-up](AI_USE_WRITEUP.md)

## 13. References

- Firebase Cloud Messaging: https://firebase.google.com/docs/cloud-messaging [Accessed 03 November 2026]
- GitHub Actions Android build: https://github.com/marketplace/actions/automated-build-android-app-with-github-action [Accessed 03 November 2026]
