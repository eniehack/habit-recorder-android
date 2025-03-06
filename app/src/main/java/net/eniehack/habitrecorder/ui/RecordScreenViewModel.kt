package net.eniehack.habitrecorder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitDao
import javax.inject.Inject

data class RecordScreenUiState(
    val habits: List<Habit> = emptyList()
)

@HiltViewModel
class RecordScreenViewModel @Inject constructor(private val habitDao: HabitDao) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllHabits()
    }

    fun getAllHabits() = viewModelScope.launch {
        habitDao.getAllHabits().collect { habits ->
            _uiState.update { current ->
               current.copy(
                   habits = habits
               )
            }
        }
    }
}