package com.mibaldi.virtualassistant.data.datasource

import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.domain.Notificacion
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    val events: Flow<List<Event>>
    suspend fun mustUpdate(): Boolean
    suspend fun save(events: List<Event>): MyError?
    suspend fun deleteAllEvents(): MyError?
    suspend fun saveEvent(event: Event): MyError?

    val notifications: Flow<List<Notificacion>>
}