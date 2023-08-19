package com.mibaldi.virtualassistant.ui.common

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(selectedDate:MutableState<String>,mTime: MutableState<String>){
    val mContext = LocalContext.current

    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute:00"
        }, mHour, mMinute, true
    )
    // set the initial date
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = mCalendar.timeInMillis)

    var showDatePicker by remember {
        mutableStateOf(false)
    }


    if (showDatePicker) {

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
                    val dayMonthYear = formatter.format(Date(datePickerState.selectedDateMillis!!))
                    selectedDate.value = dayMonthYear
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            Log.d("DATEPICKER","DATE PICKER CREADO")

            DatePicker(
                state = datePickerState
            )
        }
    }

    Button(
        onClick = {
            Log.d("DATEPICKER","DIALOG PULSADO")
            showDatePicker = true
        }
    ) {
        Text(text = "Seleccionar Fecha")
    }
    Button(
        onClick =  { mTimePickerDialog.show() }
    ) {
        Text(text = "Seleccionar Hora")
    }
    Text(
        text = "Selected date: ${selectedDate.value} and ${mTime.value}"
    )
}

