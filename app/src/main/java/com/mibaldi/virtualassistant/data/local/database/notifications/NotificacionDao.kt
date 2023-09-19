package com.mibaldi.virtualassistant.data.local.database.notifications

import androidx.room.Dao
import androidx.room.Delete

import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mibaldi.virtualassistant.data.local.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacionDao :BaseDao<Notificacion> {

    @Update
    fun update(vararg notificacion: Notificacion)

    @Delete
    fun delete(vararg notificacion: Notificacion)

    @Transaction
    @Query("SELECT * FROM " + Notificacion.TABLE_NAME + " WHERE id=:idnotificacion LIMIT 1")
    fun getNotificacionById(idnotificacion: Long): Flow<Notificacion>

    @Query("SELECT * FROM " + Notificacion.TABLE_NAME + " ORDER BY id DESC")
    fun getNotificaciones(): Flow<List<Notificacion>>

    @Query("DELETE FROM " + Notificacion.TABLE_NAME + " WHERE id=:idnotificacion")
    fun deleteNotificacionById(idnotificacion: Long)

    @Query("SELECT COUNT(id) FROM " + Notificacion.TABLE_NAME + " WHERE read = 0")
    fun getNotificacacionesSinLeer(): Flow<Int>

    @Query("SELECT COUNT() FROM ${Notificacion.TABLE_NAME}")
    fun count():Flow<Int>
}