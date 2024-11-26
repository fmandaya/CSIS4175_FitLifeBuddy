package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Exercise(
    val name: String,
    val videoUrl: String,
    val duration: Int,
    val description: String
)

data class ExerciseState(
    val progress: Float = 0f,
    val isChecked: Boolean = false
)

fun saveWorkoutProgress(
    userId: String,
    date: String,
    index: Int,
    state: ExerciseState
) {
    val firestore = FirebaseFirestore.getInstance()

    val documentKey = "$date-exercise_$index"
    val workoutProgress = mapOf(
        "progress" to state.progress,
        "isChecked" to state.isChecked,
        "date" to date,
        "exerciseIndex" to index
    )

    firestore.collection("users")
        .document(userId)
        .collection("workoutProgress")
        .document(documentKey)
        .set(workoutProgress)
        .addOnSuccessListener {
            Log.d("Firestore", "Workout progress saved successfully.")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error saving workout progress: ${e.message}")
        }
}

suspend fun loadExerciseStateByDate(userId: String, date: String, exercises: List<Exercise>): List<ExerciseState> {
    val firestore = FirebaseFirestore.getInstance()
    val snapshot = firestore.collection("users")
        .document(userId)
        .collection("workoutProgress")
        .whereEqualTo("date", date)
        .get()
        .await()

    // Initialize states to default
    val states = MutableList(exercises.size) { ExerciseState() }

    for (document in snapshot.documents) {
        val docId = document.id
        val index = docId.substringAfter("exercise_").toIntOrNull()

        if (index != null && index in states.indices) {
            val state = document.toObject(ExerciseState::class.java)
            if (state != null) states[index] = state
        }
    }
    return states
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutPlanScreen(navController: NavHostController, userId: String) {
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    // States
    var exercises by remember {
        mutableStateOf(
            listOf(
        Exercise(
            name = "Push-Ups",
            videoUrl = "https://www.youtube.com/watch?v=_l3ySVKYVJ8?rel=0&autoplay=1&fs=1&showinfo=0",
            duration = 3,
            description = "Strengthens your chest, shoulders, and triceps while engaging your core."
        ),
        Exercise(
            name = "Squats",
            videoUrl = "https://www.youtube.com/watch?v=aclHkVaku9U?rel=0&autoplay=1&fs=1&showinfo=0",
            duration = 3,
            description = "Focuses on your quads, hamstrings, and glutes for lower body strength."
        ),
        Exercise(
            name = "Lunges",
            videoUrl = "https://www.youtube.com/watch?v=QOVaHwm-Q6U?rel=0&autoplay=1&fs=1&showinfo=0",
            duration = 3,
            description = "Targets your glutes, quads, and stabilizing muscles for balance and mobility."
        ),
        Exercise(
            name = "Plank",
            videoUrl = "https://www.youtube.com/watch?v=pSHjTRCQxIw?rel=0&autoplay=1&fs=1&showinfo=0",
            duration = 3,
            description = "Builds core strength and stability while improving posture."
        ),
        Exercise(
            name = "Burpees",
            videoUrl = "https://www.youtube.com/watch?v=TU8QYVW0gDU?rel=0&autoplay=1&fs=1&showinfo=0",
            duration = 3,
            description = "A full-body workout for strength and cardiovascular endurance."
        )
            ))
    }


    var fitnessLevel by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var exerciseStates by remember { mutableStateOf(emptyList<ExerciseState>()) }
    val firestore = FirebaseFirestore.getInstance()

    // Fetch fitness level and update exercises
            LaunchedEffect(userId) {
                firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        fitnessLevel = document.getString("fitnessLevel") ?: "beginner"

                        // Adjust durations based on fitness level
                        val updatedDuration = when (fitnessLevel.lowercase()) {
                            "beginner" -> 3
                            "intermediate" -> 4
                            "advanced" -> 5
                            else -> 3
                        }
                        exercises = exercises.map { it.copy(duration = updatedDuration) }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error fetching fitness level: ${exception.message}")
                    }
                    .addOnCompleteListener { isLoading = false }
            }

    // Fetch data from Firestore for the current date
    LaunchedEffect(userId, currentDate) {
        firestore.collection("users")
            .document(userId)
            .collection("workoutProgress")
            .whereEqualTo("date", currentDate)
            .get()
            .addOnSuccessListener { snapshot ->
                val states = MutableList(exercises.size) { ExerciseState() } // Default states
                snapshot.documents.forEach { document ->
                    val index = document.id.substringAfter("exercise_").toIntOrNull()
                    val isChecked = document.getBoolean("isChecked") ?: false
                    val progress = (document.getLong("progress") ?: 0).toFloat()
                    if (index != null && index in states.indices) {
                        states[index] = ExerciseState(progress, isChecked)
                    }
                }
                exerciseStates = states
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching workoutProgress: ${exception.message}")
                isLoading = false
            }
    }

    // Calculate progress
    val overallProgress = if (exerciseStates.isEmpty()) 0f else {
        exerciseStates.count { it.isChecked }.toFloat() / exercises.size
    }

    // UI
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.bg_workoutplan),
                contentDescription = "Workout Plan Background",
                modifier = Modifier.fillMaxSize().alpha(0.5f),
                contentScale = ContentScale.Crop
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFD05C29))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp)
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
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 34.dp)
                    )
                    // Date
                    Text(
                        text = currentDate,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            fontSize = 22.sp,
                            color = Color(0xFFA3734F),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )


                    // Progress Bar
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = overallProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = Color(0xFFD05C29)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.Gray.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Overall Progress: ${(overallProgress * 100).toInt()}%",
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.quicksand_bold))
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Exercise List
                    LazyColumn {
                        itemsIndexed(exercises) { index, exercise ->
                            val state = exerciseStates.getOrNull(index) ?: ExerciseState()
                            ExerciseItem(
                                exercise = exercise,
                                state = state,
                                index = index,
                                userId = userId,
                                navController = navController
                            ) { newState ->
                                firestore.collection("users")
                                    .document(userId)
                                    .collection("workoutProgress")
                                    .document("$currentDate-exercise_$index")
                                    .set(
                                        mapOf(
                                            "progress" to newState.progress,
                                            "isChecked" to newState.isChecked,
                                            "date" to currentDate,
                                            "exerciseIndex" to index
                                        )
                                    )
                                    .addOnSuccessListener {
                                        exerciseStates = exerciseStates.toMutableList().apply {
                                            this[index] = newState
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Firestore", "Error updating workoutProgress: ${exception.message}")
                                    }
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
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Check/Uncheck Icon
            Image(
                painter = painterResource(id = if (state.isChecked) R.drawable.check else R.drawable.uncheck),
                contentDescription = if (state.isChecked) "Checked" else "Unchecked",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        coroutineScope.launch {
                            val updatedState = state.copy(
                                progress = if (!state.isChecked) 1f else 0f,
                                isChecked = !state.isChecked
                            )
                            onExerciseDone(updatedState)
                        }
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Details
            Column(
                modifier = Modifier.weight(1f).clickable {
                    val encodedUrl = URLEncoder.encode(exercise.videoUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        "youtube_player/$encodedUrl/${exercise.name}/${exercise.duration}/${exercise.description}"
                    )
                }
            ) {
                Text(
                    text = exercise.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold))
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.quicksand_regular))
                    )
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

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Add your BottomNavigationBar here
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.bg_exercise),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = exerciseName,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color(0xFFD05C29),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = description,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        fontWeight = FontWeight.Bold,
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
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.mediaPlaybackRequiresUserGesture = false

                            webViewClient = WebViewClient()

                            loadUrl(decodedUrl)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                        .background(Color.White) // Optional: Add a background color
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Timer Display
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 50.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD05C29),
                            contentColor = Color.White
                        ),
                    ) {
                        Text(
                            text = if (!isRunning) "Start" else "Pause",
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),
                        )
                    }

                    Button(
                        onClick = { resetTimer() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F8F8))
                    ) {
                        Text(text = "Reset",
                            color = Color.Gray,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),)
                    }
                }
            }
        }
    }
}
