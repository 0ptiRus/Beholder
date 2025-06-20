package com.evermore.beholder.domain.interfaces

import com.evermore.beholder.data.dto.BestiaryResponse
import com.evermore.beholder.data.dto.ClassData
import com.evermore.beholder.data.dto.ClassLevel
import com.evermore.beholder.data.dto.Monster
import com.evermore.beholder.data.dto.Race
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Dnd5eApi {
    @GET("classes/{index}")
    suspend fun getClass(
        @Path("index") index: String
    ): ClassData

    @GET("classes/{index}/levels")
    suspend fun getClassLevels(
        @Path("index") index: String
    ): List<ClassLevel>

    @GET("races/{index}")
    suspend fun getRace(
        @Path("index") index: String
    ): Race

    @GET("monsters")
    suspend fun getMonsters(
        @Query("challenge_rating") challengeRating: String? = null
    ): BestiaryResponse

    @GET("monsters/{index}") // Новый эндпоинт для получения деталей монстра
    suspend fun getMonster(
        @Path("index") index: String
    ): Monster
}