package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineHabitsRepository @Inject constructor(private val habitDao: HabitDao) : HabitRepository {
    override fun getAllHabitStream(): Flow<List<Habit>> = habitDao.getAllHabits()
    override fun getHabitStream(id: Int): Flow<Habit?> = habitDao.getHabit(id)
    override fun getHabitsWithCheckInsStream(): Flow<Map<Habit, List<CheckIn>>> = habitDao.getHabitsWithCheckIns()
    override fun getHabitWithCheckInsStream(id: Int): Flow<Map<Habit, List<CheckIn>>> = habitDao.getHabitWithCheckIns(id)
    override suspend fun deleteHabit(habit: Habit) = habitDao.delete(habit)
    override suspend fun insertHabit(habit: Habit) = habitDao.insert(habit)
    override suspend fun updateHabit(habit: Habit) = habitDao.update(habit)
}