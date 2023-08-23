package com.mibaldi.virtualassistant.data.managers

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val citas = "64c551ece5289b93dd46541fb2fb87415cb86b4513c67c4e74b6ac5b89b3220a@group.calendar.google.com"

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
        .insert(citas,newEvent)
        .execute()
}
fun createEvent(name:String,date:String): Event {
    val event = Event()
    event.summary = name
    val madrid = "Europe/Madrid"
    val timeZone = TimeZone.getTimeZone(madrid)
    val obtainDate = obtainDate(date)
    val datetimeStart = DateTime(obtainDate, timeZone)
    val datetimeEnd = DateTime(addOneHourToDate(obtainDate), timeZone)
    event.start = EventDateTime().setDateTime(datetimeStart).setTimeZone(madrid)
    event.end = EventDateTime().setDateTime(datetimeEnd).setTimeZone(madrid)
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