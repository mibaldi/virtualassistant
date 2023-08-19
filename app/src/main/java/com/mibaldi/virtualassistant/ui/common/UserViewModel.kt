package com.mibaldi.virtualassistant.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mibaldi.virtualassistant.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    // Call this function when the user logs in
    fun setUserLoggedIn() {
        getUserUseCase.setUserIsLogged(true)
        _isLoggedIn.value = getUserUseCase.userIsLogged()
    }

    // Call this function when the user logs out
    fun setUserLoggedOut() {
        getUserUseCase.logout()
        _isLoggedIn.value = getUserUseCase.userIsLogged()
    }

}


