package com.mibaldi.virtualassistant.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name : StateFlow<String> = _name.asStateFlow()

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state.asStateFlow()

    fun getName(){
        viewModelScope.launch {
            _name.update { "Mikel" }
        }
    }

    fun getEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            getEventsUseCase().fold(
                ifLeft = { cause -> _state.update { it.copy(error = cause, loading =  false) }},
                ifRight = {result ->
                    _state.update { UiState(events = result) }
                }
            )
        }
    }
    data class UiState(
        val loading: Boolean = false,
        val events: List<Event>? = null,
        val error: MyError? = null
    )

}