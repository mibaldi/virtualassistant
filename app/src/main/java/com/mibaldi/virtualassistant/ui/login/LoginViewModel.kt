package com.mibaldi.virtualassistant.ui.login

import androidx.lifecycle.ViewModel
import com.mibaldi.virtualassistant.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _isLogged = MutableStateFlow(false)
    val isLogged : StateFlow<Boolean> = _isLogged.asStateFlow()


    fun getCurrentUserStatus(){
        _isLogged.update {getUserUseCase.userIsLogged()}
    }

    fun setCurrentUserStatus(logged: Boolean){
        getUserUseCase.setUserIsLogged(logged)
        _isLogged.update { logged }
    }
}