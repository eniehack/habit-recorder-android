package net.eniehack.habitrecorder.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habit::class, CheckIn::class], version = 2, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun checkInDao(): CheckInDao
}