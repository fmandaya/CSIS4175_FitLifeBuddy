package com.example.csis4175_f24_fitlifebuddy

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
import com.example.csis4175_f24_fitlifebuddy.mainScreens.NutritionGuidanceScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.SettingsScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.SettingsScreenPreview
import com.example.csis4175_f24_fitlifebuddy.mainScreens.WorkoutPlanScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenOne
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenThree
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.OnboardingScreenTwo
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SelectFitnessLevelScreen
import com.example.csis4175_f24_fitlifebuddy.onboardingScreens.SplashScreen
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FitLifeBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash_screen") {
                        composable("splash_screen") {
                            SplashScreen(navController = navController)
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
                            LoginScreen(navController = navController)
                        }
                        composable("register_screen") {
                            RegisterScreen(navController = navController)
                        }
                        composable("fitness_level_screen") {
                            SelectFitnessLevelScreen(navController = navController)
                        }
                        composable("home_screen") {
                            HomeScreen(navController = navController)
                        }
                        composable("workout_plan_screen") {
                            WorkoutPlanScreen(navController = navController)
                        }
                        composable("nutrition_guidance_screen") {
                            NutritionGuidanceScreen(navController = navController)
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


