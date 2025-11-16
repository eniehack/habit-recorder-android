package net.eniehack.habitrecorder.data

import androidx.room.TypeConverter
import java.time.Instant

class DateTimeConverter {
    @TypeConverter
    fun instantToLong(value: Instant?): Long? {
        return value?.epochSecond
    }

    @TypeConverter
    fun longToInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(it) }
    }
}