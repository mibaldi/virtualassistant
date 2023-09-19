package com.mibaldi.virtualassistant.domain


data class RickyMortyResult(
    val info: Info,
    val results: List<MyCharacter>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class MyCharacter(
    val id: Int = 0,
    val name: String,
    val image: String,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: Location? = null,
    val location: Location? = null,
    val episode: List<String>? = null,
    val url: String? = null,
    val created: String? = null
)

data class Location(
    val name: String,
    val url: String
)