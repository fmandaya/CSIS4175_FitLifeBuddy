package com.example.csis4175_f24_fitlifebuddy.onboardingScreens

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.MyButton
import com.example.csis4175_f24_fitlifebuddy.utilities.model.UserManager
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun SelectFitnessLevelScreen(database: FirebaseFirestore, navController: NavHostController) {
    var loading by remember { mutableStateOf(false) } // Loading state
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height
    val quicksand = FontFamily(Font(R.font.quicksand_regular))
    var selectedIndex by remember { mutableStateOf(-1) }
    val fitnessLevels = listOf("Beginner", "Intermediate", "Advanced")
    val descriptions = listOf(
        "You are new to fitness training",
        "You have been training regularly",
        "You're fit and ready for an intensive workout plan"
    )

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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.3f))
            Text(
                text = "Select Your Fitness Level",
                fontSize = 24.sp,  // Smaller font size for better responsiveness
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.quicksand_bold, FontWeight.Bold)
                ),
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                      // Makes the list expand to use available space
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(fitnessLevels) { index, level ->
                    FitnessLevelItem(
                        level = level,
                        description = descriptions[index],
                        isSelected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
            Spacer(modifier = Modifier.height(screenHeight * 0.03f))
            if(loading) {
                CircularProgressIndicator(
                    color = Color(0xFFD05C29),
                    modifier = Modifier.size(50.dp)
                )
            } else {
                MyButton("Next") {
                if (selectedIndex == -1) {
                        Toast.makeText(
                            context,
                            "You must select your fitness level.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        loading = true;
                        val userRef = database.collection("users").document(UserManager.documentReferenceID)
                        userRef.update("fitnessLevel", fitnessLevels.get(selectedIndex))
                            .addOnSuccessListener {
                                Log.d("Firestore", "Fitness Level Successfully Added!")
                                Toast.makeText(
                                    navController.context,
                                    "Fitness Level Successfully Added!",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                navController.navigate("login_screen")
                            }
                            .addOnFailureListener { e -> Log.w("Firestore", "Error Adding Fitness Level", e) }
                        loading = false
                    }
                }

            }
        }
    }
}

@Composable
fun FitnessLevelItem(
    level: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFD05C29) else Color.LightGray
    val textColor = Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)  // Adds space between text elements
        ) {
            Text(
                text = level,
                fontSize = 18.sp,  // Slightly smaller font for responsiveness
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontFamily = FontFamily(
                    Font(R.font.quicksand_semibold, FontWeight.Bold)
                ),

            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = textColor,
                fontFamily = FontFamily(
                    Font(R.font.quicksand_regular, FontWeight.Bold)
                ),
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun FitnessLevelScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SelectFitnessLevelScreen(database = FirebaseFirestore.getInstance(), navController = rememberNavController())
        }
    }
}
 */