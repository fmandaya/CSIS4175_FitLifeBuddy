package com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        //  Pair("profile_screen", Icons.Filled.Home),
        Pair("workout_plan_screen", Icons.Filled.DirectionsRun),
        Pair("nutrition_history_screen", Icons.Filled.Restaurant),
        //  Pair("exercise_demos_screen", R.drawable.widget_exercisedemos),
        Pair("home_screen", Icons.Filled.Home),
        Pair("gym_finder_screen", Icons.Filled.Place),
        Pair("profile_screen", Icons.Filled.Person)
    )

    // Map routes to their parent menu items
    val routeMapping = mapOf(
        "food_search_screen" to "nutrition_history_screen",
        "food_details_screen" to "nutrition_history_screen"
    )

    Column {
        Spacer(modifier = Modifier.padding(top = 4.dp)) // Adds spacing above the divider
        Divider(
            color = Color.Gray.copy(alpha = 0.3f), // Semi-transparent gray for blurred effect
            thickness = 1.dp, // Slightly thicker for a softer look
        )

        NavigationBar(
            containerColor = Color.White, // White background for the navigation bar
            contentColor =  Color(0xFF707070) // Grey color for icons
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            // Determine the selected menu item
            val selectedRoute = routeMapping[currentRoute] ?: currentRoute


            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(item.second, contentDescription = null, modifier = Modifier.size(30.dp))
                    },
                    selected = selectedRoute == item.first,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFF9B70),
                        unselectedIconColor = Color(0xFF707070),
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        // Ensure navigation resets to the destination route
                        navController.navigate(item.first) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}