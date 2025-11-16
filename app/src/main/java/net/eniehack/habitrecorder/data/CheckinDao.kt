package net.eniehack.habitrecorder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Insert
    suspend fun insert(checkIn: CheckIn)

    @Delete
    suspend fun delete(checkIn: CheckIn)

    @Update
    suspend fun update(checkIn: CheckIn)

    @Query("SELECT * FROM checkin ORDER BY date(created_at) DESC")
    fun getAllCheckIns(): Flow<List<CheckIn>>

    @Query("SELECT * FROM checkin WHERE habit_id = :habitId ORDER BY date(created_at) DESC")
    fun getCheckInsByHabit(habitId: Int): Flow<List<CheckIn>>

    @Query("SELECT * FROM checkin WHERE habit_id = :habitId AND date = :date")
    fun getCheckInByHabitWithDate(habitId: Int, date: String): Flow<CheckIn?>

    @Query("SELECT * FROM checkin WHERE id = :id")
    fun getCheckIn(id: Int): Flow<CheckIn>
}