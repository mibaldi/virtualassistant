package com.mibaldi.virtualassistant.ui.instagrams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.domain.InstagramProfile
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.usecases.GetEventsUseCase
import com.mibaldi.virtualassistant.usecases.GetInstagramsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramsViewModel @Inject constructor(
    private val getInstagramsUseCase: GetInstagramsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state.asStateFlow()


    fun getInstagrams() {
        viewModelScope.launch(Dispatchers.IO) {
            getInstagramsUseCase().fold(
                ifLeft = { cause -> _state.update { it.copy(error = cause, loading =  false) }},
                ifRight = {result ->
                    _state.update { UiState(instagrams = result) }
                }
            )
        }
    }
    data class UiState(
        val loading: Boolean = false,
        val instagrams: List<InstagramProfile>? = null,
        val error: MyError? = null
    )

}