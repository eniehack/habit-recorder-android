package net.eniehack.habitrecorder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val pixelaId : String? = null,
    @ColumnInfo(name = "base_amount")
    val baseAmount: Int = 1,
    val unit: String,
)
