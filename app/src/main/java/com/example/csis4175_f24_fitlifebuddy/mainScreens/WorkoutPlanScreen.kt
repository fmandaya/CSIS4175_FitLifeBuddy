package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.content.Context
import android.os.CountDownTimer
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Exercise(
    val name: String,
    val videoUrl: String,
    val duration: Int, // Duration in minutes
    val description: String // Description of the exercise
)

val firestore = FirebaseFirestore.getInstance()

data class ExerciseState(
    val progress: Float = 0f,
    val isChecked: Boolean = false
)

suspend fun saveExerciseState(userId: String, index: Int, state: ExerciseState) {
    firestore.collection("users")
        .document(userId)
        .collection("workoutProgress")
        .document("exercise_$index")
        .set(state)
        .await()
}

suspend fun loadExerciseState(userId: String, index: Int): ExerciseState {
    val document = firestore.collection("users")
        .document(userId)
        .collection("workoutProgress")
        .document("exercise_$index")
        .get()
        .await()

    return document.toObject(ExerciseState::class.java) ?: ExerciseState()
}


@Composable
fun WorkoutPlanScreen(navController: NavHostController, userId: String, modifier: Modifier = Modifier) {
    val exercises = listOf(
        Exercise(
            name = "Push-Ups",
            videoUrl = "https://www.youtube.com/watch?v=_l3ySVKYVJ8",
            duration = 3,
            description = "Strengthens your chest, shoulders, and triceps while engaging your core."
        ),
        Exercise(
            name = "Squats",
            videoUrl = "https://www.youtube.com/watch?v=aclHkVaku9U",
            duration = 3,
            description = "Focuses on your quads, hamstrings, and glutes for lower body strength."
        ),
        Exercise(
            name = "Lunges",
            videoUrl = "https://www.youtube.com/watch?v=QOVaHwm-Q6U",
            duration = 3,
            description = "Targets your glutes, quads, and stabilizing muscles for balance and mobility."
        ),
        Exercise(
            name = "Plank",
            videoUrl = "https://www.youtube.com/watch?v=pSHjTRCQxIw",
            duration = 3,
            description = "Builds core strength and stability while improving posture."
        ),
        Exercise(
            name = "Burpees",
            videoUrl = "https://www.youtube.com/watch?v=TU8QYVW0gDU",
            duration = 3,
            description = "A full-body workout for strength and cardiovascular endurance."
        )
    )

    val coroutineScope = rememberCoroutineScope()
    val exerciseStates = remember { mutableStateListOf<ExerciseState>() }

    // Fetch exercise states from Firebase
    LaunchedEffect(Unit) {
        exerciseStates.clear()
        coroutineScope.launch {
            exercises.indices.forEach { index ->
                val state = loadExerciseState(userId, index)
                exerciseStates.add(state)
            }
        }
    }

    // Calculate overall progress
    val overallProgress = if (exerciseStates.isEmpty()) 0f else {
        exerciseStates.count { it.isChecked }.toFloat() / exercises.size
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.bg_workoutplan),
                contentDescription = "Workout Plan Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
            ) {
                // Title
                Text(
                    text = "Workout Plan",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp,
                        color = Color(0xFFD05C29),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                // Overall Progress Bar
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(overallProgress)
                                .fillMaxHeight()
                                .background(Color(0xFFFF4E00))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .width(210.dp)
                            .background(
                                color = Color.Gray.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Overall Progress: ${(overallProgress * 100).toInt()}%",
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Exercise List
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(exercises) { index, exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            state = exerciseStates.getOrNull(index) ?: ExerciseState(),
                            index = index,
                            userId = userId,
                            navController = navController
                        ) { newState ->
                            coroutineScope.launch {
                                saveExerciseState(userId, index, newState)
                                exerciseStates[index] = newState
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ExerciseItem(
    exercise: Exercise,
    state: ExerciseState,
    index: Int,
    userId: String,
    navController: NavHostController,
    onExerciseDone: suspend (ExerciseState) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8F8)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Check/Uncheck icon based on Firebase state
            Image(
                painter = painterResource(if (state.isChecked) R.drawable.check else R.drawable.uncheck),
                contentDescription = if (state.isChecked) "Checked" else "Unchecked",
                modifier = Modifier
                    .size(48.dp)
                    .clickable(enabled = !state.isChecked) {
                        coroutineScope.launch {
                            val updatedState = ExerciseState(progress = 1f, isChecked = true)
                            onExerciseDone(updatedState)
                        }
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        val encodedUrl = URLEncoder.encode(exercise.videoUrl, StandardCharsets.UTF_8.toString())
                        val encodedDescription = URLEncoder.encode(exercise.description, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            "youtube_player/$encodedUrl/${exercise.name}/${exercise.duration}/$encodedDescription"
                        )
                    }
            ) {
                Text(
                    text = exercise.name,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.description,
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
            }
        }
    }
}









@Composable
fun YouTubePlayerScreen(
    navController: NavHostController,
    videoUrl: String,
    exerciseName: String,
    duration: Int,
    description: String,
    onExerciseDone: () -> Unit
) {
    val decodedUrl = URLDecoder.decode(videoUrl, StandardCharsets.UTF_8.toString())

    // Timer State
    var timeLeft by remember { mutableStateOf(duration * 60 * 1000L) }
    var isRunning by remember { mutableStateOf(false) }
    var timer: CountDownTimer? by remember { mutableStateOf(null) }
    val initialTime = duration * 60 * 1000L

    fun startOrResumeTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                timeLeft = 0
                isRunning = false
            }
        }.start()
        isRunning = true
    }

    fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    fun resetTimer() {
        timer?.cancel()
        timeLeft = initialTime
        isRunning = false
    }

    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_exercise),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.btn_backarrow),
            contentDescription = "Back Button",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .clickable { navController.navigateUp() }
                .size(48.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = exerciseName,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFFA3734F),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
            )

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl(decodedUrl)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )

            // Timer Display
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    color = Color(0xFF7B6F72)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Timer Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { if (!isRunning) startOrResumeTimer() else pauseTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6B89D))
                ) {
                    Text(
                        text = if (!isRunning) "Start" else "Pause",
                        color = Color.White
                    )
                }

                Button(
                    onClick = { resetTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F8F8))
                ) {
                    Text(text = "Reset", color = Color.Gray)
                }
            }
        }
    }
}



