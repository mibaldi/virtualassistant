package com.mibaldi.virtualassistant.data.datasource

import arrow.core.Either
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError

interface RemoteDataSource {
    suspend fun getEvents(): Either<MyError, List<Event>>
    suspend fun getBookings(): Either<MyError, List<Event>>
}