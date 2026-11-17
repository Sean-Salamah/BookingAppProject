package com.example.tusbookings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusbookings.data.EquipmentRepository
import com.example.tusbookings.data.local.TUSBookingsDatabase
import com.example.tusbookings.model.Booking
import com.example.tusbookings.model.Category
import com.example.tusbookings.model.Equipment
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TUSBookingsUiState(
    val equipment: List<Equipment> = emptyList(),
    val categories: List<Category> = emptyList(),
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val actionMessage: String? = null,
    val isAdding: Boolean = false
)

class TUSBookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EquipmentRepository(
        TUSBookingsDatabase.getInstance(application)
    )
    private val mutableUiState = MutableStateFlow(TUSBookingsUiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        // 1. Seed database in the background with coroutine thread (IO thread). I think this is needed to stop
        // database from being accessed on the main thread. because it disallows that causing it to crash( I think)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.seedIfEmpty()
            } catch (e: Exception) {

            }
        }


        viewModelScope.launch {
            try {
                combine(
                    repository.equipment,
                    repository.categories,
                    repository.bookings
                ) { equipmentList, categories, bookings ->
                    Triple(equipmentList, categories, bookings)
                }.collect { (equipmentList, categories, bookings) ->
                    mutableUiState.update {
                        it.copy(
                            equipment = equipmentList,
                            categories = categories,
                            bookings = bookings,
                            isLoading = false,
                            loadError = null
                        )
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(
                        isLoading = false,
                        loadError = error.message ?: "Unable to load equipment"
                    )
                }
            }
        }
    }

    fun saveEquipment(equipment: Equipment) {
        val current = mutableUiState.value
        if (current.isLoading || current.isAdding || current.loadError != null) return
        mutableUiState.update { it.copy(isAdding = true, actionMessage = null) }
        viewModelScope.launch {
            try {
                val id = repository.saveEquipment(equipment)
                mutableUiState.update {
                    it.copy(actionMessage = "Saved equipment with ID $id")
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(actionMessage = error.message ?: "Unable to save equipment")
                }
            } finally {
                mutableUiState.update { it.copy(isAdding = false) }
            }
        }
    }

    fun deleteEquipment(equipmentId: Int) {
        val current = mutableUiState.value
        if (current.isLoading || current.isAdding || current.loadError != null) return
        mutableUiState.update { it.copy(isAdding = true, actionMessage = null) }
        viewModelScope.launch {
            try {
                repository.deleteEquipment(equipmentId)
                mutableUiState.update { it.copy(actionMessage = "Equipment deleted") }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(actionMessage = error.message ?: "Unable to delete equipment")
                }
            } finally {
                mutableUiState.update { it.copy(isAdding = false) }
            }
        }
    }

    fun saveBooking(booking: Booking) {
        val current = mutableUiState.value
        if (current.isLoading || current.isAdding || current.loadError != null) return
        // Stop the same equipment being booked for dates that overlap another booking
        val alreadyBooked = current.bookings.any {
            it.equipmentId == booking.equipmentId &&
                    it.startDate <= booking.endDate && booking.startDate <= it.endDate
        }
        if (alreadyBooked) {
            mutableUiState.update {
                it.copy(actionMessage = "Already booked for some of those dates")
            }
            return
        }
        mutableUiState.update { it.copy(isAdding = true, actionMessage = null) }
        viewModelScope.launch {
            try {
                val id = repository.saveBooking(booking)
                mutableUiState.update { it.copy(actionMessage = "Saved booking with ID $id") }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(actionMessage = error.message ?: "Unable to save booking")
                }
            } finally {
                mutableUiState.update { it.copy(isAdding = false) }
            }
        }
    }

    fun deleteBooking(bookingId: Int) {
        val current = mutableUiState.value
        if (current.isLoading || current.isAdding || current.loadError != null) return
        mutableUiState.update { it.copy(isAdding = true, actionMessage = null) }
        viewModelScope.launch {
            try {
                repository.deleteBooking(bookingId)
                mutableUiState.update { it.copy(actionMessage = "Booking cancelled") }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableUiState.update {
                    it.copy(actionMessage = error.message ?: "Unable to cancel booking")
                }
            } finally {
                mutableUiState.update { it.copy(isAdding = false) }
            }
        }
    }
}