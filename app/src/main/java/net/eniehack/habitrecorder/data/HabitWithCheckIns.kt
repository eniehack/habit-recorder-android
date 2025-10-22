package net.eniehack.habitrecorder.data

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithCheckIns(
    @Embedded
    val habit: Habit,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val checkIns: List<CheckIn>
)