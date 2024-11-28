package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.BuildConfig
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.utilities.model.UserManager
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.ApiClient
import com.example.csis4175_f24_fitlifebuddy.utilities.Quote
import com.example.csis4175_f24_fitlifebuddy.utilities.QuoteApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random


@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val scope = rememberCoroutineScope()
    var quote by remember { mutableStateOf<Quote?>(null) }
    val apiKey =  BuildConfig.QUOTES_API_KEY // Replace with your actual API key
    // Water tracker state: 8 glasses, all initially empty
    val waterTracker = remember { mutableStateListOf(false, false, false, false, false, false, false, false) }
    val lastResetDate = remember { mutableStateOf<String?>(null) }
    // Get today's date
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
    val context = LocalContext.current

    // Fetch fitness quotes
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val quote1 = ApiClient.quoteApiService.getQuotes("fitness", apiKey)
                val quote2 = ApiClient.quoteApiService.getQuotes("food", apiKey)
                //Log.d("API_CALL", "Quotes fetched: $quotes")
                quote = if (Random.nextInt(2) == 0) quote1.random() else quote2.random();

                fetchWaterTrackerFromFirebase(UserManager.documentReferenceID) { savedState ->
                    savedState.forEachIndexed { index, isFilled ->
                        waterTracker[index] = isFilled
                    }
                }
                /*
                if (lastResetDate.value != currentDate) {
                    lastResetDate.value = currentDate
                    waterTracker.fill(false) // Reset the tracker
                    saveWaterTrackerToFirebase(UserManager.documentReferenceID, waterTracker) // Update in Firestore
                } else {
                    fetchWaterTrackerFromFirebase(UserManager.documentReferenceID) { savedState ->
                        savedState.forEachIndexed { index, isFilled ->
                            waterTracker[index] = isFilled
                        }
                    }
                }

                 */
            } catch (e: Exception) {
                Log.e("API_CALL", "Error: ${e.message}")
                quote = Quote("Stay positive and keep moving!", "Anonymous")
            }
        }
    }


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.homepage_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Content with responsive padding applied
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(innerPadding).padding(top = screenHeight * 0.3f, bottom = 5.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Welcome text
                Text(
                    text = "Welcome, " + UserManager.userName + "!",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    color = Color.Black,
                    modifier = Modifier.padding(
                        top = screenHeight * 0.12f,
                    )
                )
                // Greeting message
                /* Text(
                    text = "We're excited to have you on your fitness journey! Explore the app to get started.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                ) */

                // Display the quote in a fixed-size box
                Box(
                    modifier = Modifier
                        .width(340.dp)
                        .height(100.dp),
                    contentAlignment = Alignment.Center // Align content to the center of the box
                ) {
                    quote?.let {
                        Text(
                            text = "\"${it.quote}\" - ${it.author ?: "Unknown"}",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                color = Color(0xFF26425A),
                                fontSize = 16.sp
                            ),
                            maxLines = 5, // Limit the number of lines to avoid overflow
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Add ellipsis for overflow text
                        )
                    } ?: Text(
                        text = "Fetching your motivational quote...",
                        fontSize = 16.sp,
                        color = Color(0xFF26425A),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Let's get Started!",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp, top = 15.dp)

                )
                // Buttons: Workout and Log Food
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    OrangeButton(text = "Workout") {
                        // Navigate to workout screen
                        navController.navigate("workout_plan_screen")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OrangeButton(text = "Log Food") {
                        // Navigate to log food screen
                        navController.navigate("nutrition_history_screen")
                    }
                }



                // Row for glasses aligned at the bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize().align(Alignment.CenterHorizontally)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Daily Water Tracker",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            waterTracker.forEachIndexed { index, isFilled ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        painter = painterResource(
                                            id = if (isFilled) R.drawable.full_cup else R.drawable.empty_cup
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .size(38.dp)
                                            .clickable {
                                                // Toggle the state of the clicked glass
                                                waterTracker[index] = !isFilled
                                                val mediaPlayer = MediaPlayer.create(context, R.raw.water) // Replace with your sound file
                                                mediaPlayer.start()
                                                mediaPlayer.setOnCompletionListener {
                                                    mediaPlayer.release() // Release resources when done
                                                }
                                                saveWaterTrackerToFirebase(UserManager.documentReferenceID, waterTracker)
                                            }
                                    )
                                    Text(
                                        text = "8oz",
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveWaterTrackerToFirebase(userId: String, trackerState: List<Boolean>) {
    val db = FirebaseFirestore.getInstance()
    val waterTrackerData = hashMapOf(
        "waterTracker" to trackerState,
        "timestamp" to System.currentTimeMillis() // Optional: For debugging purposes
    )

    db.collection("users")
        .document(userId) // Access the user document
        .collection("waterTracker") // Use "waterTracker" collection
        .document("current") // Overwrite or create a single document named "current"
        .set(waterTrackerData) // Overwrite with the latest state
        .addOnSuccessListener {
            Log.d("Firebase", "Water tracker data saved successfully.")
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error saving water tracker data: ${e.message}")
        }
}


fun fetchWaterTrackerFromFirebase(userId: String, onResult: (List<Boolean>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(userId) // Access the user document
        .collection("waterTracker") // Access the "waterTracker" collection
        .document("current") // Fetch the "current" document
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val trackerState = document.get("waterTracker") as? List<Boolean> ?: emptyList()
                onResult(trackerState)
            } else {
                Log.d("Firebase", "No water tracker data found.")
                onResult(emptyList())
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error fetching water tracker data: ${e.message}")
            onResult(emptyList())
        }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(navController = rememberNavController())
        }
    }
}





@Composable
fun OrangeButton(text: String, onClick: () -> Unit) {
    androidx.compose.material3.Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD05C29)),
        modifier = Modifier
           // Distribute buttons equally
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
            color = Color.White
        )
    }
}

