package com.mibaldi.virtualassistant.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mibaldi.virtualassistant.domain.Message
import com.mibaldi.virtualassistant.ui.chat.ChatState
import com.mibaldi.virtualassistant.ui.chat.ChatViewModel
import com.mibaldi.virtualassistant.ui.chat.rememberChatState

@ExperimentalFoundationApi
@Composable
fun ChatScreenActivity(
    vm: ChatViewModel = hiltViewModel(),
    chatState: ChatState = rememberChatState()
) {

    LaunchedEffect(Unit) {
        chatState.generateTextToSpeech()
    }

    val speaking by vm.speaking.collectAsState()
    LaunchedEffect(speaking) {
        if (!speaking) {
            chatState.textToSpeech?.stop()
        }
    }

    val speech by vm.speech.collectAsState()
    LaunchedEffect(speech) {
        if (speech.isNotEmpty()) {
            vm.setSpeaking(true)
            chatState.textToSpeech?.speak(
                speech,
                TextToSpeech.QUEUE_FLUSH,
                null,
                TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        //GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/Conoce_a_SAM_nuestra_asistente_virtual.gif?alt=media&token=4135a9f3-a26f-42c1-8722-febb65249942")
        val chatMessages by vm.messages.collectAsState()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            reverseLayout = true
        ) {
            items(chatMessages.reversed()) { message ->
                ChatBubble(message)
            }
        }
        val isSpeaking by vm.speaking.collectAsState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            UserInput { messageText ->
                val newMessage = Message("user", messageText)
                vm.sendMessageChatGpt(newMessage)
            }
            AnimatedVisibility(
                visible = isSpeaking,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(onClick = { vm.setSpeaking(false) }) {
                    Text(text = "Stop")
                }
            }
        }
    }
}

@Composable
fun UserInput(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        modifier = Modifier
            .padding(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Send
        ),
        placeholder = { Text("Escribe un mensaje...") },
        keyboardActions = KeyboardActions(
            onSend = {
                onMessageSent(text)
                text = ""
            }
        )
    )
}

@Composable
fun ChatBubble(
    message: Message,
    modifier: Modifier = Modifier,
    vm: ChatViewModel = hiltViewModel()
) {
    val isUserMessage = message.role == "user"
    val bubbleColor = if (isUserMessage) Color(0xFF64B5F6) else Color.Gray
    val bubbleAlignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                vm.textToSpeech(message.content)
            },
        contentAlignment = bubbleAlignment

    ) {
        Surface(
            shape = BubbleShape(isUserMessage),
            color = bubbleColor,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}

@Composable
private fun BubbleShape(isUserMessage: Boolean): Shape {
    return when {
        isUserMessage -> {
            CircleShape.copy(
                topStart = ZeroCornerSize,
                topEnd = ZeroCornerSize,
                bottomEnd = ZeroCornerSize,
                bottomStart = ZeroCornerSize
            )
        }

        else -> {
            CircleShape.copy(
                topStart = ZeroCornerSize,
                topEnd = ZeroCornerSize,
                bottomEnd = ZeroCornerSize,
                bottomStart = ZeroCornerSize
            )
        }
    }
}
