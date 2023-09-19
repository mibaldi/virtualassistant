package com.mibaldi.virtualassistant.domain

data class Notificacion(
    val id: Long,
    val codigo: String,
    val createat: String,
    val path: String,
    var read: Int,
    val texto: String,
    val tipo: Int,
    val titulo: String
)
