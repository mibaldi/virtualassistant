package com.mibaldi.virtualassistant.data.local.database.events

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbEvent::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}