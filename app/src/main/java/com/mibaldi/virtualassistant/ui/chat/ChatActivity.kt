package com.mibaldi.virtualassistant.ui.chat

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class ChatActivity : ComponentActivity() {
    private var textToSpeech :TextToSpeech? = null
    private val viewModel: ChatViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*ChatScreenActivity(onNavigate = {}) {
                userViewModel.setUserLoggedOut()
            }*/
        }
        textToSpeech = TextToSpeech(applicationContext) { status ->
            // if No error is found then only it will run
            if (status == TextToSpeech.SUCCESS) {
                // To Choose language of speech
                val locale = Locale("es", "ES") // For Spanish, Spain
                textToSpeech?.language = locale

                textToSpeech?.setOnUtteranceProgressListener(object :UtteranceProgressListener(){
                    override fun onStart(p0: String?) {
                        viewModel.setSpeaking(true)
                    }

                    override fun onDone(p0: String?) {
                        viewModel.setSpeaking(false)
                    }

                    override fun onError(p0: String?) {
                        viewModel.setSpeaking(false)
                    }
                })
            }
        }


        launchAndCollect(viewModel.speech){
            if (it.isNotEmpty()){
                viewModel.setSpeaking(true)
                textToSpeech?.speak(it,TextToSpeech.QUEUE_FLUSH,null,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)
            }
        }
        launchAndCollect(viewModel.speaking){
            if (!it){
                textToSpeech?.stop()
            }
        }

        userViewModel.isLoggedIn.observe(this){
                isLoggedIn ->
            if (!isLoggedIn) {
                // User is logged out, perform necessary actions
                goToHome()
                finish()
            }
        }

    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
        super.onDestroy()
    }
}