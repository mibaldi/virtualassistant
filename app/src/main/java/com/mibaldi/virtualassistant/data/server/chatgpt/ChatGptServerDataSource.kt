package com.mibaldi.virtualassistant.data.server.chatgpt

import arrow.core.Either
import com.mibaldi.virtualassistant.data.datasource.ChatGptDataSource
import com.mibaldi.virtualassistant.data.tryCall
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.domain.ChatGptResponse as DomainChatGptResponse
import com.mibaldi.virtualassistant.domain.Message as DomainMessage
import com.mibaldi.virtualassistant.domain.Choice as DomainChoice
import javax.inject.Inject

class ChatGptServerDataSource @Inject constructor(private val chatGptService: ChatGptService)  :ChatGptDataSource{

    override suspend fun sendMessage(message: String) : Either<MyError,DomainChatGptResponse> = tryCall {
        val request = ChatGptRequest(messages=listOf(Message("system", "Eres un asistente servicial."), Message("user", message)))
        val response = chatGptService.getChatResponse(request)
        response.toDomainModel()
    }
    private fun ChatGptResponse.toDomainModel():DomainChatGptResponse{
        return DomainChatGptResponse(id,`object`,created,model,choices.map { it.toDomainModel() })
    }

    private fun Choice.toDomainModel():DomainChoice{
        return DomainChoice(message.toDomainModel(),finish_reason)
    }
    private fun Message.toDomainModel():DomainMessage {
        return DomainMessage(role,content)
    }
}