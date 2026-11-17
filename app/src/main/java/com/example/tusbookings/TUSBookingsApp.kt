package com.example.tusbookings

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import com.example.tusbookings.ui.screens.*
import com.example.tusbookings.ui.TUSBookingViewModel
import com.example.tusbookings.ui.components.TUSBookingsMenuShell

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

    fun openHome() {
        navController.navigate("home") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openEquipment() {
        navController.navigate("equipment") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openBookings() {
        navController.navigate("bookings") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openAbout() {
        navController.navigate("about") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openHelp() {
        navController.navigate("help") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
    }

    TUSBookingsMenuShell(
        selectedRoute = currentRoute,
        enabled = !state.isLoading && state.loadError == null,
        onHome = { openHome() },
        onBrowseEquipment = { openEquipment() },
        onMyBookings = { openBookings() },
        onAbout = { openAbout() },
        onHelp = { openHelp() }
    ) { padding ->

        if (state.isLoading) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                CircularProgressIndicator()
                Text("Loading saved equipment…")
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
                        onExplore = { openEquipment() }
                    )
                }

                composable("bookings") {
                    BookingsScreen(
                        bookings = state.bookings,
                        equipmentList = state.equipment,
                        isSaving = state.isAdding,
                        actionMessage = state.actionMessage,
                        onCancelBooking = model::deleteBooking
                    )
                }

                composable("about") {
                    AboutScreen(
                        onBackHome = { openHome() }
                    )
                }

                composable("help") {
                    HelpScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("equipment") {
                    ManageEquipmentScreen(
                        equipmentList = state.equipment,
                        categories = state.categories,
                        isSaving = state.isAdding,
                        actionMessage = state.actionMessage,
                        onAddEquipment = model::saveEquipment,
                        onDeleteEquipment = model::deleteEquipment,
                        onEquipmentOpen = { id -> navController.navigate("details/$id") }
                    )
                }
                composable(
                    route = "details/{equipmentId}",
                    arguments = listOf(navArgument("equipmentId") {
                        type = NavType.IntType
                    })
                ) { entry ->
                    val id = entry.arguments?.getInt("equipmentId")
                    val equipment = state.equipment.firstOrNull { it.id == id }
                    if (equipment == null) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Equipment not found")
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
                            bookings = state.bookings.filter { it.equipmentId == equipment.id },
                            isSaving = state.isAdding,
                            actionMessage = state.actionMessage,
                            onBook = model::saveBooking,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
