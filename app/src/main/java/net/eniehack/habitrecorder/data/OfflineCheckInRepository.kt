package net.eniehack.habitrecorder.data

import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class OfflineCheckInRepository @Inject constructor(private val checkInDao: CheckInDao) : CheckInRepository {
    override fun getAllCheckInStream(): Flow<List<CheckIn>> = checkInDao.getAllCheckIns()
    override fun getCheckInStream(id: Int): Flow<CheckIn> = checkInDao.getCheckIn(id)
    override fun getCheckInStreamByHabitWithDate(habitId: Int, date: Instant): Flow<CheckIn?> {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val zoneId = ZoneId.systemDefault()
        val formattedDate = dateFormat.withZone(zoneId).format(date)
        return checkInDao.getCheckInByHabitWithDate(habitId, formattedDate)
    }
    override suspend fun deleteCheckIn(checkIn: CheckIn) = checkInDao.delete(checkIn)
    override suspend fun insertCheckIn(checkIn: CheckIn) = checkInDao.insert(checkIn)
    override suspend fun updateCheckIn(checkIn: CheckIn) = checkInDao.update(checkIn)
}