package com.mibaldi.virtualassistant.domain

data class ChatGptResponse(val id: String, val `object`: String, val created: Long, val model: String, val choices: List<Choice>)
data class Choice(val message: Message, val finish_reason: String)
data class Message(val role: String, val content: String)