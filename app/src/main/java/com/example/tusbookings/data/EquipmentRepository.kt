package com.example.tusbookings.data

import androidx.room.withTransaction
import com.example.tusbookings.data.local.TUSBookingsDatabase
import com.example.tusbookings.model.Booking
import com.example.tusbookings.model.Equipment
import com.example.tusbookings.model.Category

class EquipmentRepository(private val database: TUSBookingsDatabase) {
        private val dao = database.tusBookingsDao()
        val equipment = dao.observeEquipment()
        val categories = dao.observeCategories()
        val bookings = dao.observeBookings()

    suspend fun seedIfEmpty() {
        database.withTransaction {
            if (dao.categoryCount() == 0 && dao.equipmentCount() == 0) {
                dao.insertCategories(sampleCategories)
                dao.insertEquipmentList(sampleEquipment)
            }
        }
    }
    suspend fun saveEquipment(equipment: Equipment): Long = dao.insertEquipment(equipment)

    suspend fun deleteEquipment(equipmentId: Int) {
        dao.deleteEquipment(equipmentId)
    }

    suspend fun saveBooking(booking: Booking): Long = dao.insertBooking(booking)

    suspend fun deleteBooking(bookingId: Int) {
        dao.deleteBooking(bookingId)
    }
}
