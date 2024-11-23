package com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens

import FoodItemCard
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import com.example.csis4175_f24_fitlifebuddy.utilities.RetrofitInstance
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutritionHistoryScreen(navController: NavHostController, viewModel: FoodSearchViewModel) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // State to hold food items fetched via API
    var foodItems by remember { mutableStateOf(listOf<Quadruple<Product, String, Int, String>>()) }
    var isLoading by remember { mutableStateOf(true) }
    val firestore = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    // Fetch food IDs and their logged dates with servings and meal type from Firestore
    LaunchedEffect(uid) {
        if (uid != null) {
            firestore.collection("users").document(uid).collection("foodHistory")
                .get()
                .addOnSuccessListener { snapshot ->
                    val productData = snapshot.documents.mapNotNull {
                        val productID = it.getString("productID")
                        val loggedDate = it.getString("date") ?: "Unknown Date"
                        val servings = it.getString("servings")?.toIntOrNull() ?: 1
                        val mealType = it.getString("meal") ?: "Unknown Meal"
                        productID?.let { id -> Quadruple(id, loggedDate, servings, mealType) }
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        val productsWithDetails = productData.mapNotNull { (id, date, servings, mealType) ->
                            viewModel.getProductDetailsForNutritionHistoryScreen(id)?.let { product ->
                                Quadruple(product, date, servings, mealType)
                            }
                        }

                        withContext(Dispatchers.Main) {
                            foodItems = productsWithDetails
                            isLoading = false
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching food items: ${exception.message}")
                    isLoading = false
                }
        } else {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.nutrition_background),
                contentDescription = null, // Background image
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f) // Adjust transparency, where 1.0f is fully opaque and 0.0f is fully transparent
            )
            Column(
                modifier = Modifier.fillMaxSize()
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
                        .padding(top = 20.dp, bottom = 10.dp)
                )

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFD05C29))
                        }
                    }
                    foodItems.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No food items logged.",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                    color = Color.White,
                                    shadow = Shadow(
                                        color = Color.Black, // Shadow color
                                        offset = Offset(2f, 2f), // Shadow offset
                                        blurRadius = 2f // Shadow blur radius
                                    )
                                )
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Sort grouped items by date in descending order
                            val groupedItems = foodItems.groupBy { formatDate(it.second) }
                                .toSortedMap(compareByDescending { LocalDate.parse(it, DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH)) })

                            groupedItems.forEach { (formattedDate, items) ->
                                val totalCaloriesForDay = items.fold(0) { total, item ->
                                    total + (item.first.nutriments?.energy_kcal_100g?.times(item.third)?.toInt() ?: 0)
                                }

                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = formattedDate,
                                            style = TextStyle(
                                                fontSize = 16.sp, // Smaller font size
                                                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                            )
                                        )
                                        Text(
                                            text = "Calories: $totalCaloriesForDay kcal",
                                            style = TextStyle(
                                                fontSize = 14.sp, // Smaller font size
                                                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                                color = Color.Black,
                                            )
                                        )
                                    }
                                }
                                items(items) { (foodItem, _, servings, mealType) ->
                                    FoodItemCardHistoryScreen(
                                        foodItem = foodItem,
                                        servings = servings,
                                        mealType = mealType,
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // Add Button
            Button(
                onClick = { navController.navigate("food_search_screen") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD05C29),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp), // Use RoundedCornerShape with specified corner radius
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(50.dp), // Slightly larger size for the square
                elevation = ButtonDefaults.buttonElevation( // Add elevation for shadow
                    defaultElevation = 6.dp, // Elevation when button is at rest
                    pressedElevation = 12.dp, // Elevation when button is pressed
                    disabledElevation = 0.dp // Elevation when button is disabled
                )
            ) {
                Text(
                    text = "+",
                    fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.ExtraBold)),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FoodItemCardHistoryScreen(
    foodItem: Product,
    servings: Int, // Servings parameter
    mealType: String,
    navController: NavHostController,
    viewModel: FoodSearchViewModel
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Ensures height is bounded
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize() // Smoothly animate size changes
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val imagePainter = if (!foodItem.image_url.isNullOrEmpty()) {
                    rememberImagePainter(data = foodItem.image_url)
                } else {
                    painterResource(id = R.drawable.default_food_image)
                }

                Image(
                    painter = imagePainter,
                    contentDescription = foodItem.product_name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodItem.product_name ?: "Unknown",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.quicksand_bold))
                        )
                    )
                    Text(
                        text = "Meal Type: $mealType",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    )
                    Text(
                        text = "Calories per serving: ${foodItem.nutriments?.energy_kcal_100g ?: "N/A"} kcal",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    )
                 /*   Text(
                        text = "Total Calories: ${
                            foodItem.nutriments?.energy_kcal_100g?.times(servings) ?: "N/A"
                        } kcal",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    ) */
                    Text(
                        text = "Servings: $servings",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nutritional Information",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        color = Color(0xFF444444)
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Wrap the nutritional details in a bounded Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // Ensures bounded height for content
                ) {
                    NutritionalLabel2(foodItem = foodItem)
                }
            }
        }
    }
}

// Function to format a date string
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH)
    val date = LocalDate.parse(dateString, inputFormatter)
    return date.format(outputFormatter)
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Composable
fun NutritionalLabel2(foodItem: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Avoid using verticalScroll here
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NutritionalDetailRow(label = "Calories", value = "${foodItem.nutriments?.energy_kcal_100g ?: "N/A"} kcal")
            NutritionalDetailRow(label = "Carbohydrates", value = "${foodItem.nutriments?.carbohydrates_100g ?: "N/A"} g")
            NutritionalDetailRow(label = "Proteins", value = "${foodItem.nutriments?.proteins_100g ?: "N/A"} g")
            NutritionalDetailRow(label = "Fat", value = "${foodItem.nutriments?.fat_100g ?: "N/A"} g")
            NutritionalDetailRow(label = "Fiber", value = "${foodItem.nutriments?.fiber_100g ?: "N/A"} g")
            NutritionalDetailRow(label = "Sugars", value = "${foodItem.nutriments?.sugars_100g ?: "N/A"} g")
            NutritionalDetailRow(label = "Salt", value = "${foodItem.nutriments?.salt_100g ?: "N/A"} g")
        }
    }
}