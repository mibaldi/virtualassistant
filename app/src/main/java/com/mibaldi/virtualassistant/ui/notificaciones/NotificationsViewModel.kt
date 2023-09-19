package com.mibaldi.virtualassistant.ui.notificaciones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.data.toError
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.domain.Notificacion
import com.mibaldi.virtualassistant.usecases.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun clearList() {
        _state.value = UiState()
    }

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            getNotificationsUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { notifications ->
                    Log.i("NOTIFICATIONS", notifications.toString())
                    _state.update { UiState(notifications = notifications) }
                }

        }
    }

    data class UiState(
        val loading: Boolean = false,
        val notifications: List<Notificacion> = emptyList(),
        val error: MyError? = null
    )

}