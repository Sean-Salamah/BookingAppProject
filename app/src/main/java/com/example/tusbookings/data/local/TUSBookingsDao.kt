package com.example.tusbookings.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tusbookings.model.Category
import com.example.tusbookings.model.Equipment
import kotlinx.coroutines.flow.Flow

@Dao
interface TUSBookingsDao {

@Query("SELECT * FROM categories ORDER BY id")
fun observeCategories(): Flow<List<Category>>

    @Query("SELECT * FROM equipment ORDER BY id")
    fun observeEquipment(): Flow<List<Equipment>>

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun categoryCount(): Int

    @Query("SELECT COUNT(*) FROM equipment")
    suspend fun equipmentCount(): Int

    @Insert
    suspend fun insertCategories(categories: List<Category>)

    @Insert
    suspend fun insertEquipmentList(equipment: List<Equipment>)

    @Insert
    suspend fun insertEquipment(equipment: Equipment): Long
}