package com.mibaldi.virtualassistant.data.server

import android.util.Log
import arrow.core.Either
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mibaldi.virtualassistant.data.datasource.RemoteDataSource
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FirebaseDataSource @Inject constructor(): RemoteDataSource {
    override suspend fun getEvents(): Either<MyError, List<Event>> {
        val list = (0..9).map {
            if (it == 0){
                Event(it,"Bookings", thumb = "https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/cd-make-a-booking-300x300.png?alt=media&token=f445457e-0aba-499d-bc87-b76d02a8e496")
            } else {
                Event(it,"Event $it", thumb = "https://loremflickr.com/400/400/cat?lock=$it")
            }
        }
        return Either.Right(list)
    }

    override suspend fun getBookings(): Either<MyError, List<Event>> {

        return suspendCancellableCoroutine { continuation ->
            val database = Firebase.database
            val bookings = database.getReference("bookings")
            bookings.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.getValue<List<RemoteEvent>>()?.let {
                        it.map { it.toDomainEvent() }
                    } ?: emptyList()
                    continuation.resume(Either.Right(list))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("FIREBASE",error.toException())
                    continuation.resume(Either.Left(MyError.Server(error.code)))

                }
            })
        }

    }

    fun RemoteEvent.toDomainEvent() = Event(id,name,phone,thumb)

}