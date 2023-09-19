package com.mibaldi.virtualassistant.data.datasource

interface UserLocalDataSource {
    fun isLogged(): Boolean
    fun setLogged(logged: Boolean)
}