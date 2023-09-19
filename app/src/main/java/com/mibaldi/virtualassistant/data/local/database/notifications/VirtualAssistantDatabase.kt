package com.mibaldi.virtualassistant.data.local.database.notifications

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notificacion::class], version = 1)
abstract class VirtualAssistantDatabase : RoomDatabase() {
    abstract fun notificacionDao(): NotificacionDao
}