package net.eniehack.habitrecorder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habit_id", "date"], unique = true)]
)
data class CheckIn(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "habit_id")
    val habitId: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant?,
    val date: String,
    val amount: Int = 1,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant?,
)
