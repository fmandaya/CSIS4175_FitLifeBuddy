import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.mainScreens.nutritionScreens.NutritionHistoryScreen
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Product

@Composable
fun FoodSearchScreen(navController: NavHostController, viewModel: FoodSearchViewModel) {
    var searchText by remember { mutableStateOf("") }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Food Search",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.08f).value.sp,
                    color = Color(0xFFD05C29),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )

            // Search Bar
            SearchBar(
                searchText = searchText,
                onTextChange = { searchText = it },
                onSearch = {
                    viewModel.searchFood(searchText)
                }
            )

            // Show loading spinner or food items
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFD05C29))
                }
            } else {
                // Scrollable List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.foodItems) { foodItem ->
                        FoodItemCard(foodItem, navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

// Food Item Data Class
data class FoodItem(val imageRes: Int, val name: String, val calories: String)

// Search Bar Composable with Button
@Composable
fun SearchBar(
    searchText: String,
    onTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)) // Curvy corners for the search bar
            .background(Color(0xFFF5F5F5)) // Light gray background
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular))
            ),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text(
                        text = "Search food items...",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    )
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Search",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD05C29))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onSearch() },
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.quicksand_bold))
        )
    }
}

@Composable
fun FoodItemCard(foodItem: Product, navController: NavHostController, viewModel: FoodSearchViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                // Navigate to the FoodDetailsScreen, passing the foodItem id
                viewModel.selectProduct(foodItem) // Set the selected product
                navController.navigate("food_details_screen") // Navigate to details screen
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image with rounded corners
            val imagePainter = if (!foodItem.image_url.isNullOrEmpty()) {
                rememberImagePainter(data = foodItem.image_url)
            } else {
                painterResource(id = R.drawable.default_food_image)
            }

            Image(
                painter = imagePainter,
                contentDescription = foodItem.product_name,
                modifier = Modifier
                    .size(80.dp) // Fixed size for the image
                    .clip(RoundedCornerShape(8.dp)) // Rounds all corners
                    .padding(end = 16.dp), // Add spacing to the right
                contentScale = ContentScale.Crop // Ensures proper scaling of the image
            )

            // Food Name and Calories
            Column {
                Text(
                    text = foodItem.product_name ?: "Unknown",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold))
                    )
                )
                Text(
                    text = "Calories per serving: ${foodItem.nutriments?.energy_kcal_100g?.toString() ?: "N/A"} kcal",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun FoodSearchScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FoodSearchScreen(navController = rememberNavController())
        }
    }
}

 */
