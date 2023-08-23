package com.mibaldi.virtualassistant.usecases

import com.mibaldi.virtualassistant.data.Repository
import com.mibaldi.virtualassistant.domain.Message
import javax.inject.Inject

class ChatGptUseCase @Inject constructor(private val repository: Repository) {
    suspend fun sendMessage(message: String) = repository.sendMessage(message)
}