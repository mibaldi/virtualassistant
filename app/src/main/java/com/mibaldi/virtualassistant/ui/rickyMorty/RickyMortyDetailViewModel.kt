package com.mibaldi.virtualassistant.ui.rickyMorty

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.ui.navigation.NavArg
import com.mibaldi.virtualassistant.usecases.GetCharacterDetailRickyMorty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RickyMortyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterDetailRickyMorty: GetCharacterDetailRickyMorty
) : ViewModel() {

    private val id = savedStateHandle.get<Int>(NavArg.ItemId.key) ?: 0
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCharacterDetailRickyMorty(id.toString()).fold(
                ifLeft = { cause -> _state.update { it.copy(error = cause, loading = false) } },
                ifRight = { result ->
                    _state.update { UiState(character = result) }
                }
            )
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val character: MyCharacter? = null,
        val error: MyError? = null
    )
}