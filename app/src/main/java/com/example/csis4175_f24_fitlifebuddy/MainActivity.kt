package com.example.csis4175_f24_fitlifebuddy

import FoodSearchScreen
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.LoginScreen
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.RegisterScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.GymFinderScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.HomeScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.ProfileScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens.NutritionHistoryScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.WorkoutPlanScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.YouTubePlayerScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens.FoodDetailsScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenOne
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenThree
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenTwo
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SelectFitnessLevelScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SplashScreen
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import com.example.csis4175_f24_fitlifebuddy.utilities.model.FoodResponse
import com.example.csis4175_f24_fitlifebuddy.utilities.model.UserManager
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.tasks.await
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FitLifeBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var auth = Firebase.auth;
                    val db = FirebaseFirestore.getInstance()
                    val navController = rememberNavController()
                    val viewModel = FoodSearchViewModel()
                    if (!Places.isInitialized()) {
                        Places.initialize(this, BuildConfig.API_KEY)
                    }
                    NavHost(navController = navController, startDestination = "splash_screen") {
                        composable("splash_screen") {
                            SplashScreen(authenticator = auth, navController = navController)
                        }
                        composable("onboarding_screen_one") {
                            OnboardingScreenOne(navController = navController)
                        }
                        composable("onboarding_screen_two") {
                            OnboardingScreenTwo(navController = navController)
                        }
                        composable("onboarding_screen_three") {
                            OnboardingScreenThree(navController = navController)
                        }
                        composable("login_screen") {
                            // Get the current user's UID

                            LoginScreen( authenticator = auth, database = db,navController = navController)
                        }
                        composable("register_screen") {
                            // Get the current user's UID

                            RegisterScreen(authenticator = auth, database = db, navController = navController)
                        }
                        composable("fitness_level_screen") {
                            // Get the current user's UID
                            SelectFitnessLevelScreen(database = db, navController = navController)
                        }
                        composable("home_screen") {
                            HomeScreen(navController = navController)
                        }
                        composable("workout_plan_screen") {
                            WorkoutPlanScreen(navController = navController)
                        }
                        composable(
                            route = "youtube_player/{videoUrl}/{exerciseName}/{duration}/{description}",
                            arguments = listOf(
                                navArgument("videoUrl") { type = NavType.StringType },
                                navArgument("exerciseName") { type = NavType.StringType },
                                navArgument("duration") { type = NavType.IntType },
                                navArgument("description") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                            val exerciseName = backStackEntry.arguments?.getString("exerciseName") ?: "Exercise"
                            val duration = backStackEntry.arguments?.getInt("duration") ?: 3
                            val description = try {
                                URLDecoder.decode(
                                    backStackEntry.arguments?.getString("description") ?: "No description available",
                                    StandardCharsets.UTF_8.toString()
                                )
                            } catch (e: Exception) {
                                "No description available"
                            }

                            YouTubePlayerScreen(
                                navController = navController,
                                videoUrl = videoUrl,
                                exerciseName = exerciseName,
                                duration = duration,
                                description = description,
                                onExerciseDone = { /* Update progress in WorkoutPlanScreen */ }
                            )
                        }
                        composable("nutrition_history_screen") {
                            NutritionHistoryScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("food_search_screen") {
                            FoodSearchScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("food_details_screen") {
                            FoodDetailsScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("gym_finder_screen") {
                            GymFinderScreen(navController = navController)
                        }
                        composable("profile_screen") {
                            ProfileScreen(navController = navController)
                        }
                        // composable("profile_screen") { /* Profile code here */ }
                        // composable("exercise_demos_screen") { /* Exercise Demos code here */ }
                        // composable("progress_tracker") { /* Progress Tracker code here */ }
                    }
                }
            }
        }
    }
}

suspend fun initializeApp(
    authenticator: FirebaseAuth = Firebase.auth,
    database: FirebaseFirestore = FirebaseFirestore.getInstance()
): Boolean {
    val currentUser = authenticator.currentUser
    return if (currentUser != null) {
        val uid = currentUser.uid
        try {
            val documentSnapshot = suspendCoroutine<DocumentSnapshot> { continuation ->
                database.collection("users").document(uid).get()
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
            if (documentSnapshot.exists()) {
                val userData = documentSnapshot.data
                UserManager.documentReferenceID = uid
                UserManager.userName = userData?.get("name") as? String ?: ""
                UserManager.userBirthday = userData?.get("birthday") as? String ?: ""
                UserManager.userHeight = userData?.get("height") as? String ?: ""
                UserManager.userWeight = userData?.get("weight") as? String ?: ""
                UserManager.userSex = userData?.get("sex") as? String ?: ""
                UserManager.userEmail = userData?.get("email") as? String ?: ""
                UserManager.fitnessLevel = userData?.get("fitnessLevel") as? String ?: ""
                true
            } else {
                false // User document does not exist
            }
        } catch (e: Exception) {
            Log.e("initializeApp", "Error initializing app: ${e.message}")
            false
        }
    } else {
        false // User not logged in
    }
}


