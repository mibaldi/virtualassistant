package com.mibaldi.virtualassistant.data.datasource

interface LocalDataSource {
    fun isLogged(): Boolean
    fun setLogged(logged: Boolean)
}