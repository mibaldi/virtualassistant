package com.mibaldi.virtualassistant.data.server.chatgpt

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptService {
    @POST("chat/completions")
    suspend fun getChatResponse(@Body request: ChatGptRequest): ChatGptResponse
}