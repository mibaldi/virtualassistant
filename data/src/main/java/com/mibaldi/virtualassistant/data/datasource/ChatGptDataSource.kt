package com.mibaldi.virtualassistant.data.datasource

import arrow.core.Either
import com.mibaldi.virtualassistant.domain.ChatGptResponse
import com.mibaldi.virtualassistant.domain.MyError

interface ChatGptDataSource {
    suspend fun sendMessage(message:String) : Either<MyError, ChatGptResponse>
}