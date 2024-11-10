package com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth




@Composable
fun RegisterScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    lateinit var auth: FirebaseAuth
    auth = Firebase.auth
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = 20.dp // Adjusts to 10% of screen height
    var nameValue by remember { mutableStateOf("") }
    var birthdayValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var heightValue by remember { mutableStateOf("") }
    var weightValue by remember { mutableStateOf("") }
    var sexValue by remember { mutableStateOf("Male") }
    var passwordValue by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) } // Loading state


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        Text(
            text = "Register",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFFFF9B70),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Email TextField with gray line below
        MyTextField(
            label = "Name",
            value = nameValue,
            onValueChange = { newValue -> nameValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        MyTextField(
            label = "Birthday",
            value = birthdayValue,
            onValueChange = { newValue -> birthdayValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        MyTextField(
            label = "Height",
            value = heightValue,
            onValueChange = { newValue -> heightValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        MyTextField(
            label = "Weight",
            value = weightValue,
            onValueChange = { newValue -> weightValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(35.dp))

        // Left-aligned "Gender" text and gender toggle buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Gender",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
            GenderToggleButton()
        }
        Spacer(modifier = Modifier.height(15.dp))
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        MyTextField(
            label = "Email",
            value = emailValue,
            onValueChange = { newValue -> emailValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        MyTextField(
            label = "Password",
            value = passwordValue,
            onValueChange = { newValue -> passwordValue = newValue }
        )
        DividerLine()
        Spacer(modifier = Modifier.height(25.dp))

        // Conditional: Show loading indicator if creating account, else show button
        if (loading) {
            CircularProgressIndicator(
                color = Color(0xFFFF9B70), // Orange color for the progress indicator
                modifier = Modifier.size(50.dp)
            )
        } else {
            MyButton(text = "Next") {
                loading = true // Start loading
                auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener { task ->
                        loading = false // Stop loading
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                navController.context,
                                "Account created successfully.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            navController.navigate("fitness_level_screen")
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                navController.context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }


        Spacer(modifier = Modifier.height(6.dp))

        // "Have an account? Sign up" text with clickable "Sign up" part
        RegisterText()
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
fun RegisterText() {
    val annotatedText = AnnotatedString.Builder("Have an account? ").apply {
        pushStyle(SpanStyle(color = Color.Red, textDecoration = TextDecoration.Underline))
        append("login ")
    }.toAnnotatedString()

    ClickableText(
        text = annotatedText,
        onClick = { /* Handle sign-up click action here */ },
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Composable
fun GenderToggleButton(modifier: Modifier = Modifier) {
    var selectedGender by remember { mutableStateOf("Male") }

    Row(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color(0xFFFF9B70), // Orange color outline
                shape = RoundedCornerShape(8.dp)
            )
            .height(43.dp)
            .width(150.dp)
    ) {
        // Male button
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = if (selectedGender == "Male") Color(0xFFFF9B70) else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .clickable { selectedGender = "Male" }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Male",
                color = if (selectedGender == "Male") Color.White else Color(0xFFFF9B70),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.quicksand_bold))
            )
        }

        // Female button
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = if (selectedGender == "Female") Color(0xFFFF9B70) else Color.White,
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .clickable { selectedGender = "Female" }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Female",
                color = if (selectedGender == "Female") Color.White else Color(0xFFFF9B70),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.quicksand_bold))
            )
        }
    }
}

/*
fun onStart(navController: NavHostController) {
    // Check if user is signed in (non-null) and update UI accordingly.
    val currentUser = auth.currentUser
    if (currentUser != null) {
        navController.navigate("menu_screen")
    }
}
*/



@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            RegisterScreen(navController = rememberNavController())
        }
    }
}