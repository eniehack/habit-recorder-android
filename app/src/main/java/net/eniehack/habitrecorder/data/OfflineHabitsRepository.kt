package net.eniehack.habitrecorder.data

import androidx.annotation.OptIn
import androidx.compose.ui.graphics.computeHorizontalBounds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class OfflineHabitsRepository @Inject constructor(private val habitDao: HabitDao) : HabitRepository {
    override fun getAllHabitStream(): Flow<List<Habit>> = habitDao.getAllHabits()
    override fun getHabitStream(id: Int): Flow<Habit?> = habitDao.getHabit(id)

    override fun getHabitsWithCheckInsStream(): Flow<List<HabitWithCheckIns>> =
        habitDao.getHabitsWithCheckIns().map { habitMap ->
            if (habitMap.isNotEmpty()) {
                return@map habitMap
            } else {
                return@map listOf()
            }
        }
    override fun getHabitWithCheckInsStream(id: Int): Flow<HabitWithCheckIns> = habitDao.getHabitWithCheckIns(id)
    override suspend fun deleteHabit(habit: Habit) = habitDao.delete(habit)
    override suspend fun insertHabit(habit: Habit) = habitDao.insert(habit)
    override suspend fun updateHabit(habit: Habit) = habitDao.update(habit)
}