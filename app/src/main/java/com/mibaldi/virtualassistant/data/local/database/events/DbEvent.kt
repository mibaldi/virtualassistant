package com.mibaldi.virtualassistant.data.local.database.events

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dbEvents")
data class DbEvent(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val phone: String,
    val thumb: String
)