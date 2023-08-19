package com.mibaldi.virtualassistant.data

import arrow.core.Either
import com.mibaldi.virtualassistant.data.datasource.LocalDataSource
import com.mibaldi.virtualassistant.data.datasource.RemoteDataSource
import com.mibaldi.virtualassistant.domain.Event
import com.mibaldi.virtualassistant.domain.MyError
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource){
    suspend fun getEvents(): Either<MyError, List<Event>>{
        return remoteDataSource.getEvents()
    }
    suspend fun getBookings(): Either<MyError, List<Event>>{
        return remoteDataSource.getBookings()
    }


    fun userIsLogged() : Boolean{
        return localDataSource.isLogged()
    }
    fun setUserIsLogged(logged: Boolean){
        localDataSource.setLogged(logged)
    }
}