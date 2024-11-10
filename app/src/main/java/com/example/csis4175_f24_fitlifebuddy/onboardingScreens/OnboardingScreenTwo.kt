package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme

@Composable
fun OnboardingScreenTwo(navController: NavHostController, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_screen_two),
            contentDescription = "FitLife Buddy Splash Screen",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Track Your Nutrition",
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.quicksand_bold, FontWeight.Bold)
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFFFF9B70),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(top = topPadding)
        )

        Text(
            text = "Fuel your fitness journey with balanced nutrition. Track your meals, explore healthy recipes, and make each meal a step toward a healthier you. Let’s dive in!",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_regular, FontWeight.Normal)),
                fontSize = 18.sp,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding * 7.2f, start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center
        )

        // Position the indicators and button at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp) // Space from the bottom of the screen
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Space between indicators and button
            ) {

                // Next button
                NextButton("Start") {
                    navController.navigate("onboarding_screen_three")
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun  OnboardingScreenTwoPreview() {
    val navController = rememberNavController()
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnboardingScreenTwo(navController = rememberNavController())
        }
    }
}