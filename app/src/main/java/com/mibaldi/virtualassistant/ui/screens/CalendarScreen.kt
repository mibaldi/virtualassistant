package com.mibaldi.virtualassistant.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.api.services.calendar.model.Event
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.acquireGooglePlayServices
import com.mibaldi.virtualassistant.data.managers.citas
import com.mibaldi.virtualassistant.data.managers.isDeviceOnline
import com.mibaldi.virtualassistant.databinding.CalendarLayoutBinding
import com.mibaldi.virtualassistant.ui.bookings.BookingState
import com.mibaldi.virtualassistant.ui.bookings.BookingViewModel
import com.mibaldi.virtualassistant.ui.bookings.rememberBookingState
import com.mibaldi.virtualassistant.ui.bookings.workWithEvents
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.savvi.rangedatepicker.CalendarPickerView
import pub.devrel.easypermissions.EasyPermissions
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun CalendarScreen(onClick: ()->Unit, bookingViewModel: BookingViewModel = hiltViewModel(), bookingState: BookingState = rememberBookingState()) {

    bookingState.generateAccountPickerLauncher(){
        bookingViewModel.getMibaldiEvents(bookingState.mService!!,citas)
    }
    bookingState.generateGetAccountLauncher(){
        bookingViewModel.getMibaldiEvents(bookingState.mService!!,citas)
    }

    Column {
        GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/sam-sam-from-samsung.gif?alt=media&token=47bd221f-89ca-4673-b170-70a136af7d16")
        Text(text = "Calendario",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        /*Button(onClick = {

            navController.navigate("booking")
        }) {
            Text(text = "Booking")
        }*/
        Button(onClick) {
            Text(text = "Booking")
        }


        val calendarState by bookingViewModel.calendarState.collectAsState()
        LaunchedEffect(calendarState.calendarEvents){
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
                            workWithEvents(bookingState.context,bookingState.mCredential,bookingViewModel,bookingState.mService,bookingState.accountPickerLauncher,bookingState.getAccountLauncher){
                                bookingViewModel.getMibaldiEvents(bookingState.mService!!, citas)
                            }
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
                    bookingViewModel.getMibaldiEvents(bookingState.mService!!, citas)
                }
            }

        }
        AndroidViewBinding(
            factory = CalendarLayoutBinding::inflate,
            update = {
                val currentDay = Calendar.getInstance().time
                val yearafter = Calendar.getInstance()
                val yearBefore = Calendar.getInstance()
                yearafter.add(Calendar.MONTH, 12)
                //yearBefore.add(Calendar.MONTH,-1)
                calendarView.init(yearBefore.time, yearafter.time).inMode(
                    CalendarPickerView.SelectionMode.MULTIPLE).withSelectedDate(currentDay)

                val list = calendarState.calendarEvents?.filter { generateDate(it) >= currentDay }?.map {event->
                    generateDate(event)
                }?.toList()
                list?.let {
                    calendarText.text = calendarState.calendarEvents?.filter { generateDate(it) >= currentDay }?.map {
                        val date = generateDate(it)
                        "${it.summary}: $date"
                    }.toString()
                    calendarView.highlightDates(list)
                    calendarView
                }
            }
        )
    }


}

fun generateDate(event:Event) :Date{
    var start = event.start.dateTime
    if (start == null) {
        start = event.start.date
    }
    val startLong = start.value
    return Date(startLong)
}

