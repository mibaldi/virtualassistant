package com.mibaldi.virtualassistant.data.local.database.events


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mibaldi.virtualassistant.data.local.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao : BaseDao<DbEvent> {
    @Query("SELECT * FROM dbEvents")
    fun getAll(): Flow<List<DbEvent>>

    @Query("SELECT * FROM dbEvents WHERE id = :id")
    fun findById(id: Int): Flow<DbEvent>

    @Query("SELECT COUNT(id) FROM dbEvents")
    suspend fun eventCount(): Int

    @Query("DELETE from dbEvents")
    suspend fun deleteAll()
}

@Transaction
suspend fun EventDao.replaceAll(vararg events: DbEvent) {
    deleteAll()
    insert(*events)
}

