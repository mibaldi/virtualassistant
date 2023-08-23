package com.mibaldi.virtualassistant.ui.bookings

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.initCalendarBuild
import com.mibaldi.virtualassistant.data.managers.initCredentials
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.showAppSettingsDialog
import com.mibaldi.virtualassistant.ui.common.showPermissionExplanationDialog
import com.mibaldi.virtualassistant.ui.navigation.Feature
import com.mibaldi.virtualassistant.ui.navigation.NavCommand
import com.mibaldi.virtualassistant.ui.navigation.NavItem
import com.mibaldi.virtualassistant.ui.navigation.navigatePoppingUpToStartDestination


@Composable
fun rememberBookingState(userViewModel: UserViewModel= hiltViewModel(),viewModel: BookingViewModel= hiltViewModel(),navController: NavHostController = rememberNavController()) : BookingState {
    val current = LocalContext.current
    return remember { BookingState(current, userViewModel,viewModel,navController) }
}

class BookingState(
    val context: Context,
    val userViewModel: UserViewModel,
    val vm: BookingViewModel,
    val navController: NavHostController
) {
    val mCredential = context.initCredentials()
    val mService =  initCalendarBuild(mCredential)
    lateinit var accountPickerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
    lateinit var getAccountLauncher: ActivityResultLauncher<String>
    val currentRoute: String
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route
            ?: ""


    val isLoggedIn : UserViewModel.UserIsLogged
        @Composable get() {
            val isLoggedIn by  userViewModel.isLoggedInFlow.collectAsState()
            return isLoggedIn
        }
    val setBooking : BookingViewModel.SetBooking
        @Composable get() {
            val setBooking by vm.setBooking.collectAsState()
            return setBooking
        }
    val error : BookingViewModel.AccountError?
        @Composable get() {
            val error by vm.error.collectAsState()
            return error
        }
    val state : BookingViewModel.UiState
        @Composable get() {
            val state by vm.state.collectAsState()
            return state
        }

    @Composable
    fun generateAccountPickerLauncher(){
            val r = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    if (result.data?.extras != null) {
                        val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                        if (accountName != null) {
                            val settings = (context as Activity).getPreferences(Context.MODE_PRIVATE)
                            val editor = settings?.edit()
                            editor?.putString(Constants.PREF_ACCOUNT_NAME, accountName)
                            editor?.apply()
                            mCredential.selectedAccountName = accountName
                            setEvents(context,mCredential,vm,mService,accountPickerLauncher,getAccountLauncher)
                        }
                    }
                }
            }
            accountPickerLauncher = r
        }


    val requestAuthorizationLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
        @Composable get() = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        }

    @Composable
    fun generateGetAccountLauncher() {
        val r = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            with(context as Activity){
                if (isGranted) setEvents(context,mCredential,vm,mService,accountPickerLauncher,getAccountLauncher) else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS)) {
                        showPermissionExplanationDialog(getAccountLauncher)
                    } else {
                        showAppSettingsDialog()
                    }
                }
            }
        }
        getAccountLauncher = r
    }
}