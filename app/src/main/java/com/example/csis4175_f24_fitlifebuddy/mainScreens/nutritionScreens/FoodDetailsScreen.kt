package com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Nutriments
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Product

@Composable
fun FoodDetailsScreen(navController: NavHostController, viewModel: FoodSearchViewModel) {
    val foodItem = viewModel.selectedProduct

    Scaffold(
        containerColor = Color.White, // Set the background color to white
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (foodItem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Food Name Title
                    Text(
                        text = foodItem.product_name ?: "Food Details",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFFD05C29),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    // Food Image
                    val imagePainter = if (!foodItem.image_url.isNullOrEmpty()) {
                        rememberImagePainter(data = foodItem.image_url)
                    } else {
                        painterResource(id = R.drawable.default_food_image)
                    }

                    Image(
                        painter = imagePainter,
                        contentDescription = foodItem.product_name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // Adjust height as necessary
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Nutritional Information Title
                    Text(
                        text = "Nutritional Information",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                    )

                    // Nutritional Facts Label
                    NutritionalLabel(foodItem)
                }
            } else {
                // Handle the case where no product is selected
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No food item selected.", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun NutritionalLabel(foodItem: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Make the content scrollable
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
@Composable
fun NutritionalDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                color = Color.Black
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                color = Color.Gray
            )
        )
    }
}



/*
@Preview(showBackground = true)
@Composable
fun FoodDetailsScreenPreview() {
    FitLifeBuddyTheme {
        // Sample food item for preview purposes
        val sampleFoodItem = Product(
            product_name = "Red Apple",
            image_url = "https://upload.wikimedia.org/wikipedia/commons/1/15/Red_Apple.jpg", // Example image URL
            nutriments = Nutriments(
                energy_kcal_100g = 52f,
                carbohydrates_100g = 14f,
                proteins_100g = 0.3f,
                fat_100g = 0.2f,
                fiber_100g = 2.4f,
                sugars_100g = 10f,
                salt_100g = 0.01f
            )
        )

        // Create a dummy NavController for preview
        val navController = rememberNavController()

        // Display the FoodDetailsScreen with the sample food item
        FoodDetailsScreen(
            navController = navController,
            viewModel =  FoodSearchViewModel(),
          // Pass the mock Product object
        )
    }
}
 */