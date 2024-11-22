import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.utilities.FoodSearchViewModel
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Product
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FoodSearchScreen(navController: NavHostController, viewModel: FoodSearchViewModel) {
    var searchText by remember { mutableStateOf("") }


    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Title
            Text(
                text = "Food Search",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
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
                onSearch = { viewModel.searchFood(searchText) }
            )

            // Content: Loading or Results
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFD05C29))
                }
            } else {
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
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemCard(
    foodItem: Product,
    navController: NavHostController,
    viewModel: FoodSearchViewModel
) {
    var isExpanded by remember { mutableStateOf(false) }
    var editableName by remember { mutableStateOf(foodItem.product_name ?: "") }
    var servings by remember { mutableStateOf("2") }
    var selectedDate by remember { mutableStateOf(getTodayDate()) }

    // State for Dropdown Menu
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf("Breakfast") }
    val mealOptions = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                // Navigate to food details screen on card tap
                viewModel.selectProduct(foodItem)
                navController.navigate("food_details_screen")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize() // Smoothly animates size changes
        ) {
            // Row for Image and Title
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
                        text = "Calories: ${foodItem.nutriments?.energy_kcal_100g?.toString() ?: "N/A"} kcal",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular))
                        )
                    )
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Remove else Icons.Default.Add,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFFD05C29)
                    )
                }
            }

            // Expanded Section
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // Editable Name
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = Color(0xFFD05C29), // Orange circle for the selection handle
                        backgroundColor = Color(0xFFFFE0B2) // Light yellow selection highlight
                    )
                ) {
                    // Editable Name
                    OutlinedTextField(
                        value = editableName,
                        onValueChange = { editableName = it },
                        label = { Text("Food Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.LightGray, // Grey border when not focused
                            focusedBorderColor = Color(0xFFD05C29),  // Orange border when focused
                            cursorColor = Color(0xFFD05C29), // Orange cursor
                            focusedLabelColor = Color(0xFFD05C29) // Orange label when focused
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Editable Servings
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = Color(0xFFD05C29), // Orange circle for the selection handle
                        backgroundColor = Color(0xFFFFE0B2) // Light yellow selection highlight
                    )
                ) {
                    OutlinedTextField(
                        value = servings,
                        onValueChange = { if (it.all { char -> char.isDigit() }) servings = it },
                        label = { Text("Number of Servings") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.LightGray, // Grey border when not focused
                            focusedBorderColor = Color(0xFFD05C29),  // Orange border when focused
                            cursorColor = Color(0xFFD05C29), // Orange cursor
                            focusedLabelColor = Color(0xFFD05C29) // Orange label when focused
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Editable Date
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = Color(0xFFD05C29), // Orange circle for the selection handle
                        backgroundColor = Color(0xFFFFE0B2) // Light yellow selection highlight
                    )
                ) {
                    // State for showing the date picker
                    var showDatePicker by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {}, // No manual editing allowed
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pick Date",
                                    tint = Color(0xFFD05C29)
                                )
                            }
                        },
                        readOnly = true, // Prevent manual input to enforce using the date picker
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.LightGray, // Grey border when not focused
                            focusedBorderColor = Color(0xFFD05C29), // Orange border when focused
                            cursorColor = Color(0xFFD05C29), // Orange cursor
                            focusedLabelColor = Color(0xFFD05C29) // Orange label when focused
                        )
                    )


                    // Display the DatePickerDialog if showDatePicker is true
                    if (showDatePicker) {
                        ThemedDatePickerDialog(
                            selectedDate = selectedDate,
                            onDateSelected = { date ->
                                selectedDate = date
                            },
                            onDismissRequest = { showDatePicker = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    // OutlinedTextField for Meal Type
                    OutlinedTextField(
                        value = selectedMeal,
                        onValueChange = {},
                        label = { Text("Meal Type") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDropdown = true },
                        trailingIcon = {
                            IconButton(onClick = { expandedDropdown = !expandedDropdown }) {
                                Icon(
                                    imageVector = if (expandedDropdown) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = "Meal Dropdown",
                                    tint = Color(0xFFD05C29)
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.LightGray,  // Grey border when not focused
                        focusedBorderColor = Color(0xFFD05C29),  // Grey border when focused
                        cursorColor = Color(0xFFD05C29),
                        focusedLabelColor = Color(0xFFD05C29),// Grey label when focused
                    )
                    )

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
                        modifier = Modifier
                            .fillMaxWidth() // Matches the width of the OutlinedTextField
                            .background(Color.White) // Ensure dropdown has a white background
                    ) {
                        mealOptions.forEach { meal ->
                            DropdownMenuItem(
                                text = { Text(meal) },
                                onClick = {
                                    selectedMeal = meal
                                    expandedDropdown = false
                                },
                                modifier = Modifier.fillMaxWidth() // Matches width of OutlinedTextField
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Button
                Button(
                    onClick = { isExpanded = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD05C29)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Add", color = Color.White)
                }
            }
        }
    }
}

fun getTodayDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedDatePickerDialog(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    // Override Material Theme for DatePicker
    val customColors = MaterialTheme.colorScheme.copy(
        primary = Color(0xFFD05C29), // Orange for accents
        onPrimary = Color.White, // Text color on primary
        surface = Color.White, // White background for the calendar
    )

    MaterialTheme(colorScheme = customColors) {
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedMillis ->
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = selectedMillis
                            add(Calendar.DAY_OF_MONTH, 1) // Adjust for potential timezone issues
                        }
                        onDateSelected(formatter.format(calendar.time))
                    }
                    onDismissRequest()
                }) {
                    Text("OK", color = Color(0xFFD05C29))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel",color = Color(0xFFD05C29))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}