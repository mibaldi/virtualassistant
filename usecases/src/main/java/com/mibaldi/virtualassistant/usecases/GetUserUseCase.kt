package com.mibaldi.virtualassistant.usecases

import com.mibaldi.virtualassistant.data.Repository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: Repository) {

    fun userIsLogged(): Boolean{
        return repository.userIsLogged()
    }
    fun setUserIsLogged(logged:Boolean){
        repository.setUserIsLogged(logged)
    }
    fun logout(){
        repository.setUserIsLogged(false)
    }
}