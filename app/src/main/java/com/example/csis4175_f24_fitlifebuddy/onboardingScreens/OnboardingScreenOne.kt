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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun OnboardingScreenOne(navController: NavHostController, modifier: Modifier = Modifier) {
    /*
        lateinit var auth: FirebaseAuth
        auth = Firebase.auth
        //onStart(navController = rememberNavController());
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate("menu_screen")
        }
     */

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.welcome_bg),
            contentDescription = "FitLife Buddy Splash Screen",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Archive Your Fitness Goals",
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.quicksand_bold, FontWeight.Bold)
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                color = Color(0xFFD05C29),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(top = topPadding)
        )

        Text(
            text = "Start your fitness journey with personalized workouts designed just for you. Track each session, challenge yourself, and see real progress. Let’s get moving!",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_regular, FontWeight.Normal)),
                fontSize = 20.sp,
                color = Color(0xFFD05C29)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding * 7.2f, start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Space between indicators and button
            ) {

                // Next button
                NextButton("NEXT") {
                    navController.navigate("onboarding_screen_two")
                }
            }
        }
    }
}

@Composable
fun NextButton(text: String, onNextClicked: () -> Unit) {
    Button(
        onClick = {
            onNextClicked()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD05C29)
        ),
        modifier = Modifier
            .width(300.dp)
            .padding(25.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White // Orange text color
        )
    }
}


@Preview(showBackground = true)
@Composable
fun  OnboardingScreenOnePreview() {
    val navController = rememberNavController()
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnboardingScreenOne(navController = rememberNavController())
        }
    }
}