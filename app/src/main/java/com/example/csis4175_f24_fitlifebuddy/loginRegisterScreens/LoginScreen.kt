package com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

//lateinit var auth: FirebaseAuth

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    lateinit var auth: FirebaseAuth
    auth = Firebase.auth
    val quicksand = FontFamily(Font(R.font.quicksand_regular))
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val topPadding = screenHeight * 0.03f

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) } // Loading state


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.05f)
        ) {
            Spacer(modifier = Modifier.height(topPadding))

            Text(
                text = "Login",
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.quicksand_bold, FontWeight.Bold)
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.08f).value.sp,
                    color = Color(0xFFD05C29),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.54f))

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
                    fontFamily = quicksand
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


        Spacer(modifier = Modifier.height(screenHeight * 0.02f))
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
                    fontFamily = quicksand
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

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))
            // Conditional: Show loading indicator if creating account, else show button
            if (loading) {
                CircularProgressIndicator(
                    color = Color(0xFFD05C29), // Orange color for the progress indicator
                    modifier = Modifier.size(50.dp)
                )
            } else {
                // Login Button
                MyButton(text = "Login") {
                    // Check if email and password are filled
                    if (emailValue.isNotBlank() && passwordValue.isNotBlank()) {
                        // Initialize Firebase Auth
                        loading = true
                        auth.signInWithEmailAndPassword(emailValue, passwordValue)
                            .addOnCompleteListener { task ->
                                loading = false
                                if (task.isSuccessful) {
                                    // Sign in success, navigate to home screen
                                    Log.d(TAG, "signInWithEmail:success")
                                    navController.navigate("home_screen")
                                } else {
                                    // Sign in fails, display an error message
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        navController.context,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                    } else {
                        // Display a message if email or password is missing
                        Toast.makeText(
                            navController.context,
                            "Please fill in both email and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            // "Have an account? Sign up" text with clickable "Sign up" part
            SignUpText(navController = navController)
        }
    }
}

@Composable
fun MyButton(text: String, onNextClicked: () -> Unit) {
    Button(
        onClick = onNextClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD05C29)
        ),
        modifier = Modifier
            .width(300.dp)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun SignUpText(navController: NavController) {
    val annotatedText = AnnotatedString.Builder("Have an account? ").apply {
        pushStyle(SpanStyle(color = Color.Red, textDecoration = TextDecoration.Underline))
        append("Sign up")
    }.toAnnotatedString()

    ClickableText(
        text = annotatedText,
        onClick = { navController.navigate("register_screen") },
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen(navController = rememberNavController())
        }
    }
}