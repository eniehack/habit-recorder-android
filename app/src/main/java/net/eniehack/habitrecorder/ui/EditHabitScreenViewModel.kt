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

data class EditHabitScreenUiState(
    val title: String = ""
)

@HiltViewModel
class EditHabitScreenViewModel @Inject constructor(private val habitDao: HabitDao) : ViewModel() {
    private val _uiState = MutableStateFlow(EditHabitScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChanged(title: String) = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                title = title
            )
        }
    }

    fun onSubmit() = viewModelScope.launch {
        habitDao.insert(
            Habit(
                title = _uiState.value.title,
                pixelaId = null
            )
        )
    }

}