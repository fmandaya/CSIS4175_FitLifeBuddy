package com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme

@Composable
fun NutritionHistoryScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val topPadding = screenHeight * 0.05f

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Nutrition",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = (screenWidth * 0.08f).value.sp,
                        color = Color(0xFFD05C29),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
            }

            Button(
                onClick = { navController.navigate("food_search_screen")},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD05C29), // Orange color
                    contentColor = Color.White // White text color
                ),
                shape = CircleShape, // Make the button circular
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Align to bottom-right
                    .padding(10.dp) // Padding from the edges
                    .size(55.dp) // Ensure the button is circular by setting equal width and height
            ) {
                Text(
                    text = "+",
                    fontFamily = FontFamily(
                        Font(R.font.quicksand_bold, FontWeight.ExtraBold)
                    ),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp, // Slightly larger font for the "+" symbol
                    textAlign = TextAlign.Center
                    
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionGuidanceScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NutritionHistoryScreen(navController = rememberNavController())
        }
    }
}
