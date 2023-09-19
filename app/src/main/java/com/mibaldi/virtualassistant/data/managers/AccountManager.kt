package com.mibaldi.virtualassistant.data.managers

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import pub.devrel.easypermissions.EasyPermissions

fun Fragment.getResultsFromApi(
    mCredential: GoogleAccountCredential,
    networkError: () -> Unit,
    makeRequestTask: () -> Unit
) {
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context!!) != 0) {
        acquireGooglePlayServices()
    } else if (mCredential.selectedAccountName == null) {
        chooseAccount(mCredential, networkError, makeRequestTask)
    } else if (!context!!.isDeviceOnline()) {
        networkError()
    } else {
        makeRequestTask()
    }
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

private fun Fragment.chooseAccount(
    mCredential: GoogleAccountCredential,
    networkError: () -> Unit,
    makeRequestTask: () -> Unit
) {

    if (EasyPermissions.hasPermissions(context!!, android.Manifest.permission.GET_ACCOUNTS)
    ) {
        val accountName = activity?.getPreferences(Context.MODE_PRIVATE)
            ?.getString(Constants.PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            mCredential.selectedAccountName = accountName
            getResultsFromApi(mCredential, networkError, makeRequestTask)
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

