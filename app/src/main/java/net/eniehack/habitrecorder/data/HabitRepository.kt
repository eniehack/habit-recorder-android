package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabitStream(): Flow<List<Habit>>

    fun getHabitStream(id: Int): Flow<Habit?>

    fun getHabitWithCheckInsStream(id: Int): Flow<HabitWithCheckIns>

    fun getHabitsWithCheckInsStream(): Flow<List<HabitWithCheckIns>>

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)
}