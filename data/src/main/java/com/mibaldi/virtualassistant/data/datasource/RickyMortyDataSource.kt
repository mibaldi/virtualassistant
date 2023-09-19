package com.mibaldi.virtualassistant.data.datasource

import arrow.core.Either
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.domain.RickyMortyResult

interface RickyMortyDataSource {
    suspend fun getCharacters(page: Int): Either<MyError, RickyMortyResult>
    suspend fun getCharacterDetail(id: String): Either<MyError, MyCharacter>
}