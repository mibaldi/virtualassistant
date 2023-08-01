package com.mibaldi.virtualassistant.data

import arrow.core.Either
import com.mibaldi.virtualassistant.data.datasource.RemoteDataSource
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource){
    suspend fun getEvents(): Either<MyError, List<Event>>{
        return remoteDataSource.getEvents()
    }
}