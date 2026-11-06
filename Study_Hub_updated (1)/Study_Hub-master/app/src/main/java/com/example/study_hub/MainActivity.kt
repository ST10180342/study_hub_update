package com.example.study_hub

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.study_hub.ui.theme.Study_HubTheme

class MainActivity : ComponentActivity() {

    // Apply the language chosen in Settings (English / isiZulu / Afrikaans)
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SettingsStore.wrapWithLocale(newBase))
    }

    // Android 13+ runtime permission for push notifications
    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            Study_HubTheme(darkTheme = SettingsStore.darkMode(this)) {
                StudyHubApp()
            }
        }
    }
}

@Composable
fun StudyHubApp() {

    val authViewModel: AuthViewModel = viewModel()
    val timetableViewModel: TimetableViewModel = viewModel()
    val assignmentViewModel: AssignmentViewModel = viewModel()
    val examViewModel: ExamViewModel = viewModel()
    val flashcardViewModel: FlashcardViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()
    val studySessionViewModel: StudySessionViewModel = viewModel()

    var showRegister by remember { mutableStateOf(false) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    var selectedTab by remember {
        mutableStateOf(StudyHubTab.HOME)
    }

    var showTimetable by remember { mutableStateOf(false) }
    var showAssignments by remember { mutableStateOf(false) }
    var showExams by remember { mutableStateOf(false) }
    var showFlashcards by remember { mutableStateOf(false) }
    var showQuiz by remember { mutableStateOf(false) }
    var showTakeQuiz by remember { mutableStateOf(false) }
    var showFocusTimer by remember { mutableStateOf(false) }

    when {

        showTakeQuiz -> {
            TakeQuizScreen(
                viewModel = quizViewModel,
                onBack = {
                    showTakeQuiz = false
                }
            )
        }

        showQuiz -> {
            QuizScreen(
                viewModel = quizViewModel,
                onBack = {
                    showQuiz = false
                }
            )
        }

        showFlashcards -> {
            FlashcardsScreen(
                viewModel = flashcardViewModel,
                onBack = {
                    showFlashcards = false
                }
            )
        }

        showAssignments -> {
            AssignmentScreen(
                viewModel = assignmentViewModel,
                onBack = {
                    showAssignments = false
                }
            )
        }

        showExams -> {
            ExamScreen(
                viewModel = examViewModel,
                onBack = {
                    showExams = false
                }
            )
        }

        showTimetable -> {
            TimetableScreen(
                viewModel = timetableViewModel,
                onBack = {
                    showTimetable = false
                }
            )
        }

        showFocusTimer -> {
            FocusTimerWithSessionsScreen(
                viewModel = studySessionViewModel,
                onBack = {
                    showFocusTimer = false
                }
            )
        }

        showRegister -> {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    showRegister = false
                    isLoggedIn = true
                    selectedTab = StudyHubTab.HOME
                },
                onBack = {
                    showRegister = false
                }
            )
        }

        !isLoggedIn -> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    isLoggedIn = true
                    selectedTab = StudyHubTab.HOME
                },
                onRegister = {
                    showRegister = true
                }
            )
        }

        else -> {

            Scaffold(
                bottomBar = {
                    StudyHubBottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = {
                            selectedTab = it

                            showTimetable = false
                            showAssignments = false
                            showExams = false
                            showFlashcards = false
                            showQuiz = false
                            showTakeQuiz = false
                            showFocusTimer = false
                        }
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    when (selectedTab) {

                        StudyHubTab.HOME -> {

                            HomeScreen(
                                onPlanner = {
                                    selectedTab = StudyHubTab.PLANNER
                                },
                                onLearn = {
                                    selectedTab = StudyHubTab.LEARN
                                },
                                onFocus = {
                                    selectedTab = StudyHubTab.FOCUS
                                },
                                onSettings = {
                                    selectedTab = StudyHubTab.SETTINGS
                                },
                                onLogout = {
                                    authViewModel.logout {
                                        isLoggedIn = false
                                        selectedTab = StudyHubTab.HOME
                                    }
                                }
                            )
                        }

                        StudyHubTab.PLANNER -> {

                            if (showTimetable) {

                                TimetableScreen(
                                    viewModel = timetableViewModel,
                                    onBack = {
                                        showTimetable = false
                                    }
                                )

                            } else if (showAssignments) {

                                AssignmentScreen(
                                    viewModel = assignmentViewModel,
                                    onBack = {
                                        showAssignments = false
                                    }
                                )

                            } else if (showExams) {

                                ExamScreen(
                                    viewModel = examViewModel,
                                    onBack = {
                                        showExams = false
                                    }
                                )

                            } else {

                                PlannerScreen(
                                    onTimetable = {
                                        showTimetable = true
                                    },
                                    onAssignments = {
                                        showAssignments = true
                                    },
                                    onExams = {
                                        showExams = true
                                    }
                                )
                            }
                        }

                        StudyHubTab.LEARN -> {

                            if (showFlashcards) {

                                FlashcardsScreen(
                                    viewModel = flashcardViewModel,
                                    onBack = {
                                        showFlashcards = false
                                    }
                                )

                            } else if (showQuiz) {

                                QuizScreen(
                                    viewModel = quizViewModel,
                                    onBack = {
                                        showQuiz = false
                                    }
                                )

                            } else if (showTakeQuiz) {

                                TakeQuizScreen(
                                    viewModel = quizViewModel,
                                    onBack = {
                                        showTakeQuiz = false
                                    }
                                )

                            } else {

                                LearnScreen(
                                    onFlashcards = {
                                        showFlashcards = true
                                    },
                                    onQuiz = {
                                        showQuiz = true
                                    },
                                    onTakeQuiz = {
                                        showTakeQuiz = true
                                    }
                                )
                            }
                        }

                        StudyHubTab.FOCUS -> {

                            if (showFocusTimer) {

                                FocusTimerWithSessionsScreen(
                                    viewModel = studySessionViewModel,
                                    onBack = {
                                        showFocusTimer = false
                                    }
                                )

                            } else {

                                FocusScreen(
                                    onFocusTimer = {
                                        showFocusTimer = true
                                    }
                                )
                            }
                        }

                        StudyHubTab.SETTINGS -> {

                            SettingsScreen(
                                onBack = {
                                    selectedTab = StudyHubTab.HOME
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


// ==================================================
// LOGIN SCREEN
// ==================================================

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val message by viewModel.message.collectAsState()
    val context = LocalContext.current

    val cream = Color(0xFFFBFBFB)
    val blushPink = Color(0xFFF4D0D0)
    val taupe = Color(0xFFB7A09A)
    val charcoal = Color(0xFF3A3A3A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Study Hub",
            style = MaterialTheme.typography.headlineLarge,
            color = charcoal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.plan_learn_focus),
            style = MaterialTheme.typography.bodyLarge,
            color = taupe
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineSmall,
            color = charcoal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = taupe
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(stringResource(R.string.email))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(stringResource(R.string.password))
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = charcoal,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = {
                viewModel.login(
                    email = email,
                    password = password,
                    onSuccess = onLoginSuccess
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = blushPink,
                contentColor = charcoal
            )
        ) {
            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = charcoal
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Single Sign-On (Google)
        OutlinedButton(
            onClick = {
                viewModel.loginWithGoogle(context, onLoginSuccess)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = stringResource(R.string.login_google),
                color = charcoal
            )
        }
    }
}


// ==================================================
// REGISTER SCREEN
// ==================================================

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val cream = Color(0xFFFBFBFB)
    val blushPink = Color(0xFFF4D0D0)
    val taupe = Color(0xFFB7A09A)
    val charcoal = Color(0xFF3A3A3A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Study Hub",
            style = MaterialTheme.typography.headlineLarge,
            color = charcoal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Start your study journey",
            style = MaterialTheme.typography.bodyLarge,
            color = taupe
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.headlineSmall,
            color = charcoal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Create your account to access Study Hub.",
            style = MaterialTheme.typography.bodyMedium,
            color = taupe
        )

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(stringResource(R.string.full_name))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(stringResource(R.string.email))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(stringResource(R.string.password))
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text(stringResource(R.string.confirm_password))
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = charcoal,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Button(
            onClick = {
                viewModel.register(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    onSuccess = onRegisterSuccess
                )
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = blushPink,
                contentColor = charcoal
            )
        ) {
            Text(
                text = if (isLoading) {
                    "Creating Account..."
                } else {
                    "Register"
                },
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = stringResource(R.string.back_to_login),
                color = charcoal
            )
        }
    }
}


// ==================================================
// HOME SCREEN
// ==================================================

@Composable
fun HomeScreen(
    onPlanner: () -> Unit,
    onLearn: () -> Unit,
    onFocus: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {

    val cream = Color(0xFFFBFBFB)
    val blushPink = Color(0xFFF4D0D0)
    val softRose = Color(0xFFB78F8F)
    val taupe = Color(0xFFB7A09A)
    val charcoal = Color(0xFF3A3A3A)
    val muted = Color(0xFF6F6F6F)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = stringResource(R.string.welcome_back_home),
                        style = MaterialTheme.typography.bodyLarge,
                        color = taupe
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Study Hub",
                        style = MaterialTheme.typography.headlineLarge,
                        color = charcoal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Plan. Learn. Focus. Achieve.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted
                    )
                }

                IconButton(
                    onClick = onSettings,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = blushPink,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = charcoal
                    )
                }
            }
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = blushPink
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = stringResource(R.string.todays_focus),
                            style = MaterialTheme.typography.titleLarge,
                            color = charcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Make progress one focused session at a time.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = muted
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onFocus,
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            Text(stringResource(R.string.start_focus))
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(58.dp),
                        tint = softRose
                    )
                }
            }
        }

        item {

            Text(
                text = stringResource(R.string.quick_access),
                style = MaterialTheme.typography.headlineSmall,
                color = charcoal
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                HomeQuickCard(
                    modifier = Modifier.weight(1f),
                    title = "Planner",
                    description = "Classes & tasks",
                    icon = Icons.Default.CalendarMonth,
                    onClick = onPlanner
                )

                HomeQuickCard(
                    modifier = Modifier.weight(1f),
                    title = "Learn",
                    description = "Flashcards & quizzes",
                    icon = Icons.Default.MenuBook,
                    onClick = onLearn
                )
            }
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = softRose,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Column {

                            Text(
                                text = stringResource(R.string.todays_plan),
                                style = MaterialTheme.typography.titleLarge,
                                color = charcoal
                            )

                            Text(
                                text = "Keep your study day organised.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = muted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    HomePlanRow(
                        icon = Icons.Default.School,
                        title = "Timetable",
                        description = "Check your classes",
                        onClick = onPlanner
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HomePlanRow(
                        icon = Icons.Default.Assignment,
                        title = "Assignments",
                        description = "Review upcoming work",
                        onClick = onPlanner
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HomePlanRow(
                        icon = Icons.Default.MenuBook,
                        title = "Study",
                        description = "Continue learning",
                        onClick = onLearn
                    )
                }
            }
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = charcoal
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Your study journey",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Stay consistent and keep building your progress.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE0D2D2)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        HomeProgressItem(
                            value = "0",
                            label = "Sessions"
                        )

                        HomeProgressItem(
                            value = "0",
                            label = "Assignments"
                        )

                        HomeProgressItem(
                            value = "0",
                            label = "Quizzes"
                        )
                    }
                }
            }
        }

        item {

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}


// ==================================================
// HOME QUICK CARD
// ==================================================

@Composable
fun HomeQuickCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    val charcoal = Color(0xFF3A3A3A)
    val muted = Color(0xFF6F6F6F)
    val blushPink = Color(0xFFF4D0D0)
    val softRose = Color(0xFFB78F8F)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = softRose,
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        blushPink,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(9.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = charcoal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = muted
            )
        }
    }
}


// ==================================================
// HOME PLAN ROW
// ==================================================

@Composable
fun HomePlanRow(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    val charcoal = Color(0xFF3A3A3A)
    val muted = Color(0xFF6F6F6F)
    val blushPink = Color(0xFFF4D0D0)
    val softRose = Color(0xFFB78F8F)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFBFBFB)
        ),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = softRose,
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        blushPink,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(9.dp)
            )

            Spacer(modifier = Modifier.size(14.dp))

            Column {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = charcoal
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = muted
                )
            }
        }
    }
}


// ==================================================
// HOME PROGRESS ITEM
// ==================================================

@Composable
fun HomeProgressItem(
    value: String,
    label: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE0D2D2)
        )
    }
}