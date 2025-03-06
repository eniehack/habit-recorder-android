package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabitStream(): Flow<List<Habit>>

    fun getHabitStream(id: Int): Flow<Habit?>

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)
}