package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    fun getAllCheckInStream(): Flow<List<CheckIn>>
    fun getCheckInStream(id: Int): Flow<CheckIn>
    suspend fun insertCheckIn(checkIn: CheckIn)
    suspend fun deleteCheckIn(checkIn: CheckIn)
    suspend fun updateCheckIn(checkIn: CheckIn)
}