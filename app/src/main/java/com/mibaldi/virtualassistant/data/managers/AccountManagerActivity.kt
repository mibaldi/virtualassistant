package com.mibaldi.virtualassistant.data.managers
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import pub.devrel.easypermissions.EasyPermissions

fun Activity.getResultsFromApi(mCredential: GoogleAccountCredential,networkError: ()-> Unit,makeRequestTask:()->Unit) {
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0) {
        acquireGooglePlayServices()
    } else if (mCredential.selectedAccountName == null) {
        chooseAccount(mCredential,networkError,makeRequestTask)
    } else if (!isDeviceOnline()) {
        networkError()
    } else {
        makeRequestTask()
    }
}

fun Activity.acquireGooglePlayServices() {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
        showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
    }
}

fun Activity.showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val dialog = apiAvailability.getErrorDialog(
        this,
        connectionStatusCode,
        Constants.REQUEST_GOOGLE_PLAY_SERVICES
    )
    dialog?.show()
}

private fun Activity.chooseAccount(mCredential: GoogleAccountCredential,networkError: ()-> Unit,makeRequestTask:()->Unit) {

    if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)
    ) {
        val accountName = this.getPreferences(Context.MODE_PRIVATE)
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

