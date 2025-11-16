package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface CheckInRepository {
    fun getAllCheckInStream(): Flow<List<CheckIn>>
    fun getCheckInStream(id: Int): Flow<CheckIn>
    fun getCheckInStreamByHabitWithDate(habitId: Int, date: Instant): Flow<CheckIn?>
    suspend fun insertCheckIn(checkIn: CheckIn)
    suspend fun deleteCheckIn(checkIn: CheckIn)
    suspend fun updateCheckIn(checkIn: CheckIn)
}