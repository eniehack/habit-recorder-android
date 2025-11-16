package net.eniehack.habitrecorder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Habit::class, CheckIn::class], version = 3, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun checkInDao(): CheckInDao
}