package net.eniehack.habitrecorder.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.CheckIn
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.OfflineCheckInRepository
import net.eniehack.habitrecorder.data.OfflineHabitsRepository
import net.eniehack.habitrecorder.data.PixelaApi
import net.eniehack.habitrecorder.data.PixelaCredentialRepository
import net.eniehack.habitrecorder.data.UserPreferencesRepository
import net.eniehack.habitrecorder.snippets.proto.UserPreferences
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import javax.inject.Inject

data class HabitWithStreak(
    val habit: Habit,
    val streaks: Int = 0,
    val hasTodayCheckIn: Boolean = false,
)

data class RecordScreenUiState(
    val habits: List<HabitWithStreak> = emptyList(),
    val isSelectionMode: Boolean = false,
    val selectedItems: Set<Habit> = setOf()
)

fun calcStreaks(checkIns: List<CheckIn>, baseDate: LocalDate, dateFormat: DateTimeFormatter): Int {
    if (checkIns.isEmpty()) return 0

    var currentStreak = 0
    var previousDate = baseDate
    for (checkIn in checkIns.sortedBy { checkIn -> checkIn.createdAt }.reversed()) {
        val currentDate = LocalDate.parse(checkIn.date, dateFormat)

        if (currentDate != previousDate) {
            break
        }
        currentStreak++
        previousDate = previousDate.minusDays(1)
    }
    return currentStreak
}

sealed class RecordScreenEvent {
    data class Toast(val message: String): RecordScreenEvent()
    data class DeletedToast(val quantity: Int) : RecordScreenEvent()
}

@HiltViewModel
class RecordScreenViewModel @Inject constructor(
    private val offlineHabitsRepo: OfflineHabitsRepository,
    private val offlineCheckInRepo: OfflineCheckInRepository,
    private val pixelaCredentialRepo: PixelaCredentialRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordScreenUiState())
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RecordScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val userPrefs = userPreferencesRepository.preferenceFlow.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(), UserPreferences.getDefaultInstance())

    init {
        getAllHabits()
    }

    fun checkTodayCheckIn(checkIns: List<CheckIn>): Boolean {
        if (checkIns.isEmpty()) return false
        val today = LocalDate.now()
        val formatedToday = today.format(dateFormat)
        return checkIns.fold(false) { _, checkIn -> checkIn.date == formatedToday }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllHabits() {
        offlineHabitsRepo.getHabitsWithCheckInsStream().flatMapLatest { habitsWithCheckins ->
            Log.d("HabitBuilder", habitsWithCheckins.toString())
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
                    Log.d("HabitBuilder", "streaks: ${calcStreaks(habitWithCheckIn.checkIns, LocalDate.now(), dateFormat)}")
                    HabitWithStreak(
                        habit = habitWithCheckIn.habit,
                        streaks = calcStreaks(habitWithCheckIn.checkIns, LocalDate.now(), dateFormat),
                        hasTodayCheckIn = checkTodayCheckIn(habitWithCheckIn.checkIns)
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
            Log.d(
                "habitrecorder",
                "inserting checkin (${habitWithStreak.habit.id}, ${
                    LocalDate.now().format(dateFormat)
                })"
            )
            val now = Instant.now()
            val timeZoneId = ZoneId.of(TimeZone.getDefault().id)
            offlineCheckInRepo.insertCheckIn(
                CheckIn(
                    habitId = habitWithStreak.habit.id,
                    date = dateFormat.withZone(timeZoneId).format(now),
                    updatedAt = now,
                    createdAt = now
                )
            )
            if (habitWithStreak.habit.pixelaId != null && userPrefs.value.enablePixela) {
                createCheckInOnPixela(habitWithStreak.habit.pixelaId)
            }
            _eventFlow.emit(RecordScreenEvent.Toast("check in"))
        } else {
            Log.d(
                "HabitRecorderApp",
                "updating checkin (${habitWithStreak.habit.id}, ${
                    LocalDate.now().format(dateFormat)
                })"
            )
            offlineCheckInRepo.deleteCheckIn(checkIn)
            Log.d(
                "HabitRecorderApp",
                "updated checkin (${habitWithStreak.habit.id}, ${
                    LocalDate.now().format(dateFormat)
                })"
            )
        }
    }

    fun enableSelectedMode() {
        _uiState.update { current ->
            current.copy(
                isSelectionMode = true
            )
        }
    }

    fun disableSelectedMode() {
        _uiState.update { current ->
            current.copy(
                isSelectionMode = false,
                selectedItems = setOf(),
            )
        }
    }

    fun addSelectedItem(habit: Habit) {
        _uiState.update { current ->
            current.copy(
                selectedItems = current.selectedItems.toMutableSet().apply { add(habit) }
            )
        }
        Log.d("HabitRecorderApp", "updated selectedItems ${_uiState.value.selectedItems}")
    }

    fun removeSelectedItem(habit: Habit) {
        _uiState.update { current ->
            current.copy(
                selectedItems = current.selectedItems.toMutableSet().apply { remove(habit) }
            )
        }
    }

    fun removeSelectedItemsFromDatabase() = viewModelScope.launch {
        _uiState.value.selectedItems.map { habit ->
            offlineHabitsRepo.deleteHabit(habit)
        }
        Log.d("HabitRecorderApp", "removed selectedItems ${_uiState.value.selectedItems}")
        _eventFlow.emit(RecordScreenEvent.DeletedToast(_uiState.value.selectedItems.size))
        _uiState.update { current ->
            current.copy(
                selectedItems = setOf()
            )
        }
        disableSelectedMode()
    }

    fun createCheckInOnPixela(pixelaId: String) = viewModelScope.launch {
        val credential = pixelaCredentialRepo.credentialFlow.firstOrNull()
        if (credential == null) {
            _eventFlow.emit(RecordScreenEvent.Toast("credential not set"))
            return@launch
        }
        val resp = PixelaApi.retrofitService.increment(
            graphId = pixelaId,
            userId = credential.userId,
            userToken = credential.token,
        )
    }
}