package net.eniehack.habitrecorder.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class HabitType {
    ACHIEVEMENT,
    RECORD
}

@Entity
@Serializable
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String = "",
    val type : HabitType = HabitType.ACHIEVEMENT,
    val pixelaId : String? = null,
    val unit: String = "",
)
