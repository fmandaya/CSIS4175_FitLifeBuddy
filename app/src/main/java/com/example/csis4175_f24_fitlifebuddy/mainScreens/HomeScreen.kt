package com.example.csis4175_f24_fitlifebuddy.mainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.example.csis4175_f24_fitlifebuddy.ui.theme.FitLifeBuddyTheme



@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Ensures content is not covered by the bottom navigation
        ) {
            Image(
                painter = painterResource(id = R.drawable.homepage_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FitLifeBuddyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(navController = rememberNavController())
        }
    }
}









/*

@Composable
fun MenuScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    // List of navigation destinations and corresponding image resources
    val items = listOf(
        Pair("profile_screen", R.drawable.widget_profile),
        Pair("workout_plan_screen", R.drawable.widget_workoutplan),
        Pair("nutrition_guidance_screen", R.drawable.widget_nutritionguidance),
        Pair("exercise_demos_screen", R.drawable.widget_exercisedemos),
        Pair("gym_finder_screen", R.drawable.widget_gymfinder),
        Pair("progress_tracker_screen", R.drawable.widget_progresstracker)
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
 */