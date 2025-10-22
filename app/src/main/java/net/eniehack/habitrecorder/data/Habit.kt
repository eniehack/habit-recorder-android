package net.eniehack.habitrecorder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HabitType {
    ACHIEVEMENT,
    RECORD
}

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val type : HabitType,
    val pixelaId : String? = null,
    val unit: String,
)
