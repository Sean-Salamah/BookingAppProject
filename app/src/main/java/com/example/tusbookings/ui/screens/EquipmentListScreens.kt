package com.example.tusbookings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import com.example.tusbookings.ui.components.EquipmentCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.tusbookings.model.Category
import com.example.tusbookings.model.Equipment



@Composable

fun HomeScreen(
    equipmentList: List<Equipment>, categories: List<Category>,
    onEquipmentOpen: (Int) -> Unit, onExplore: () -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Text("Book Tus Equipment",
                style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onExplore) { Text("Browse all equipment") }
            Text("Featured equipment")
        }

        items(equipmentList.take(2), key = { it.id }) { equipment ->
            val name = categories.firstOrNull {
                it.id == equipment.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = equipment, categoryName = name,
                onOpen = { onEquipmentOpen(equipment.id) })
        }
    }
}

@Composable

fun EquipmentListScreen(
    equipmentList: List<Equipment>, categories: List<Category>,
    onEquipmentOpen: (Int) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)

    ) {
        item { Text("All equipment",
            style = MaterialTheme.typography.headlineMedium) }
        items(equipmentList, key = { it.id }) { equipment ->
            val name = categories.firstOrNull {
                it.id == equipment.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = equipment, categoryName = name,
                onOpen = { onEquipmentOpen(equipment.id) })
        }
    }
}