package com.mibaldi.virtualassistant.data.local.database.notifications

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Notificacion.TABLE_NAME)
class Notificacion(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "codigo") val codigo: String,
    @ColumnInfo(name = "createat") val createat: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "read") var read: Int,
    @ColumnInfo(name = "texto") val texto: String,
    @ColumnInfo(name = "tipo") val tipo: Int,
    @ColumnInfo(name = "titulo") val titulo: String
) {
    companion object {
        const val TABLE_NAME = "notificacion"
    }
}