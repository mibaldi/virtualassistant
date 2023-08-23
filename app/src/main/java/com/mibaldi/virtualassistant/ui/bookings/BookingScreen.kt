package com.mibaldi.virtualassistant.ui.bookings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.calendar.Calendar
import com.mibaldi.virtualassistant.MyAppComposable
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.acquireGooglePlayServices
import com.mibaldi.virtualassistant.data.managers.isDeviceOnline
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.common.CustomDialog
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.MainAppBar
import com.mibaldi.virtualassistant.ui.common.Thumb
import com.mibaldi.virtualassistant.ui.common.Title
import com.mibaldi.virtualassistant.ui.common.UserViewModel
import com.mibaldi.virtualassistant.ui.common.goToHome
import pub.devrel.easypermissions.EasyPermissions


@ExperimentalFoundationApi
@Composable
fun BookingScreen(onNavigate: (Int) -> Unit,nav:()->Unit,vm: BookingViewModel = hiltViewModel(),bookingState: BookingState = rememberBookingState()) {


    bookingState.generateAccountPickerLauncher()
    bookingState.generateGetAccountLauncher()
    LaunchedEffect(Unit){
        vm.getBookings()
    }
    val showToast = bookingState.state.showToast
    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(bookingState.context,"Evento creado",Toast.LENGTH_SHORT).show()
            vm.setToast(false) // Restablece el valor en el ViewModel a false
        }
    }
    val logged = bookingState.isLoggedIn.isLogged
    LaunchedEffect(logged){
        if (!logged){
            bookingState.context.goToHome()
            (bookingState.context as Activity).finish()
        }
    }

    val name = bookingState.setBooking.name
    val dateString = bookingState.setBooking.dateString
    LaunchedEffect(name,dateString){
        if(name.isNotEmpty() && dateString.isNotEmpty()){
            with (bookingState.context as Activity){
                if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0) {
                    (bookingState.context as Activity).acquireGooglePlayServices()
                } else if (bookingState.mCredential.selectedAccountName == null) {
                    if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)
                    ) {
                        val accountName = this.getPreferences(Context.MODE_PRIVATE)
                            ?.getString(Constants.PREF_ACCOUNT_NAME, null)
                        if (accountName != null) {
                            bookingState.mCredential.selectedAccountName = accountName
                            setEvents(bookingState.context,bookingState.mCredential,vm,bookingState.mService,bookingState.accountPickerLauncher,bookingState.getAccountLauncher)
                        } else {
                            // Start a dialog from which the user can choose an account
                            bookingState.accountPickerLauncher.launch(bookingState.mCredential.newChooseAccountIntent())
                        }
                    } else {
                        bookingState.getAccountLauncher.launch(android.Manifest.permission.GET_ACCOUNTS)

                    }
                } else if (!isDeviceOnline()) {
                    Log.d("SETEVENT","ERROR" )
                } else {
                    vm.setDataInCalendar(bookingState.mService!!)
                }
            }
        }
    }
    val error = bookingState.error
    val requestAuthorizationLauncher = bookingState.requestAuthorizationLauncher
    LaunchedEffect(error){
        if (error != null){
            requestAuthorizationLauncher.launch(error.exception.intent)
        }
    }
    /*MyAppComposable {
        Scaffold(
            topBar = { MainAppBar(stringResource(id = R.string.app_name), logout = {
                logout()
            }) }
        ) { padding ->*/
            Column {
                GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/sam-sam-from-samsung.gif?alt=media&token=47bd221f-89ca-4673-b170-70a136af7d16")
                Text(text = "Realizar reservas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center)
                Button(nav) {
                    Text(text = "Calendario")
                }
                BookingContent(onNavigate = onNavigate, modifier = Modifier.padding(10.dp), vm = vm)
            }

   // }
}



fun setEvents(context: Context,mCredential: GoogleAccountCredential,viewModel: BookingViewModel,mService:Calendar?,accountPickerLauncher: ActivityResultLauncher<Intent>,getAccountLauncher:ActivityResultLauncher<String>){
    with (context as Activity){
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0) {
            (context as Activity).acquireGooglePlayServices()
        } else if (mCredential.selectedAccountName == null) {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)
            ) {
                val accountName = this.getPreferences(Context.MODE_PRIVATE)
                    ?.getString(Constants.PREF_ACCOUNT_NAME, null)
                if (accountName != null) {
                    mCredential.selectedAccountName = accountName
                    setEvents(context,mCredential,viewModel,mService,accountPickerLauncher,getAccountLauncher)
                } else {
                    // Start a dialog from which the user can choose an account
                    accountPickerLauncher.launch(mCredential.newChooseAccountIntent())
                }
            } else {
                getAccountLauncher.launch(android.Manifest.permission.GET_ACCOUNTS)
            }
        } else if (!isDeviceOnline()) {
            Log.d("SETEVENT","ERROR" )
        } else {
            viewModel.setDataInCalendar(mService!!)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BookingContent(modifier: Modifier = Modifier, vm: BookingViewModel = hiltViewModel(), onNavigate: (Int) -> Unit, ) {
    BookingList(
        onClick = { onNavigate(it.id) },
        modifier = modifier, vm = vm
    )
}
@ExperimentalFoundationApi
@Composable
fun BookingList(
    modifier: Modifier = Modifier,
    vm: BookingViewModel = hiltViewModel(),
    onClick: (Event) -> Unit,
) {
    val openDialogCustom = remember { mutableStateOf(false) }
    val bookingClicked = remember { mutableStateOf(Event(-1,"")) }

    val state by vm.state.collectAsState()
    val list = state.events ?: emptyList()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.cell_min_width)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier
    ) {
        items(list) {
            BookingListItem(
                eventItem = it,
                onClick = {
                    openDialogCustom.value = true
                    bookingClicked.value = it
                    onClick(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall))
            )
        }
    }
    if (openDialogCustom.value && bookingClicked.value.id != -1){
        CustomDialog(openDialogCustom = openDialogCustom, bookingClicked = bookingClicked, bookingClicked.value.name == "fisio")
    }

}


@Composable
fun BookingListItem(
    eventItem: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Column {
            Thumb(
                itemThumb = eventItem.thumb,
                modifier = modifier.height(dimensionResource(R.dimen.cell_thumb_height))
            )
            Title(eventItem.name,modifier = modifier.width(dimensionResource(R.dimen.cell_thumb_height)))
        }
    }
}


/*@Composable
fun SetEvent(bookingViewModel: BookingViewModel= hiltViewModel()){
    val mCredential = LocalContext.current.initCredentials()
    val transport = AndroidHttp.newCompatibleTransport()
    val jsonFactory = JacksonFactory.getDefaultInstance()
    val mService = Calendar.Builder(transport, jsonFactory, mCredential)
        .setApplicationName("MibaldiCalendar").build()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen","PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen","PERMISSION DENIED")
        }
    }
    val collectAsState by bookingViewModel.setBooking.collectAsState()
    if (collectAsState.name.isNotEmpty() && collectAsState.dateString.isNotEmpty()) {
        GetResultsFromApi(mCredential = mCredential, networkError = { /*TODO*/ }) {
            bookingViewModel.setDataInCalendar(mService!!)
        }
    }

}*/