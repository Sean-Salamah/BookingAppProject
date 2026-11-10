package com.example.tusbookings.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tusbookings.model.Category
import com.example.tusbookings.model.Equipment

@Database(
    entities = [Category::class, Equipment::class],
    version = 1,
    exportSchema = true
)
abstract class TUSBookingsDatabase : RoomDatabase() {
    abstract fun tusBookingsDao(): TUSBookingsDao
    companion object {
        @Volatile private var instance: TUSBookingsDatabase? = null
        fun getInstance(context: Context): TUSBookingsDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TUSBookingsDatabase::class.java,
                    "tusbookings.db"
                ).fallbackToDestructiveMigration()
                .build().also { instance = it }
            }
    }
}