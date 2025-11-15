package net.eniehack.habitrecorder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.eniehack.habitrecorder.data.PixelaApi
import net.eniehack.habitrecorder.data.PixelaCredentialRepository
import net.eniehack.habitrecorder.data.UserPreferencesRepository
import javax.inject.Inject

data class SettingsScreenUiState(
    val pixelaUserId : String = "",
    val pixelaToken : String = "",
    val showPixelaCredentialDialog : Boolean = false,
    val checkingPixelaCredentials : Boolean = false,
    val pixelaDialogErrorMessage: String? = null,
    val pixelaEnabled: Boolean = true
)

sealed class SettingsScreenEvent{
    data class Toast(val message: String): SettingsScreenEvent()
    object CloseDialog : SettingsScreenEvent()
}

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val pixelaCredentialRepo: PixelaCredentialRepository,
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventFlow = MutableSharedFlow<SettingsScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
        userPreferencesRepo.preferenceFlow
            .onEach { preference ->
                _uiState.update { current ->
                    current.copy(
                        pixelaEnabled = preference.enablePixela,
                    )
                }
            }
            .launchIn(viewModelScope)
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
        toggleCheckingCredentialState()
        setPixelaDialogErrorMessage(null)
        val resp = PixelaApi.retrofitService.getGraphDefinitions(userId = uiState.value.pixelaUserId, userToken = uiState.value.pixelaToken)
        if (resp.code() == 503) {
            _eventFlow.emit(SettingsScreenEvent.Toast("please press button again."))
            toggleCheckingCredentialState()
            return@launch
        }
        if (!resp.isSuccessful) {
            _eventFlow.emit(SettingsScreenEvent.Toast("failed to sign in pixela"))
            setPixelaDialogErrorMessage("invalid userid or token")
            toggleCheckingCredentialState()
            return@launch
        }
        pixelaCredentialRepo.save(uiState.value.pixelaUserId, uiState.value.pixelaToken)
        togglePixelaCredentialDialog()
        setPixelaDialogErrorMessage(null)
        _eventFlow.emit(SettingsScreenEvent.Toast("credential saved"))
    }

    fun togglePixelaCredentialDialog() = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                showPixelaCredentialDialog = !current.showPixelaCredentialDialog
            )
        }
    }

    fun togglePixelaFeature(enable: Boolean) = viewModelScope.launch {
        userPreferencesRepo.updateEnablePixela(enable)
    }

    private fun toggleCheckingCredentialState() = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                checkingPixelaCredentials = !current.checkingPixelaCredentials
            )
        }
    }

    private fun setPixelaDialogErrorMessage(message: String?) = viewModelScope.launch {
        _uiState.update { current ->
            current.copy(
                pixelaDialogErrorMessage = message
            )
        }
    }
}