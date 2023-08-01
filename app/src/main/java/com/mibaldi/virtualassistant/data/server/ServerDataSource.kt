package com.mibaldi.virtualassistant.data.server

import arrow.core.Either
import com.mibaldi.virtualassistant.data.datasource.RemoteDataSource
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import javax.inject.Inject

class ServerDataSource @Inject constructor(): RemoteDataSource {
    override suspend fun getEvents(): Either<MyError, List<Event>> {
        val list = (1..10).map {
            Event(it,"Event $it","https://loremflickr.com/400/400/cat?lock=$it")
        }
        return Either.Right(list)
    }
}