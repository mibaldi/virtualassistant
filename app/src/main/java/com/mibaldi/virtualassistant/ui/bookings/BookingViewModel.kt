package com.mibaldi.virtualassistant.ui.bookings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.calendar.Calendar
import com.mibaldi.virtualassistant.data.managers.Constants
import com.mibaldi.virtualassistant.data.managers.setMibaldiEvents
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.usecases.GetBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state : StateFlow<UiState> = _state.asStateFlow()

    private val _setBooking = MutableStateFlow(SetBooking("",""))
    val setBooking : StateFlow<SetBooking> = _setBooking.asStateFlow()
    private val _error = MutableStateFlow<AccountError?>(null)
    val error : StateFlow<AccountError?> = _error.asStateFlow()

    fun getBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            getBookingsUseCase().fold(
                ifLeft = { cause -> _state.update { it.copy(error = cause, loading =  false) }},
                ifRight = {result ->
                    _state.update { UiState(events = result) }
                }
            )
        }
    }
    fun setToast(state:Boolean) {
        _state.update { it.copy(showToast = state) }
    }
    fun setDataInCalendar(mService:Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setMibaldiEvents(mService, setBooking.value.name, setBooking.value.dateString)
                setToast(true)
            } catch (e: IOException) {
                Log.d("Google", e.message.toString())
                if (e is UserRecoverableAuthIOException) {
                    _error.value = AccountError(e)
                }
            }
        }
    }
    fun createBooking(name:String,date: String,time:String){
        val dateString = "${date}T$time"
        _setBooking.value = SetBooking(name,dateString)
        Log.d("CREATEBOOKING","$name: $dateString")
    }
    data class SetBooking(val name:String,val dateString:String)
    data class AccountError(val exception: UserRecoverableAuthIOException)
    data class UiState(
        val loading: Boolean = false,
        val showToast: Boolean = false,
        val events: List<Event>? = null,
        val error: MyError? = null
    )
}
