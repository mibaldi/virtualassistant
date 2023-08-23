package com.mibaldi.virtualassistant.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.bookings.BookingViewModel
import com.mibaldi.virtualassistant.ui.common.theme.Purple80
import com.mibaldi.virtualassistant.ui.common.theme.PurpleGrey40
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CustomDialog(openDialogCustom: MutableState<Boolean>, bookingClicked: MutableState<Event>,contactWhatsapp: Boolean = false) {
    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        CustomDialogUI(openDialogCustom = openDialogCustom, booking = bookingClicked,contactWhatsapp)
    }
}

//Layout
@Composable
fun CustomDialogUI(openDialogCustom: MutableState<Boolean>, booking: MutableState<Event>,contactWhatsapp: Boolean, vm: BookingViewModel = hiltViewModel() ){
    val calendarInstance = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val dayMonthYear = formatter.format(Date(calendarInstance.timeInMillis))
    val selectedDate = remember { mutableStateOf(dayMonthYear) }
    val mHour = calendarInstance[Calendar.HOUR_OF_DAY]
    val mMinute = calendarInstance[Calendar.MINUTE]
    val mTime = remember { mutableStateOf("$mHour:$mMinute:00") }
    val context = LocalContext.current

    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.background(Color.White)) {

            //.......................................................................

            AsyncImage(
                model = booking.value.thumb,
                modifier =  Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = booking.value.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (!contactWhatsapp) {
                                context.openPhone(booking.value.phone)
                            } else {
                                context.whatsapp("Me gustaria coger cita, ¿cuando habria disponible?",booking.value.phone)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                    ) {
                        Text(text = booking.value.phone)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DatePickerUI(selectedDate,mTime)
                    Button(
                        onClick = {
                            context.whatsapp("Reservado en ${booking.value.name} el ${selectedDate.value} a las ${mTime.value}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text(text = "Enviar Whatsapp")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Purple80),
                horizontalArrangement = Arrangement.SpaceAround) {

                TextButton(onClick = {
                    openDialogCustom.value = false
                }) {
                    Text(
                        "Cancelar",
                        fontWeight = FontWeight.Bold,
                        color = PurpleGrey40,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                TextButton(onClick = {
                    openDialogCustom.value = false
                    vm.createBooking(booking.value.name,selectedDate.value,mTime.value)
                }) {
                    Text(
                        "Reservar",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}
