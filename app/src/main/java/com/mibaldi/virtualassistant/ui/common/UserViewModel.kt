package com.mibaldi.virtualassistant.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData(getUserUseCase.userIsLogged())
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private val _isLoggedInFlow = MutableStateFlow(UserIsLogged(getUserUseCase.userIsLogged()))
    val isLoggedInFlow : StateFlow<UserIsLogged> = _isLoggedInFlow.asStateFlow()


    // Call this function when the user logs in
    fun setUserLoggedIn() {
        viewModelScope.launch {
            getUserUseCase.setUserIsLogged(true)
            getUserLoggedIn()
        }
    }

    fun getUserLoggedIn() : StateFlow<UserIsLogged>{
        val userIsLogged = getUserUseCase.userIsLogged()
        _isLoggedIn.value = userIsLogged
        _isLoggedInFlow.value = UserIsLogged(userIsLogged)
        return isLoggedInFlow
    }
    // Call this function when the user logs out
    fun setUserLoggedOut() {
        viewModelScope.launch {
            getUserUseCase.logout()
            val userIsLogged = getUserUseCase.userIsLogged()
            _isLoggedIn.value = userIsLogged
            _isLoggedInFlow.value = UserIsLogged(userIsLogged)
        }
    }

    data class UserIsLogged(val isLogged: Boolean = false)

}


