package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme

@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.splash_screen),
        contentDescription = "FitLife Buddy Splash Screen",
        modifier = Modifier.fillMaxWidth(), // or any other modifier you need
        contentScale = ContentScale.Crop // Adjust how the image is scaled within the composable
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitLifeBuddyTheme {
       Surface(modifier = Modifier.fillMaxSize()) {
           SplashScreen(navController = rememberNavController())
       }
    }
}