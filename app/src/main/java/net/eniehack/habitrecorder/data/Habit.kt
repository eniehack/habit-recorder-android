package net.eniehack.habitrecorder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

enum class HabitType {
    BINARY,
    NUMERIC
}

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String = "",
    val type: HabitType = HabitType.BINARY,
    val pixelaId : String? = null,
    val unit: String = "",
    @ColumnInfo("last_synced_at")
    val lastSyncedAt: Instant?,
    @ColumnInfo("updated_at")
    val updatedAt: Instant?,
)
