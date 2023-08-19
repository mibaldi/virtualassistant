package com.mibaldi.virtualassistant.ui.bookings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.Calendar
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.Constants.REQUEST_AUTHORIZATION
import com.mibaldi.virtualassistant.data.managers.getResultsFromApi
import com.mibaldi.virtualassistant.data.managers.initCredentials
import com.mibaldi.virtualassistant.data.managers.onActivityResult
import com.mibaldi.virtualassistant.data.managers.setMibaldiEvents
import com.mibaldi.virtualassistant.data.managers.showGooglePlayServicesAvailabilityErrorDialog
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.common.CustomDialog
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.Thumb
import com.mibaldi.virtualassistant.ui.common.Title
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.errorToString
import com.mibaldi.virtualassistant.ui.common.goToHome
import com.mibaldi.virtualassistant.ui.common.launchAndCollect
import com.mibaldi.virtualassistant.ui.common.theme.VirtualAssistantTheme
import com.mibaldi.virtualassistant.ui.login.LoginViewModel
import com.mibaldi.virtualassistant.ui.main.EventList
import com.mibaldi.virtualassistant.ui.main.EventListItem
import com.mibaldi.virtualassistant.ui.main.MainContent
import com.mibaldi.virtualassistant.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class BookingFragment : Fragment(){
    private val viewModel: BookingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var mCredential: GoogleAccountCredential? = null //to access our account
    private var mService: Calendar? = null //To access the calendar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getBookings()
        initCredentials2()
        userViewModel.isLoggedIn.observe(viewLifecycleOwner){
                isLoggedIn ->
            if (!isLoggedIn) {
                // User is logged out, perform necessary actions
                activity?.goToHome()
                activity?.finish()
            }
        }
        launchAndCollect(viewModel.setBooking){
            if (it.name.isNotEmpty() && it.dateString.isNotEmpty()) {
                setEvents()
            }
        }
        launchAndCollect(viewModel.error){
            if (it != null){
                startActivityForResult(it.exception.intent, Constants.REQUEST_AUTHORIZATION)
            }
        }
        return composeView {
            BookingScreen({
                Log.d("CLICK","elemento clicado $it")
            },{
                userViewModel.setUserLoggedOut()
            })
        }
    }
    private fun initCredentials2() {
        mCredential = activity?.initCredentials()
        initCalendarBuild(mCredential)
    }
    private fun initCalendarBuild(credential: GoogleAccountCredential?) {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = Calendar.Builder(transport, jsonFactory, credential)
            .setApplicationName("MibaldiCalendar").build()
    }
    private fun setEvents() {
        getResultsFromApi(mCredential!!, {
            Log.d("SETEVENT","ERROR" )

            // binding.txtOut.text = "No network connection available."
        }, {
            viewModel.setDataInCalendar(mService!!)
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(mCredential!!,requestCode,resultCode,data,{
           // binding.txtOut.text = "This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app."
        },{
            setEvents()
        })

    }
}
fun Fragment.composeView(content: @Composable () -> Unit) : ComposeView{
    return ComposeView(requireContext()).apply {
        setContent {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            content()
        }
    }
}