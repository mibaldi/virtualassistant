package com.mibaldi.virtualassistant.usecases

import com.mibaldi.virtualassistant.data.Repository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke() = repository.getEvents()
}