package com.mibaldi.virtualassistant.data.managers

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.CalendarScopes
import pub.devrel.easypermissions.EasyPermissions

fun Fragment.getResultsFromApi(mCredential: GoogleAccountCredential, networkError: ()-> Unit, makeRequestTask:()->Unit) {
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context!!) != 0) {
        acquireGooglePlayServices()
    } else if (mCredential.selectedAccountName == null) {
        chooseAccount(mCredential,networkError,makeRequestTask)
    } else if (!context!!.isDeviceOnline()) {
        networkError()
    } else {
        makeRequestTask()
    }
}
fun Context.initCredentials(): GoogleAccountCredential {
    return GoogleAccountCredential.usingOAuth2(
        this,
        arrayListOf(CalendarScopes.CALENDAR)
    ).setBackOff(ExponentialBackOff())
}
private fun Context.isDeviceOnline(): Boolean {
    val connMgr =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

private fun Fragment.acquireGooglePlayServices() {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context!!)
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
        showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
    }
}

fun Fragment.showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val dialog = apiAvailability.getErrorDialog(
        this,
        connectionStatusCode,
        Constants.REQUEST_GOOGLE_PLAY_SERVICES
    )
    dialog?.show()
}

private fun Fragment.chooseAccount(mCredential: GoogleAccountCredential,networkError: ()-> Unit,makeRequestTask:()->Unit) {

    if (EasyPermissions.hasPermissions(context!!, android.Manifest.permission.GET_ACCOUNTS)
    ) {
        val accountName = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(Constants.PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            mCredential.selectedAccountName = accountName
            getResultsFromApi(mCredential,networkError,makeRequestTask)
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                mCredential.newChooseAccountIntent(),
                Constants.REQUEST_ACCOUNT_PICKER
            )
        }
    } else {
        // Request the GET_ACCOUNTS permission via a user dialog
        EasyPermissions.requestPermissions(
            this,
            "This app needs to access your Google account (via Contacts).",
            Constants.REQUEST_PERMISSION_GET_ACCOUNTS,
            android.Manifest.permission.GET_ACCOUNTS
        )
    }
}

fun Fragment.onActivityResult(mCredential: GoogleAccountCredential,requestCode: Int, resultCode: Int, data: Intent?,errorPlayServices:()->Unit,getResultsFromApiExtracted:()->Unit){

    when (requestCode) {
        Constants.REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
            errorPlayServices()
        } else {
            getResultsFromApiExtracted()

        }

        Constants.REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null &&
            data.extras != null
        ) {
            val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            if (accountName != null) {
                val settings = activity?.getPreferences(Context.MODE_PRIVATE)
                val editor = settings?.edit()
                editor?.putString(Constants.PREF_ACCOUNT_NAME, accountName)
                editor?.apply()
                mCredential.selectedAccountName = accountName
                getResultsFromApiExtracted()

            }
        }

        Constants.REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
            getResultsFromApiExtracted()

        }
    }
}

object Constants {

    const val REQUEST_ACCOUNT_PICKER = 1000
    const val REQUEST_AUTHORIZATION = 1001
    const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
    const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    const val PREF_ACCOUNT_NAME = "getCalendarEvent"
}