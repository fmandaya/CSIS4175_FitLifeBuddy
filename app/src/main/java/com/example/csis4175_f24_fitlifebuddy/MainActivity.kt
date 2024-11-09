package com.example.csis4175_f24_fitlifebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.LoginScreen
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.RegisterScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.GymFinderScreen
import com.example.csis4175_f24_fitlifebuddy.mainScreens.MenuScreen
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
        setContent {
            FitLifeBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "onboarding_screen_one") {
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
                        composable("menu_screen") {
                            MenuScreen(navController = navController)
                        }
                        composable("profile_screen") { /* Profile code here */ }
                        composable("workout_plan_screen") { /* Workout Plan code here */ }
                        composable("nutrition_guidance_screen") { /* Nutrition Guidance code here */ }
                        composable("exercise_demos_screen") { /* Exercise Demos code here */ }
                        composable("gym_finder_screen") {
                            GymFinderScreen(navController = navController)
                        }
                        composable("progress_tracker") { /* Progress Tracker code here */ }
                    }
                }
            }
        }
    }
}


