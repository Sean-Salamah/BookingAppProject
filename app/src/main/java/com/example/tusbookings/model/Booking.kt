package com.example.tusbookings.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [ForeignKey(
        entity = Equipment::class,
        parentColumns = ["id"],
        childColumns = ["equipmentId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["equipmentId"])]
)
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val equipmentId: Int,
    val studentName: String,
    // Dates are saved as milliseconds, which is what the calendar picker gives back
    val startDate: Long,
    val endDate: Long
)
