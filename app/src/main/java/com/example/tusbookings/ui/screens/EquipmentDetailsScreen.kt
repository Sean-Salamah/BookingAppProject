package com.example.tusbookings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusbookings.model.Booking
import com.example.tusbookings.model.Equipment


@Composable

fun EquipmentDetailsScreen(
    equipment: Equipment,
    categoryName: String,
    bookings: List<Booking>,
    isSaving: Boolean,
    actionMessage: String?,
    onBook: (Booking) -> Unit,
    onBack: () -> Unit
) {
    var showBookForm by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedButton(onClick = onBack) { Text("Back") }
        Text(categoryName, style = MaterialTheme.typography.labelLarge)
        Text(equipment.name, style = MaterialTheme.typography.headlineMedium)

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Category: $categoryName")
                Text("equipment name: ${equipment.name}")
            }
        }
        Text("About this ${categoryName} ", style = MaterialTheme.typography.titleLarge)
        Text(equipment.description)

        Button(onClick = { showBookForm = true }, enabled = !isSaving) {
            Text("Book this equipment")
        }
        if (isSaving) { Text("Saving booking…") }
        actionMessage?.let { message -> Text(message) }

        Text("Booked dates", style = MaterialTheme.typography.titleLarge)
        if (bookings.isEmpty()) {
            Text("Not booked yet.")
        }
        bookings.forEach { booking ->
            Text("${formatDate(booking.startDate)} to ${formatDate(booking.endDate)} - ${booking.studentName}")
        }
    }

    if (showBookForm) {
        BookEquipmentDialog(equipment = equipment,
            bookings = bookings,
            onDismiss = { showBookForm = false },
            onSave = { booking ->
                onBook(booking)
                showBookForm = false
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookEquipmentDialog(
    equipment: Equipment,
    bookings: List<Booking>,
    onDismiss: () -> Unit,
    onSave: (Booking) -> Unit
) {

    var studentName by rememberSaveable { mutableStateOf("") }
    var startDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var endDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var showCalendar by rememberSaveable { mutableStateOf(false) }
    val canSave = studentName.isNotBlank() && startDate != null && endDate != null

    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Book ${equipment.name}") },
        text = {
            Column(Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Enter your name and choose the dates you need it.")
                OutlinedTextField(value = studentName, onValueChange = { studentName = it },
                    label = { Text("Your name") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth())
                OutlinedButton(onClick = { showCalendar = true }) {
                    Text("Choose dates")
                }
                Text("Start: ${startDate?.let { formatDate(it) } ?: "Not chosen"}")
                Text("End: ${endDate?.let { formatDate(it) } ?: "Not chosen"}")
            }
        },
        confirmButton = {
            TextButton(enabled = canSave, onClick = {
                val validStart = startDate ?: return@TextButton
                val validEnd = endDate ?: return@TextButton
                onSave(Booking(id = 0, equipmentId = equipment.id,
                    studentName = studentName.trim(),
                    startDate = validStart, endDate = validEnd))
            }) { Text("Book") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })

    if (showCalendar) {
        // The calendar works in UTC, so "today" is worked out as midnight UTC
        val dayInMillis = 24 * 60 * 60 * 1000L
        val today = System.currentTimeMillis() / dayInMillis * dayInMillis
        val calendarState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = startDate,
            initialSelectedEndDateMillis = endDate,
            selectableDates = object : SelectableDates {
                // Past days and days that are already booked can't be picked
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val alreadyBooked = bookings.any {
                        utcTimeMillis >= it.startDate && utcTimeMillis <= it.endDate
                    }
                    return utcTimeMillis >= today && !alreadyBooked
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showCalendar = false },
            confirmButton = {
                TextButton(
                    enabled = calendarState.selectedStartDateMillis != null &&
                            calendarState.selectedEndDateMillis != null,
                    onClick = {
                        startDate = calendarState.selectedStartDateMillis
                        endDate = calendarState.selectedEndDateMillis
                        showCalendar = false
                    }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCalendar = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(
                state = calendarState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
