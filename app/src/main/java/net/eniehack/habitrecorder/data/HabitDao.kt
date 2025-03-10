package net.eniehack.habitrecorder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getHabit(id: Int): Flow<Habit>

    @Query("SELECT * FROM habit ORDER BY id ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit JOIN checkin ON checkin.habit_id = habit.id WHERE habit.id = :id ORDER BY checkin.created_at DESC")
    fun getHabitWithCheckIns(id: Int): Flow<Map<Habit, List<CheckIn>>>

    @Query("SELECT * FROM habit JOIN checkin ON checkin.habit_id = habit.id ORDER BY checkin.created_at DESC")
    fun getHabitsWithCheckIns(): Flow<Map<Habit, List<CheckIn>>>
}