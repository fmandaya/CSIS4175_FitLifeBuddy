package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.R

@Composable
fun OnboardingScreenThree(navController: NavHostController, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        Text(
            text = "Let's Get Started",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFFFF9B70),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.woman_man_logo),
            contentDescription = "FitLife Buddy Splash Screen",
            modifier = Modifier
                .size(300.dp) // Smaller image size
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please Select an Option",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(130.dp))
        MyButton(text = "I'm a new user") {
            navController.navigate("register_screen")
        }
        Spacer(modifier = Modifier.height(16.dp))
        MyButton(text = "I have an account") {
            navController.navigate("login_screen")
        }
    }
}

@Composable
fun MyButton(text: String, onNextClicked: () -> Unit) {
    Button(
        onClick = onNextClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9B70) // Orange button background
        ),
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp) // Add vertical padding for separation
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White // White text color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenThreePreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnboardingScreenThree(navController = rememberNavController())
        }
    }
}