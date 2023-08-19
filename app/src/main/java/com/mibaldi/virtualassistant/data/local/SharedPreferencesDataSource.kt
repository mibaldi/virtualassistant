package com.mibaldi.virtualassistant.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mibaldi.virtualassistant.data.datasource.LocalDataSource
import javax.inject.Inject

class SharedPreferencesDataSource @Inject constructor(context:Application):LocalDataSource {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("VirtualAssistant", Context.MODE_PRIVATE)


    override fun isLogged(): Boolean {
        return getSharedPreferenceBoolean("logged",false)
    }

    override fun setLogged(logged: Boolean) {
        setSharedPreferenceBoolean("logged",logged)
    }

    private fun setSharedPreferenceString(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getSharedPreferenceString(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    private fun setSharedPreferenceBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getSharedPreferenceBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }
}