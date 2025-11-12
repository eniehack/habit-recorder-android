package net.eniehack.habitrecorder.ui

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.PixelaCredentialRepository
import javax.inject.Inject

data class SettingsScreenUiState(
    val pixelaUserId : String = "",
    val pixelaToken : String = "",
    val showPixelaCredentialDialog : Boolean = false
)

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val pixelaCredentialRepo: PixelaCredentialRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val credentialFlow = pixelaCredentialRepo.read()
            credentialFlow.collect { cred ->
                _uiState.update { current ->
                    current.copy(
                        pixelaUserId = cred.userId,
                        pixelaToken = cred.token,
                    )
                }
            }
        }
    }

    fun onPixelaUserIdChanged(userId: String) = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                pixelaUserId = userId
            )
        }
    }

    fun onPixelaTokenChanged(token: String) = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                pixelaToken = token
            )
        }
    }

    fun savePixelaCredential() = viewModelScope.launch {
        pixelaCredentialRepo.save(uiState.value.pixelaUserId, uiState.value.pixelaToken)
    }

    fun togglePixelaCredentialDialog() = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                showPixelaCredentialDialog = !current.showPixelaCredentialDialog
            )
        }
    }
}