package com.mibaldi.virtualassistant.ui.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mibaldi.virtualassistant.domain.Message
import com.mibaldi.virtualassistant.usecases.ChatGptUseCase
import com.mibaldi.virtualassistant.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatGptUseCase: ChatGptUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow(mutableListOf<Message>())
    val messages : StateFlow<List<Message>> = _messages.asStateFlow()

    private val _speech = MutableStateFlow("")
    val speech : StateFlow<String> = _speech.asStateFlow()

    private val _speaking = MutableStateFlow(false)
    val speaking : StateFlow<Boolean> = _speaking.asStateFlow()

    fun sendMessageChatGpt(message: Message = Message("user","Translate 'hello' to French.")) {
        _messages.update {
            addItem(message)
        }
        viewModelScope.launch {
            chatGptUseCase.sendMessage(message.content).fold(
                ifLeft = {
                    Log.d("CHATGPTError",it.toString())
                    _messages.update {
                        removeItem(message)
                    }
                },
                ifRight = {result->
                    _messages.update {
                        addItem(result.choices.first().message)
                    }
                    Log.d("CHATGPT",result.toString())
                }
            )
        }
    }

    private fun addItem(message: Message) : MutableList<Message>{
        val newList = mutableListOf<Message>()
        newList.addAll(_messages.value)
        newList.add(message)
        return newList

    }

    private fun removeItem(message: Message) :MutableList<Message>{
        val newList = mutableListOf<Message>()
        newList.addAll(_messages.value)
        newList.remove(message)
        return newList
    }
    fun setSpeaking(isSpeaking: Boolean){
        _speaking.value = isSpeaking
    }
    fun textToSpeech(content: String) {
        _speech.value = content
    }
}