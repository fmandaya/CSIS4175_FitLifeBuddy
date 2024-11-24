package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.os.CountDownTimer
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class Exercise(
    val name: String,
    val videoUrl: String,
    val duration: Int, // Duration in minutes
    val description: String // Description of the exercise
)

@Composable
fun WorkoutPlanScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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

    // Initialize progress states for each exercise
    val progressStates = remember { mutableStateListOf(*Array(exercises.size) { 0f }) }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.bg_workoutplan),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp)
            ) {
                Text(
                    text = "Workout Plan",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp,
                        color = Color(0xFFD05C29),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(exercises) { index, exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            progress = progressStates[index],
                            navController = navController,
                            onDone = { progressStates[index] = 1f } // Update progress to 100% when done
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    progress: Float,
    navController: NavHostController,
    onDone: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val encodedUrl = URLEncoder.encode(exercise.videoUrl, StandardCharsets.UTF_8.toString())
                val encodedDescription = URLEncoder.encode(exercise.description, StandardCharsets.UTF_8.toString())
                navController.navigate(
                    "youtube_player/$encodedUrl/${exercise.name}/${exercise.duration}/$encodedDescription"
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8F8)),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = exercise.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = exercise.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0)) // Background color
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Color(0xFFD05C29)) // Progress color
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onExerciseDone() // Update progress to 100%
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD05C29))
            ) {
                Text(text = "Done", color = Color.White)
            }
        }
    }
}



