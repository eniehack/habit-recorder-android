package net.eniehack.habitrecorder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val pixelaId : String?
)
