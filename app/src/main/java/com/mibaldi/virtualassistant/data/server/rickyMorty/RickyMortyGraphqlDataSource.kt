package com.mibaldi.virtualassistant.data.server.rickyMorty

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.mibaldi.virtualassistant.GetCharacterDetailQuery
import com.mibaldi.virtualassistant.GetCharactersByNameAndPageQuery
import com.mibaldi.virtualassistant.data.datasource.RickyMortyDataSource
import com.mibaldi.virtualassistant.data.tryCall
import com.mibaldi.virtualassistant.domain.Info
import com.mibaldi.virtualassistant.domain.MyCharacter
import com.mibaldi.virtualassistant.domain.MyError
import com.mibaldi.virtualassistant.domain.RickyMortyResult
import javax.inject.Inject

class RickyMortyGraphqlDataSource @Inject constructor(private val apolloClient: ApolloClient) :
    RickyMortyDataSource {

    override suspend fun getCharacters(
        page: Int
    ): Either<MyError, RickyMortyResult> = tryCall {
        val execute = apolloClient.query(GetCharactersByNameAndPageQuery(page)).execute()
        execute.data!!.toDomainModel()
    }

    override suspend fun getCharacterDetail(id: String): Either<MyError, MyCharacter> = tryCall {
        val execute = apolloClient.query(GetCharacterDetailQuery(id)).execute()
        execute.data!!.toDomainModel()
    }
}

private fun GetCharactersByNameAndPageQuery.Data.toDomainModel(): RickyMortyResult {
    val info = characters?.info
    val results = characters?.results
    return RickyMortyResult(
        Info(info?.count ?: 0, info?.pages ?: 1, info?.next.toString(), null),
        results?.map { it!!.toDomainModel() } ?: emptyList())
}

private fun GetCharactersByNameAndPageQuery.Result.toDomainModel(): MyCharacter {
    val finalID = id ?: "0"
    val finalName = name ?: "0"
    val finalImage = image ?: "0"
    return MyCharacter(
        id = finalID.toInt(),
        name = finalName,
        image = finalImage
    )
}

private fun GetCharacterDetailQuery.Data.toDomainModel(): MyCharacter {
    with(character!!) {
        val finalID = id ?: "0"
        val finalName = name ?: "0"
        val finalImage = image ?: "0"
        return MyCharacter(
            id = finalID.toInt(),
            name = finalName,
            image = finalImage
        )
    }
}