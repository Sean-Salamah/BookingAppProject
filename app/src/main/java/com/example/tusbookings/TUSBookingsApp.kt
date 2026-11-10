package com.example.tusbookings

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import com.example.tusbookings.ui.screens.*
import com.example.tusbookings.ui.TUSBookingViewModel

@Composable
fun TUSBookingsApp() {
    val application = LocalContext.current.applicationContext as Application
    val model: TUSBookingViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )
    val state by model.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val canNavigateBack = currentRoute != null && currentRoute != "home"

    Scaffold { padding ->
        //Top Bar code
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (canNavigateBack) {
                        TextButton(onClick = { navController.navigateUp() }) {
                            Text("< Back", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Text(
                        text = when {
                            currentRoute == "equipment" -> "All Equipment"
                            currentRoute?.startsWith("details") == true -> "Equipment Details"
                            else -> "TUS Bookings"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            // End of top bar code
            if (state.isLoading) {
                Column(Modifier.padding(padding).padding(16.dp)) {
                    CircularProgressIndicator()
                    Text("Loading saved events…")
                }
            } else if (state.loadError != null) {
                Text(
                    text = "Database error: ${state.loadError}",
                    modifier = Modifier.padding(padding).padding(16.dp)
                )
            } else {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") {
                        HomeScreen(
                            equipmentList = state.equipment,
                            categories = state.categories,
                            onEquipmentOpen = { id -> navController.navigate("details/$id") },
                            onExplore = { navController.navigate("equipment") }
                        )
                    }
                    composable("equipment") {
                        Column(Modifier.fillMaxSize()) {
                            Button(
                                onClick = model::addTestEquipment,
                                enabled = !state.isAdding && state.categories.isNotEmpty(),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text(if (state.isAdding) "Saving…" else "Add test equipment")
                            }
                            state.actionMessage?.let { message ->
                                Text(message, Modifier.padding(horizontal = 16.dp))
                            }
                            Box(Modifier.weight(1f)) {
                                EquipmentListScreen(
                                    equipmentList = state.equipment,
                                    categories = state.categories,
                                    onEquipmentOpen = { id ->
                                        navController.navigate("details/$id")
                                    }
                                )
                            }
                        }
                    }
                    composable(
                        route = "details/{eventId}",
                        arguments = listOf(navArgument("eventId") {
                            type = NavType.IntType
                        })
                    ) { entry ->
                        val id = entry.arguments?.getInt("eventId")
                        val equipment = state.equipment.firstOrNull { it.id == id }
                        if (equipment == null) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Event not found")
                                Button(onClick = { navController.popBackStack() }) {
                                    Text("Back")
                                }
                            }
                        } else {
                            val categoryName = state.categories.firstOrNull {
                                it.id == equipment.categoryId
                            }?.name ?: "Uncategorised"
                            EquipmentDetailsScreen(
                                equipment = equipment,
                                categoryName = categoryName,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}