package com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun RegisterScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    lateinit var auth: FirebaseAuth
    auth = Firebase.auth
    val quicksandBold = FontFamily(Font(R.font.quicksand_bold))
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = screenHeight * 0.1f // Adjusts to 10% of screen height
    var nameValue by remember { mutableStateOf("") }
    var birthdayValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var heightValue by remember { mutableStateOf("") }
    var weightValue by remember { mutableStateOf("") }
    var sexValue by remember { mutableStateOf("Male") }
    var passwordValue by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) } // Loading state

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
                .padding(top = 55.dp, start = 20.dp, end = 20.dp)
        ) {
            //Spacer(modifier = Modifier.height(topPadding))

            Text(
                text = "Register",
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
                    .padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Full Name",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
            BasicTextField(
                value = nameValue,
                onValueChange = { newValue -> nameValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (nameValue.isEmpty()) {
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
                text = "Birthday",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
            BasicTextField(
                value = birthdayValue,
                onValueChange = { newValue -> birthdayValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (birthdayValue.isEmpty()) {
                            Text(
                                text = "yyyy-MM-dd",
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
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
            BasicTextField(
                value = heightValue,
                onValueChange = { newValue -> heightValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),  // Fixed height for the text field
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (heightValue.isEmpty()) {
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
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )

            BasicTextField(
                value = weightValue,
                onValueChange = { newValue -> weightValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),  // Fixed height for the text field
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (weightValue.isEmpty()) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Gender",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.quicksand_medium)),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
                GenderToggleButton()
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Email",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )

            BasicTextField(
                value = emailValue,
                onValueChange = { newValue -> emailValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),  // Fixed height for the text field
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (emailValue.isEmpty()) {
                            Text(
                                text = "example@email.com",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Password",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )

            BasicTextField(
                value = passwordValue,
                onValueChange = { newValue -> passwordValue = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .height(45.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = quicksandBold
                ),
                visualTransformation = PasswordVisualTransformation(), // Masking password input
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(12.dp)) {
                        if (passwordValue.isEmpty()) {
                            Text(
                                text = "Enter your password",
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color(0xFFD05C29),
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    MyButton(
                        text = "NEXT",
                        isLoading = loading
                    ) {
                        // Perform validation before creating the account
                        if (isValidEmail(emailValue) &&
                            isValidHeight(heightValue) &&
                            isValidWeight(weightValue) &&
                            isValidBirthday(birthdayValue) &&
                            isValidName(nameValue) &&
                            isValidPassword(passwordValue)
                        ) {
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
                        } else {

                            // Handle invalid input
                            Toast.makeText(
                                navController.context,
                                "Please enter valid input for all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // "Have an account? Sign up" text with clickable "Sign up" part
            LoginText(navController = navController)
        }
    }
}


// Validation functions
fun isValidEmail(emailValue: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(emailValue).matches()
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

fun isValidBirthday(birthday: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // e.g., "1990-01-01"
        dateFormat.isLenient = false
        dateFormat.parse(birthday) != null
    } catch (e: Exception) {
        false
    }
}

fun isValidName(name: String): Boolean {
    return name.isNotBlank()
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
    return password.matches(passwordPattern)
}


@Composable
fun LoginText(navController: NavController) {
    val annotatedText = AnnotatedString.Builder("Have an account? ").apply {
        pushStyle(SpanStyle(color = Color.Red, textDecoration = TextDecoration.Underline))
        append("login ")
    }.toAnnotatedString()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center // Center horizontally within the Box
    ) {
        ClickableText(
            text = annotatedText,
            onClick = { navController.navigate("login_screen") },
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )
    }
}


@Composable
fun GenderToggleButton(modifier: Modifier = Modifier) {
    var selectedGender by remember { mutableStateOf("Male") }

    Row(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color(0xFFD05C29),
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
                    color = if (selectedGender == "Male") Color(0xFFD05C29) else Color.White,
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
                color = if (selectedGender == "Male") Color.White else Color(0xFFD05C29),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular))
            )
        }

        // Female button
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = if (selectedGender == "Female") Color(0xFFD05C29) else Color.White,
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
                color = if (selectedGender == "Female") Color.White else Color(0xFFD05C29),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular))
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

@Composable
fun MyButton(
    text: String,
    isLoading: Boolean,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Center the button horizontally
    ) {
        Button(
            onClick = { onNextClicked() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD05C29)
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(25.dp),
            shape = RoundedCornerShape(10.dp),
            enabled = !isLoading // Disable button while loading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

