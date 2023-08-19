package com.mibaldi.virtualassistant.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mibaldi.virtualassistant.R
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.ui.bookings.BookingViewModel
import com.mibaldi.virtualassistant.ui.common.theme.Purple40
import com.mibaldi.virtualassistant.ui.common.theme.Purple80
import com.mibaldi.virtualassistant.ui.common.theme.PurpleGrey40
import com.mibaldi.virtualassistant.ui.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun Greeting(vm: MainViewModel = hiltViewModel(), onNameClick: (String)-> Unit) {
    val name by vm.name.collectAsState()
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Hello $name!",
            modifier = Modifier
                .padding(30.dp)
                .clickable { onNameClick(name) }

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(text: String, onUpClick:(()->Unit)? = null,logout:(()->Unit)? = null) {
    val navIcon:(@Composable () -> Unit)?   =
        if (onUpClick != null) {
            { ArrowBackIcon(onUpClick)}
        } else {
            null
        }
    TopAppBar(
        title = { Text(text) },
        navigationIcon = navIcon ?: {},
        actions = {
            if (logout != null){
                AppBarAction(
                    imageVector = Icons.Default.Logout,
                    onClick = { logout() }
                )
            }
        }
    )
}
@Composable
fun ArrowBackIcon(onUpClick: () -> Unit) {
    IconButton(onClick = onUpClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}
@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}
@Composable
fun Title(eventItem: Event) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = eventItem.name,
            // style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun Thumb(eventItem: Event, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {

        AsyncImage(
            model = eventItem.thumb,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun CustomDialog(openDialogCustom: MutableState<Boolean>,bookingClicked: MutableState<Event>) {
    Dialog(onDismissRequest = { openDialogCustom.value = false}) {
        CustomDialogUI(openDialogCustom = openDialogCustom, booking = bookingClicked)
    }
}

//Layout
@Composable
fun CustomDialogUI(modifier: Modifier = Modifier, openDialogCustom: MutableState<Boolean>,booking: MutableState<Event>,vm: BookingViewModel = hiltViewModel() ){
    val calendarInstance = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val dayMonthYear = formatter.format(Date(calendarInstance.timeInMillis))
    val selectedDate = remember {mutableStateOf(dayMonthYear)}
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
            /*Image(
                painter = painterResource(id = drawableResId),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter  = ColorFilter.tint(
                    color = Purple40
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),

                )*/

            Column(modifier = Modifier.padding(16.dp)) {
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
                    /*OutlinedTextField(
                        value = name,
                        onValueChange = { newName -> name = newName },
                        label = { Text("Name") }
                    )*/
                    BasicText(
                        text = booking.value.phone,
                        modifier = Modifier.clickable {
                            context.openPhone(booking.value.phone)
                        }
                    )
                    //Text(text = booking.value.name)

                    Spacer(modifier = Modifier.height(16.dp))

                    DatePickerUI(selectedDate,mTime)

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

                androidx.compose.material3.TextButton(onClick = {
                    openDialogCustom.value = false
                }) {
                    Text(
                        "Cancelar",
                        fontWeight = FontWeight.Bold,
                        color = PurpleGrey40,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                androidx.compose.material3.TextButton(onClick = {
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

fun Context.openPhone(numberPhone:String){
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:${numberPhone.replace(" ","")}")
    startActivity(dialIntent)
}
@Preview(showSystemUi = true)
@Composable
fun preview(){
    val calendarInstance = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val dayMonthYear = formatter.format(Date(calendarInstance.timeInMillis))
    val selectedDate = remember {mutableStateOf(dayMonthYear)}
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
            Image(
                painter = painterResource(id = R.drawable.no_image_available),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter  = ColorFilter.tint(
                    color = Purple40
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "booking.value.name",
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
                    /*OutlinedTextField(
                        value = name,
                        onValueChange = { newName -> name = newName },
                        label = { Text("Name") }
                    )*/

                    BasicText(
                        text = "booking.value.phone",
                        modifier = Modifier.clickable {
                            context.openPhone("booking.value.phone")
                        }
                    )
                    Text(text = "booking.value.name")

                    Spacer(modifier = Modifier.height(16.dp))

                    DatePickerUI(selectedDate,mTime)

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

                androidx.compose.material3.TextButton(onClick = {
                    //openDialogCustom.value = false
                }) {
                    Text(
                        "Not Now",
                        fontWeight = FontWeight.Bold,
                        color = PurpleGrey40,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                androidx.compose.material3.TextButton(onClick = {
                    /*openDialogCustom.value = false
                    vm.createBooking(booking.value.name,selectedDate.value,mTime.value)*/
                }) {
                    Text(
                        "Allow",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}