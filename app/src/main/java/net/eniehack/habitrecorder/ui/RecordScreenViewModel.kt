package net.eniehack.habitrecorder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.CheckIn
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.OfflineCheckInRepository
import net.eniehack.habitrecorder.data.OfflineHabitsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class HabitWithStreak(
    val habit: Habit,
    val streaks: Int = 0,
)

data class RecordScreenUiState(
    val habits: List<HabitWithStreak> = emptyList()
)

@HiltViewModel
class RecordScreenViewModel @Inject constructor(
    private val offlineHabitsRepo: OfflineHabitsRepository,
    private val offlineCheckInRepo: OfflineCheckInRepository
) : ViewModel() {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllHabits() {
        offlineHabitsRepo.getHabitsWithCheckInsStream().flatMapLatest { habitsWithCheckins ->
            if (habitsWithCheckins.isEmpty()) {
                offlineHabitsRepo.getAllHabitStream().map { habits ->
                    habits.map { habit ->
                        HabitWithStreak(
                            habit,
                            streaks = 0,
                        )
                    }
                }
            } else {
                flowOf(habitsWithCheckins.map { habitWithCheckIn ->
                    HabitWithStreak(
                        habit = habitWithCheckIn.habit,
                        streaks = calcStreaks(habitWithCheckIn.checkIns),
                    )
                })
            }
        }.onEach { habitWithStreaks ->
            Log.d("HabitRecorder", "recv new data. len: ${habitWithStreaks.size}")
            _uiState.update { current ->
                current.copy(habits = habitWithStreaks)
            }
        }.launchIn(viewModelScope)
    }

    fun onHabitCardClicked(habitWithStreak: HabitWithStreak) = viewModelScope.launch {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkIn = offlineCheckInRepo.getCheckInStreamByHabitWithDate(
            habitId = habitWithStreak.habit.id,
            date = LocalDate.now()
        ).firstOrNull()
        if (checkIn == null) {
            Log.d("habitrecorder", "inserting checkin (${habitWithStreak.habit.id}, ${LocalDate.now().format(dateFormat)})")
            offlineCheckInRepo.insertCheckIn(
                CheckIn(
                    habitId = habitWithStreak.habit.id,
                    createdAt = LocalDate.now().format(dateFormat)
                )
            )
        } else {
            Log.d("HabitRecorderApp", "updating checkin (${habitWithStreak.habit.id}, ${LocalDate.now().format(dateFormat)})")
            offlineCheckInRepo.updateCheckIn(
                checkIn.copy(
                    id = checkIn.id,
                    habitId = habitWithStreak.habit.id,
                    createdAt = checkIn.createdAt,
                    amount = checkIn.amount.inc(),
                )
            )
            Log.d("HabitRecorderApp", "updated checkin (${habitWithStreak.habit.id}, ${LocalDate.now().format(dateFormat)})")
        }
    }
}