package com.example.tusbookings.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["categoryId"])]
)
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val categoryId: Int,
    val date: String,
    val location: String,
    val description: String,
    val priceEuro: Int,
    val availablePlaces: Int
)