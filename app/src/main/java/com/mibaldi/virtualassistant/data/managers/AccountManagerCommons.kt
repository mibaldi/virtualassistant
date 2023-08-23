package com.mibaldi.virtualassistant.data.managers

import android.content.Context
import android.net.ConnectivityManager
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes

fun Context.initCredentials(): GoogleAccountCredential {
    return GoogleAccountCredential.usingOAuth2(
        this,
        arrayListOf(CalendarScopes.CALENDAR)
    ).setBackOff(ExponentialBackOff())
}
fun Context.isDeviceOnline(): Boolean {
    val connMgr =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
fun initCalendarBuild(credential: GoogleAccountCredential?) : Calendar?{

    val transport = AndroidHttp.newCompatibleTransport()
    val jsonFactory = JacksonFactory.getDefaultInstance()
    val mService = Calendar.Builder(transport, jsonFactory, credential)
        .setApplicationName("MibaldiCalendar").build()
    return mService
}

object Constants {

    const val REQUEST_ACCOUNT_PICKER = 1000
    const val REQUEST_AUTHORIZATION = 1001
    const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
    const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    const val PREF_ACCOUNT_NAME = "getCalendarEvent"
}