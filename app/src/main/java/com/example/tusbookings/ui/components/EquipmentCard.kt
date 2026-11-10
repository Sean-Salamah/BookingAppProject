package com.example.tusbookings.ui.components
import android.R.attr.category
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusbookings.model.Equipment

@Composable

fun EquipmentCard(
    equipment: Equipment,
    categoryName: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        onClick = onOpen,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                categoryName.uppercase(),
                style = MaterialTheme.typography.labelMedium
            )

            Text(equipment.name, style = MaterialTheme.typography.titleLarge)
            Text(equipment.description)

        }
    }
}

