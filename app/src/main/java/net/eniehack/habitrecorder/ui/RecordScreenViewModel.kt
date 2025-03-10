package net.eniehack.habitrecorder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.CheckIn
import net.eniehack.habitrecorder.data.CheckInDao
import net.eniehack.habitrecorder.data.HabitDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class HabitWithStreak(
    val id: Int = 0 ,
    val title: String = "",
    val streaks: Int = 0,
    val unit: String = "",
    val pixelaId : String? = null,
    val baseAmount: Int = 1,
)

data class RecordScreenUiState(
    val habits: List<HabitWithStreak> = emptyList()
)

@HiltViewModel
class RecordScreenViewModel @Inject constructor(private val habitDao: HabitDao, private val checkInDao: CheckInDao) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllHabits()
    }

    fun calcStreaks(checkIns: List<CheckIn>): Int {
        if (checkIns.isEmpty()) return 0

        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var currentStreak = 0
        var previousDate = LocalDate.now()
        for (element in checkIns) {
            val currentDate = LocalDate.parse(element.createdAt, dateFormat)

            if (currentDate == previousDate) {
                currentStreak++
            } else {
                break
            }
            previousDate = previousDate.minusDays(1)
        }
        return currentStreak
    }

    fun getAllHabits() {
        viewModelScope.launch {
            habitDao.getHabitsWithCheckIns().collect { result ->
                _uiState.update { current ->
                    current.copy(
                        habits = result.keys.map { habit ->
                            if (result.containsKey(habit)) {
                                HabitWithStreak(
                                    title = habit.title,
                                    id = habit.id,
                                    baseAmount = habit.baseAmount,
                                    streaks = calcStreaks(result.getValue(habit)),
                                    unit = habit.unit,
                                )
                            } else {
                                HabitWithStreak(
                                    title = habit.title,
                                    id = habit.id,
                                    baseAmount = habit.baseAmount,
                                    streaks = 0,
                                    unit = habit.unit,
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun onHabitCardClicked(habit: HabitWithStreak) = viewModelScope.launch {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        checkInDao.insert(
            CheckIn(
                habitId = habit.id,
                createdAt = LocalDate.now().format(dateFormat)
            )
        )
    }
}