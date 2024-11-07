package com.example.csis4175_f24_fitlifebuddy

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "welcome") {
                composable("welcome") {
                    WelcomeScreen(navController = navController)
                }
                composable("select_fitness_level") {
                    SelectFitnessLevelScreen(navController = navController)
                }
                composable("sign_up") {
                    SignUp(navController = navController)
                }
                composable("home") {
                    HomePage(navController = navController)
                }
                composable("profile") { /* Profile code here */ }
                composable("workout_plan") { /* Workout Plan code here */ }
                composable("nutrition_guidance") { /* Nutrition Guidance code here */ }
                composable("exercise_demos") { /* Exercise Demos code here */ }
                composable("gym_finder") {
                    GymFinder(navController = navController)
                }
                composable("progress_tracker") { /* Progress Tracker code here */ }
            }
        }
    }
}





@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(55.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.txtWelcome),
                fontSize = 34.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.txtFitLifeBuddy),
                fontSize = 43.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF84A127)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.txtWelcomeDescp),
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onGetStartedClick(navController) },
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .padding(25.dp),
                    shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.txtGetStarted),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun onGetStartedClick(navController: NavHostController) {
    navController.navigate("select_fitness_level")
}

@Composable
fun SelectFitnessLevelScreen(navController: NavHostController) {
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
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fitnesslvl_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, top = 25.dp, end = 25.dp, bottom = 70.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Your Fitness Level",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(35.dp))
            Button(
                onClick = {
                    if (selectedIndex == -1) {
                        Toast.makeText(
                            context,
                            "You must select your fitness level.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onNextClick(navController)
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                shape = RoundedCornerShape(10.dp)

            ) {
                Text(
                    text = "Next",
                    fontSize = 25.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun onNextClick(navController: NavHostController) {
    navController.navigate("sign_up")
}

@Composable
fun FitnessLevelItem(
    level: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFD0FD3E) else Color.LightGray
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

@Composable
fun SignUp(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "CREATE ACCOUNTS",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Please enter your credentials to proceed",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(45.dp))
            Text(
                text = "Full Name",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp
            )
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(56.dp),  // Fixed height for the text field
                textStyle = TextStyle(fontSize = 25.sp, color = Color.Gray),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (name.isEmpty()) {
                            Text(
                                text = "Enter your name",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Email Address",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp
            )

            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(56.dp),  // Fixed height for the text field
                textStyle = TextStyle(fontSize = 25.sp, color = Color.Gray),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (email.isEmpty()) {
                            Text(
                                text = "Enter your email",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Height",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp
            )
            BasicTextField(
                value = height,
                onValueChange = { height = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(56.dp),  // Fixed height for the text field
                textStyle = TextStyle(fontSize = 25.sp, color = Color.Gray),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (height.isEmpty()) {
                            Text(
                                text = "in cm",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Weight",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp
            )

            BasicTextField(
                value = weight,
                onValueChange = { weight = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(56.dp),  // Fixed height for the text field
                textStyle = TextStyle(fontSize = 25.sp, color = Color.Gray),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (weight.isEmpty()) {
                            Text(
                                text = "in kg",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Gender",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 20.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == "Male",
                    onClick = { gender = "Male" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Gray, unselectedColor = Color.Gray)
                )
                Text(
                    text = "Male",
                    color = Color.Gray,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = gender == "Female",
                    onClick = { gender = "Female" },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Gray, unselectedColor = Color.Gray)
                )
                Text(
                    text = "Female",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }


            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    // Perform validation and submit data
                    if (isValidEmail(email) && isValidHeight(height) && isValidWeight(weight)) {
                        // Insert database logic here

                        navController.navigate("home") // Navigate to Home after successful sign-up
                    } else {
                        // Handle invalid input
                        Toast.makeText(
                            navController.context,
                            "Please enter a correct input",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFD0FD3E)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 26.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// Validation functions
fun isValidEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

fun isValidHeight(height: String): Boolean {
    return try {
        val heightValue = height.toInt()
        heightValue in 50..220
    } catch (e: Exception) {
        false
    }
}

fun isValidWeight(weight: String): Boolean {
    return try {
        val weightValue = weight.toInt()
        weightValue in 20..130
    } catch (e: Exception) {
        false
    }
}

@Composable
fun HomePage(navController: NavHostController) {
    // List of navigation destinations and corresponding image resources
    val items = listOf(
        Pair("profile", R.drawable.widget_profile),
        Pair("workout_plan", R.drawable.widget_workoutplan),
        Pair("nutrition_guidance", R.drawable.widget_nutritionguidance),
        Pair("exercise_demos", R.drawable.widget_exercisedemos),
        Pair("gym_finder", R.drawable.widget_gymfinder),
        Pair("progress_tracker", R.drawable.widget_progresstracker)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.homepage_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Welcome, User", // Replace "User" with the fetched username
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 370.dp)
                    .align(Alignment.Start)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(bottom = 150.dp)
            ) {
                items(items) { item ->
                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate(item.first) }
                            .aspectRatio(1f)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = item.second),
                            contentDescription = item.first,
                            modifier = Modifier.size(290.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GymFinder(navController: NavHostController) {
        val context = LocalContext.current
        val googleMapsUrl = "https://www.google.com"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .background(Color.Black)
        ) {
            AndroidView(
                factory = {
                    WebView(context).apply {
                        clearCache(true)
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.setSupportZoom(true)
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        webViewClient = WebViewClient()
                        loadUrl(googleMapsUrl)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Image(
                painter = painterResource(id = R.drawable.back_btn),
                contentDescription = "Back Button",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        navController.popBackStack()
                    }
                    .padding(0.dp)
            )
        }
    }







