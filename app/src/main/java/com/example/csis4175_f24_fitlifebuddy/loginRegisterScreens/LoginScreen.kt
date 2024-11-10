package com.example.csis4175_f24_fitlifebuddy.loginRegisterScreens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.R


@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = 50.dp // Adjusts to 10% of screen height
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        Text(
            text = "Login",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
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
                .size(300.dp)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(25.dp))

        // Email TextField with gray line below
        MyTextField(
            label = "Email",
            value = emailValue,
            onValueChange = { newValue -> emailValue = newValue }
        )
        DividerLine()

        Spacer(modifier = Modifier.height(25.dp))

        // Password TextField with gray line below
        MyTextField(
            label = "Password",
            value = passwordValue,
            onValueChange = { newValue -> passwordValue = newValue }
        )
        DividerLine()

        Spacer(modifier = Modifier.height(80.dp))

        // Login Button
        MyButton(text = "Login") {
            navController.navigate("menu_screen")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // "Have an account? Sign up" text with clickable "Sign up" part
        SignUpText()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label on the left, bold and black
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        // TextField on the right, smaller and orange text
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                fontSize = 14.sp,
                color = Color(0xFFFF9B70), // Orange color
                textAlign = androidx.compose.ui.text.style.TextAlign.End // Align text to the right
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // No background color
                focusedIndicatorColor = Color.Transparent, // No focused indicator
                unfocusedIndicatorColor = Color.Transparent // No unfocused indicator
            ),
            modifier = Modifier.width(200.dp)
        )
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun MyButton(text: String, onNextClicked: () -> Unit) {
    Button(
        onClick = onNextClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9B70)
        ),
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun SignUpText() {
    val annotatedText = AnnotatedString.Builder("Have an account? ").apply {
        pushStyle(SpanStyle(color = Color.Red, textDecoration = TextDecoration.Underline))
        append("Sign up")
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen(navController = rememberNavController())
        }
    }
}