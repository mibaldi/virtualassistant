package com.mibaldi.virtualassistant.data.local.database

import androidx.lifecycle.map
import com.mibaldi.virtualassistant.data.datasource.LocalDataSource
import com.mibaldi.virtualassistant.data.local.database.events.DbEvent
import com.mibaldi.virtualassistant.data.local.database.events.EventDao
import com.mibaldi.virtualassistant.data.local.database.events.replaceAll
import com.mibaldi.virtualassistant.data.local.database.notifications.NotificacionDao
import com.mibaldi.virtualassistant.data.tryCall
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.Notificacion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mibaldi.virtualassistant.data.local.database.notifications.Notificacion as DbNotification

class RoomDataSource @Inject constructor(
    private val eventDao: EventDao,
    private val notificationDao: NotificacionDao
) : LocalDataSource {
    override val events: Flow<List<Event>> =
        eventDao.getAll().map { it.toDomainEvent() }

    override suspend fun mustUpdate(): Boolean {
        return true
    }

    override suspend fun saveEvent(event: Event) = tryCall {
        eventDao.insert(event.fromDomainModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override val notifications =
        notificationDao.getNotificaciones().map { it.toDomainNotification() }

    override suspend fun save(events: List<Event>) = tryCall {
        events.map { it.fromDomainModel() }.let {
            eventDao.replaceAll(*it.toTypedArray())
        }
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun deleteAllEvents() = tryCall {
        eventDao.deleteAll()
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )
}

private fun List<DbEvent>.toDomainEvent(): List<Event> = map { it.toDomainEvent() }
private fun List<DbNotification>.toDomainNotification(): List<Notificacion> =
    map { it.toDomainNotification() }

private fun DbEvent.toDomainEvent() =
    Event(id, name, phone, thumb)

private fun DbNotification.toDomainNotification() =
    Notificacion(id, codigo, createat, path, read, texto, tipo, titulo)


private fun List<Event>.fromDomainModel(): List<DbEvent> = map { it.fromDomainModel() }

private fun Event.fromDomainModel() =
    DbEvent(id, name, phone, thumb)