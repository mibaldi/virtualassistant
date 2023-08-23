package com.mibaldi.virtualassistant.ui.chat

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale


@Composable
fun rememberChatState(
    chatViewModel: ChatViewModel = hiltViewModel()
): ChatState  {
    val applicationContext = LocalContext.current.applicationContext
    return remember(chatViewModel,applicationContext) {
        ChatState(chatViewModel,applicationContext)
    }
}

class ChatState(val chatViewModel: ChatViewModel, val applicationContext: Context) {
    var textToSpeech :TextToSpeech? = null

    fun generateTextToSpeech(){
        textToSpeech = TextToSpeech(applicationContext) { status ->
            // if No error is found then only it will run
            if (status == TextToSpeech.SUCCESS) {
                // To Choose language of speech
                val locale = Locale("es", "ES") // For Spanish, Spain
                textToSpeech?.language = locale

                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
                    override fun onStart(p0: String?) {
                        chatViewModel.setSpeaking(true)
                    }

                    override fun onDone(p0: String?) {
                        chatViewModel.setSpeaking(false)
                    }

                    override fun onError(p0: String?) {
                        chatViewModel.setSpeaking(false)
                    }
                })
            }
        }
    }



}