package com.mibaldi.virtualassistant.ui.rickyMorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.usecases.GetCharactersRickyMorty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RickyMortyViewModel @Inject constructor(
    private val getCharactersRickyMorty: GetCharactersRickyMorty
) : ViewModel() {
    private var totalPages: Int = 1
    private var nextPage = 1
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun clearList() {
        _state.value = UiState()
    }

    fun getCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            if (nextPage <= totalPages) {
                getCharactersRickyMorty(nextPage).fold(
                    ifLeft = { cause -> _state.update { it.copy(error = cause, loading = false) } },
                    ifRight = { result ->
                        _state.update {
                            val resultList = result.results
                            UiState(characters = _state.value.characters.plus(resultList))
                        }

                        result.info.next?.let {
                            nextPage = it.toInt()
                            totalPages = result.info.pages
                            //_state.update { UiState(noMoreItemFound = false) }
                        }

                    }
                )
            } else {
                _state.update { UiState(noMoreItemFound = true) }
            }

        }
    }

    data class UiState(
        val loading: Boolean = false,
        val characters: List<MyCharacter> = emptyList(),
        val noMoreItemFound: Boolean = false,
        val error: MyError? = null
    )
}