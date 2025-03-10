package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineCheckInRepository @Inject constructor(private val checkInDao: CheckInDao) : CheckInRepository {
    override fun getAllCheckInStream(): Flow<List<CheckIn>> = checkInDao.getAllCheckIns()
    override fun getCheckInStream(id: Int): Flow<CheckIn> = checkInDao.getCheckIn(id)
    override suspend fun deleteCheckIn(checkIn: CheckIn) = checkInDao.delete(checkIn)
    override suspend fun insertCheckIn(checkIn: CheckIn) = checkInDao.insert(checkIn)
    override suspend fun updateCheckIn(checkIn: CheckIn) = checkInDao.update(checkIn)
}