package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens.LoginRegisterButton
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.model.UserManager
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavHostController) {
    val quicksand = FontFamily(Font(R.font.quicksand_regular))
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White, // Set the background color to white
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Profile",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFFD05C29),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Profile Information
                ProfileItem("Name", UserManager.userName.orEmpty(), quicksand)
                ProfileItem("Birthday", UserManager.userBirthday.orEmpty(), quicksand)
                ProfileItem("Email", UserManager.userEmail.orEmpty(), quicksand)
                ProfileItem("Fitness Level", UserManager.fitnessLevel.orEmpty(), quicksand)
                ProfileItem("Height", UserManager.userHeight.orEmpty() + "cm", quicksand)
                ProfileItem("Weight", UserManager.userWeight.orEmpty() + "kg", quicksand)
                ProfileItem("Sex", UserManager.userSex.orEmpty(), quicksand)
            }

            // Logout Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color(0xFFD05C29),
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    LoginRegisterButton(
                        text = "Log out",
                        isLoading = loading,
                    ) {
                        loading = true
                        try {
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(context, "Logged out successfully.", Toast.LENGTH_SHORT).show()
                            navController.navigate("login_screen")
                        } catch (e: Exception) {
                            Log.e(TAG, "Logout failed", e)
                            Toast.makeText(context, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()
                        } finally {
                            loading = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, fontFamily: FontFamily) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    color = Color(0xFF666666)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FitLifeBuddyTheme {
        ProfileScreen(navController = rememberNavController())
    }
}