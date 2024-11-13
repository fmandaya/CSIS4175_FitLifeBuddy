package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun OnboardingScreenOne(navController: NavHostController, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.welcome_bg),
            contentDescription = "FitLife Buddy Splash Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = topPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Achieve Your Fitness Goals",
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.quicksand_bold, FontWeight.Bold)
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.07f).value.sp, // Font size relative to screen width
                    color = Color(0xFFD05C29),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Text(
                text = "Start your fitness journey with personalized workouts designed just for you. Track each session, challenge yourself, and see real progress. Let’s get moving!",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_regular, FontWeight.Normal)),
                    fontSize = (screenWidth * 0.046f).value.sp, // Font size relative to screen width
                    color = Color(0xFFD05C29)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = screenHeight * 0.55f, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = screenHeight * 0.05f) // Adjusts bottom padding relative to screen height
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NextButton("Next") {
                    navController.navigate("onboarding_screen_two")
                }
            }
        }
    }
}

@Composable
fun NextButton(text: String, onNextClicked: () -> Unit) {
    Button(
        onClick = { onNextClicked() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD05C29)
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f) // Button width is 80% of screen width
            .padding(25.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            fontFamily = FontFamily(
                Font(R.font.quicksand_bold, FontWeight.Bold)
            ),
            text = text,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenOnePreview() {
    val navController = rememberNavController()
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnboardingScreenOne(navController = navController)
        }
    }
}