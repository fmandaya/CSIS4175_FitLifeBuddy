package com.example.csis4175_f24_fitlifebuddy.mainScreens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.csis4175_f24_fitlifebuddy.R
import com.example.csis4175_f24_fitlifebuddy.mainScreens.navigation.BottomNavigationBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.CameraPositionState


@Composable
fun GymFinderScreen(navController: NavHostController) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Title
            Text(
                text = "Gym Finder",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.quicksand_bold, FontWeight.Bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = (screenWidth * 0.08f).value.sp,
                    color = Color(0xFFD05C29),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp)
            )

            // Location Permission Handling
            RequestLocationPermission {
                GymFinder(padding = 10.dp)
            }
        }
    }
}

@Composable
fun GymFinder(padding: Dp) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val gyms = remember { mutableStateListOf<Pair<LatLng, String>>() }

    // Fetch gyms using Places API
    LaunchedEffect(Unit) {
        fetchNearbyGyms(
            location = LatLng(49.2036, -122.9127), // Example: Vancouver, BC
            apiKey = "AIzaSyAe6r_-1fVe1GeK4JpwEuekzI1sjA6RPHY",
            gyms = gyms,
            cameraPositionState = cameraPositionState
        )
    }


    // Google Map with markers
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)) // Add curved edges
                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)) // Optional border
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.SATELLITE // Choose NORMAL, HYBRID, TERRAIN, or NONE
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true, // Enable zoom controls
                    compassEnabled = true,     // Enable compass
                    myLocationButtonEnabled = false // Disable 'My Location' button
                ),
            ) {
                if (gyms.isNotEmpty()) {
                    gyms.forEach { (gymLocation, gymName) ->
                        Marker(
                            state = MarkerState(position = gymLocation),
                            title = gymName,
                            snippet = "Nearby gym"
                        )
                    }

                    // Automatically adjust camera to show all markers
                    LaunchedEffect(gyms) {
                        val boundsBuilder = LatLngBounds.Builder()
                        gyms.forEach { (gymLocation, _) ->
                            boundsBuilder.include(gymLocation)
                        }
                        val bounds = boundsBuilder.build()

                        try {
                            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                            cameraPositionState.animate(cameraUpdate)
                        } catch (e: Exception) {
                            Log.e("GymFinder", "Error adjusting camera: ${e.message}")
                        }
                    }
                }
            }

        }

    }
}


@SuppressLint("MissingPermission")
suspend fun fetchNearbyGyms(
    location: LatLng,
    apiKey: String,
    gyms: MutableList<Pair<LatLng, String>>,
    cameraPositionState: CameraPositionState
) {
    try {
        val response = withContext(Dispatchers.IO) {
            RetrofitClient.api.getNearbyPlaces(
                location = "${location.latitude},${location.longitude}",
                radius = 2000, // Radius in meters
                keyword = "Gym",
                apiKey = apiKey
            )
        }

        if (response.results.isNotEmpty()) {
            gyms.clear() // Clear existing gyms

            val boundsBuilder = LatLngBounds.Builder()

            response.results.forEach { place ->
                val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                gyms.add(latLng to place.name)
                boundsBuilder.include(latLng)
            }

            if (gyms.isNotEmpty()) {
                val bounds = boundsBuilder.build()
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                cameraPositionState.move(cameraUpdate)
            }
        } else {
            Log.e("GymFinder", "No gyms found.")
        }
    } catch (e: Exception) {
        Log.e("GymFinder", "Error fetching gyms: ${e.message}")
    }
}


@Composable
fun RequestLocationPermission(onPermissionGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (permissionGranted) {
        onPermissionGranted()
    } else {
        Text("Location permission is required to find gyms.")
    }
}




data class PlaceResponse(
    val results: List<PlaceResult>
)

data class PlaceResult(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: LatLngResponse
)

data class LatLngResponse(
    val lat: Double,
    val lng: Double
)



interface GooglePlacesApi {
    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("keyword") keyword: String,
        @Query("key") apiKey: String
    ): PlaceResponse
}


object RetrofitClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"

    val api: GooglePlacesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApi::class.java)
    }
}