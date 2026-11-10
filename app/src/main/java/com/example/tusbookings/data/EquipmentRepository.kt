package com.example.tusbookings.data

import androidx.room.withTransaction
import com.example.tusbookings.data.local.TUSBookingsDatabase
import com.example.tusbookings.model.Equipment
import com.example.tusbookings.model.Category

class EquipmentRepository(private val database: TUSBookingsDatabase) {
        private val dao = database.tusBookingsDao()
        val equipment = dao.observeEquipment()
        val categories = dao.observeCategories()

    suspend fun seedIfEmpty() {
        database.withTransaction {
            if (dao.categoryCount() == 0 && dao.equipmentCount() == 0) {
                dao.insertCategories(sampleCategories)
                dao.insertEquipmentList(sampleEquipment)
            }
        }
    }
        suspend fun addEquipment(categoryId: Int): Long = dao.insertEquipment(
            Equipment(
                id = 0,
                name = "TestName!!",
                categoryId = categoryId,
                description = "test description!!"
            )
        )
    }
