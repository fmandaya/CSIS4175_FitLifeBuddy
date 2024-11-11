package com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
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
        Pair("nutrition_guidance_screen", Icons.Filled.Restaurant),
        //  Pair("exercise_demos_screen", R.drawable.widget_exercisedemos),
        Pair("home_screen", Icons.Filled.Home),
        Pair("gym_finder_screen", Icons.Filled.Place),
        Pair("settings_screen", Icons.Filled.Settings)
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
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.second, contentDescription = null,  modifier = Modifier.size(30.dp) ) },
                    selected = currentRoute == item.first,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFF9B70),
                        unselectedIconColor = Color(0xFF707070),
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navController.navigate(item.first) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}