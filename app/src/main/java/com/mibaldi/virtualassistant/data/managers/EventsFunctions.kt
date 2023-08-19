package com.mibaldi.virtualassistant.data.managers

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val trabajo = "adf1d95fec533eb37e30e31dd6c4e0df59c0a30b4ac26adfaf3f44ed8b1252f1@group.calendar.google.com"
const val entrenamientos = "35919c061713127afb13d16fff2188df89e5e0ae092a5a99ad6c8d50860a0e73@group.calendar.google.com"

fun getEvents(mService: Calendar,calendarId:String):List<Event>{
    val now = DateTime(System.currentTimeMillis())
    val eventStrings = ArrayList<GetEventModel>()
    val events = mService.events().list(calendarId)
        .setMaxResults(10)
        .setTimeMin(now)
        .setOrderBy("startTime")
        .setSingleEvents(true)
        .execute()
    val items = events.items

    /*for (event in items) {
        var start = event.start.dateTime
        if (start == null) {
            start = event.start.date
        }

        eventStrings.add(
            GetEventModel(
                summary = event.summary,
                startDate = start.toString()
            )
        )
    }*/
    return items
}
fun setMibaldiEvents(mService: Calendar,eventName: String,dateString:String){
    val newEvent = createEvent(eventName,dateString)
    mService.events()
        .insert(entrenamientos,newEvent)
        .execute()
}
fun createEvent(name:String,date:String): Event {
    val event = Event()
    event.summary = name
    val obtainDate = obtainDate(date)
    val datetimeStart = DateTime(obtainDate, TimeZone.getTimeZone("Europe/Madrid"))
    val datetimeEnd = DateTime(addOneHourToDate(obtainDate), TimeZone.getTimeZone("Europe/Madrid"))
    event.start = EventDateTime().setDateTime(datetimeStart).setTimeZone("Europe/Madrid")
    event.end = EventDateTime().setDateTime(datetimeEnd).setTimeZone("Europe/Madrid")
    return event
}
fun obtainDate(date:String):Date{
    val strDatefullFormat = "yyyy-MM-dd'T'HH:mm:ss"
    val sdffull = SimpleDateFormat(strDatefullFormat, Locale.getDefault())
    val datefinal = sdffull.parse(date)
    return datefinal ?: Date()
}
fun addOneHourToDate(originalDate: Date): Date {
    val calendar = java.util.Calendar.getInstance()
    calendar.time = originalDate
    calendar.add(java.util.Calendar.HOUR_OF_DAY, 1)
    return calendar.time
}
data class GetEventModel(
    var id: Int = 0,
    var summary: String? = "",
    var startDate: String = "",
    var date: Date
)