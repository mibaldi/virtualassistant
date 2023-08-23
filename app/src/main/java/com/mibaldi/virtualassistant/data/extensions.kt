package com.mibaldi.virtualassistant.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mibaldi.virtualassistant.domain.MyError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toError(): MyError = when (this) {
    is IOException -> MyError.Connectivity
    is HttpException -> MyError.Server(code())
    else -> MyError.Unknown(message ?: "")
}

suspend fun <T> tryCall(action: suspend () -> T): Either<MyError, T> = try {
    action().right()
} catch (e: Exception) {
    e.toError().left()
}