package net.eniehack.habitrecorder.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.EditHabitNavigationArgument
import net.eniehack.habitrecorder.data.Habit
import net.eniehack.habitrecorder.data.HabitDao
import net.eniehack.habitrecorder.data.HabitType
import javax.inject.Inject

data class EditHabitScreenUiState(
    val habit: Habit = Habit(
        title = "",
        unit = "",
        type = HabitType.BINARY,
        pixelaId = null,
        lastSyncedAt = null,
        updatedAt = null,
    )
)

sealed class EditHabitScreenEvent {
    data class Toast(val message: String): EditHabitScreenEvent()
}

@HiltViewModel
class EditHabitScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val habitDao: HabitDao) : ViewModel() {
    private val argument = savedStateHandle.toRoute<EditHabitNavigationArgument>()
    private val _uiState = MutableStateFlow(EditHabitScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditHabitScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        if (argument.habitId != null) {
            val habitFlow = habitDao.getHabit(argument.habitId)
            viewModelScope.launch {
                habitFlow.collect { habit ->
                    Log.d("habitrecorder", "habit.id: ${habit.id}")
                    _uiState.update { current ->
                        current.copy(
                            habit = habit,
                        )
                    }
                }
            }
        }
    }

    fun onTitleChanged(title: String) = viewModelScope.launch {
        _uiState.update { current ->
            val newHabit = current.habit.copy(title = title)
            current.copy(
                habit = newHabit
            )
        }
    }

    fun onSubmit() = viewModelScope.launch {
        if (argument.habitId == null) {
            habitDao.insert(
                _uiState.value.habit
            )
            _eventFlow.emit(EditHabitScreenEvent.Toast("added"))
        } else {
            habitDao.update(
                _uiState.value.habit
            )
            _eventFlow.emit(EditHabitScreenEvent.Toast("updated"))
        }
    }

    fun onUnitChanged(unit: String) = viewModelScope.launch {
        _uiState.update { current ->
            val newHabit = current.habit.copy(unit = unit)
            current.copy(
                habit = newHabit
            )
        }
    }

    fun onPixelaIdChanged(pixelaId: String?) = viewModelScope.launch {
        if (pixelaId != null) {
            _uiState.update {  current ->
                current.copy(
                    habit = current.habit.copy(pixelaId = pixelaId)
                )
            }
        }
    }

    /*
    fun onAmountChanged(amount: Int?) = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                amount = amount ?: 1
            )
        }
    }
     */
}