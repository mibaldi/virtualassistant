package com.mibaldi.virtualassistant.ui.bookings

import android.accounts.AccountManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.calendar.Calendar
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.acquireGooglePlayServices
import com.mibaldi.virtualassistant.data.managers.initCalendarBuild
import com.mibaldi.virtualassistant.data.managers.initCredentials
import com.mibaldi.virtualassistant.data.managers.isDeviceOnline
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.common.launchAndCollect
import com.mibaldi.virtualassistant.ui.common.showAppSettingsDialog
import com.mibaldi.virtualassistant.ui.common.showPermissionExplanationDialog
import com.mibaldi.virtualassistant.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class BookingActivity : ComponentActivity(){
    /*private val viewModel: BookingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()*/

    /*private var mCredential: GoogleAccountCredential? = null //to access our account
    private var mService: Calendar? = null //To access the calendar

    lateinit var accountPickerLauncher: ActivityResultLauncher<Intent>
    lateinit var requestAuthorizationLauncher: ActivityResultLauncher<Intent>
    lateinit var getAccountLauncher: ActivityResultLauncher<String>*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookingApp()
            /*BookingScreen({
                Log.d("CLICK","elemento clicado $it")
            },{
                userViewModel.setUserLoggedOut()
            })*/
        }
        /*viewModel.getBookings()
        mCredential = initCredentials()
        mService = initCalendarBuild(mCredential)

        launchAndCollect(userViewModel.isLoggedIn){
            if (!it) {
                // User is logged out, perform necessary actions
                goToHome()
                finish()
            }
        }
        launchAndCollect(viewModel.setBooking){
            if (it.name.isNotEmpty() && it.dateString.isNotEmpty()) {
                setEvents()
            }
        }
        launchAndCollect(viewModel.error){
            if (it != null){
                requestAuthorizationLauncher.launch(it.exception.intent)
            }
        }
        accountPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                if (result!!.data?.extras != null) {
                    val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        val settings = getPreferences(Context.MODE_PRIVATE)
                        val editor = settings?.edit()
                        editor?.putString(Constants.PREF_ACCOUNT_NAME, accountName)
                        editor?.apply()
                        mCredential?.selectedAccountName = accountName
                        setEvents()
                    }
                }
            }
        }
        requestAuthorizationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        }
        getAccountLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) setEvents() else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS)) {
                    showPermissionExplanationDialog(getAccountLauncher)
                } else {
                    showAppSettingsDialog()
                }
            }
        }*/


    }



    /*private fun setEvents() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0) {
            acquireGooglePlayServices()
        } else if (mCredential?.selectedAccountName == null) {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)
            ) {
                val accountName = this.getPreferences(Context.MODE_PRIVATE)
                    ?.getString(Constants.PREF_ACCOUNT_NAME, null)
                if (accountName != null) {
                    mCredential?.selectedAccountName = accountName
                    setEvents()
                } else {
                    // Start a dialog from which the user can choose an account
                    accountPickerLauncher.launch(mCredential!!.newChooseAccountIntent())
                }
            } else {

                getAccountLauncher.launch(android.Manifest.permission.GET_ACCOUNTS)

            }
        } else if (!isDeviceOnline()) {
            Log.d("SETEVENT","ERROR" )
        } else {
            viewModel.setDataInCalendar(mService!!)
        }
    }*/
}

@Composable
fun BookingApp(bookingState: BookingState = rememberBookingState()){
    Navigation(rememberNavController())
}