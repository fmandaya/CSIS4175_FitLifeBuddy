package com.example.csis4175_f24_fitlifebuddy

import FoodSearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.LoginScreen
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.RegisterScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.GymFinderScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.HomeScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens.NutritionHistoryScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.SettingsScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.WorkoutPlanScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens.FoodDetailsScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenOne
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenThree
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenTwo
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SelectFitnessLevelScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SplashScreen
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import com.example.csis4175_f24_fitlifebuddy.utilities.model.FoodResponse
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Firebase

class MainActivity : ComponentActivity() {
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
                        composable("nutrition_history_screen") {
                            NutritionHistoryScreen(navController = navController)
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
                        composable("settings_screen") {
                            SettingsScreen(navController = navController)
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


