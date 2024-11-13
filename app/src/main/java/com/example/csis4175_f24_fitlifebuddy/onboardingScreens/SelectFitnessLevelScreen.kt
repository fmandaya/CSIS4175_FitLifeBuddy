package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.MyButton
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme

@Composable
fun SelectFitnessLevelScreen(navController: NavHostController) {
    val quicksand = FontFamily(Font(R.font.quicksand_regular))
    var selectedIndex by remember { mutableStateOf(-1) }
    val fitnessLevels = listOf("Beginner", "Intermediate", "Advanced")
    val descriptions = listOf(
        "You are new to fitness training",
        "You have been training regularly",
        "You're fit and ready for an intensive workout plan"
    )

    // Get the current context
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.fitnesslvl_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 25.dp, end = 25.dp, top = 350.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Select Your Fitness Level",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = quicksand
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                itemsIndexed(fitnessLevels) { index, level ->
                    FitnessLevelItem(
                        level = level,
                        description = descriptions[index],
                        isSelected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            MyButton("NEXT") {
                if (selectedIndex == -1) {
                    Toast.makeText(
                        context,
                        "You must select your fitness level.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onNextClick(navController)
                }

            }
        }
    }
}

fun onNextClick(navController: NavHostController) {
    navController.navigate("home_screen")
}

@Composable
fun FitnessLevelItem(
    level: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFD05C29) else Color.LightGray
    val textColor = if (isSelected) Color.Black else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = level,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FitnessLevelScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectFitnessLevelScreen(navController = rememberNavController())
        }
    }
}