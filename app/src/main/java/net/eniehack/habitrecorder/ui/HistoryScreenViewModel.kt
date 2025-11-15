package net.eniehack.habitrecorder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.OfflineHabitsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


data class HistoryScreenUiState(
    val habits: List<HabitWithHistory> = emptyList(),
    val isSelectionMode: Boolean = false,
    val selectedItems: Set<Habit> = setOf()
)

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val offlineHabitsRepo: OfflineHabitsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryScreenUiState())
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val uiState = _uiState.asStateFlow()

    init {
        getAllHabits()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllHabits() {
        val today = LocalDate.now()
        val dateList = (0..6).map {
            today.minusDays(it.toLong())
        }
        offlineHabitsRepo.getHabitsWithCheckInsStream()
            .flatMapLatest { habitsWithCheckins ->
                Log.d("HabitBuilder", habitsWithCheckins.toString())
                if (habitsWithCheckins.isEmpty()) {
                    return@flatMapLatest offlineHabitsRepo.getAllHabitStream().map { habits ->
                        habits.map { habit ->
                            HabitWithHistory(
                                habit,
                                streaks = 0,
                                history = dateList.map { date ->
                                    Pair(date.format(dateFormat), false)
                                },
                            )
                        }
                    }
                }
                flowOf(habitsWithCheckins.map { habitWithCheckIn ->
                    Log.d(
                        "HabitBuilder",
                        "streaks: ${
                            calcStreaks(
                                habitWithCheckIn.checkIns,
                                LocalDate.now(),
                                dateFormat
                            )
                        }"
                    )
                    val checkInDatesSet = habitWithCheckIn.checkIns
                        .mapNotNull {
                            LocalDate.parse(it.createdAt, dateFormat)
                        }
                        .toSet()
                    val history = dateList.map { date ->
                        val isDone = checkInDatesSet.contains(date)
                        Pair(date.format(dateFormat), isDone)
                    }
                    HabitWithHistory(
                        habit = habitWithCheckIn.habit,
                        streaks = calcStreaks(
                            habitWithCheckIn.checkIns,
                            LocalDate.now(),
                            dateFormat
                        ),
                        history = history,
                    )
                })
            }.onEach { habitWithHistories ->
                Log.d("HabitRecorder", "recv new data. len: ${habitWithHistories.size}")
                _uiState.update { current ->
                    current.copy(habits = habitWithHistories)
                }
            }.launchIn(viewModelScope)
    }
}