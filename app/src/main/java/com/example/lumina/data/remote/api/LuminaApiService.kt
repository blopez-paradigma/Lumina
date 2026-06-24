package com.example.lumina.data.remote.api

import com.example.lumina.data.remote.dto.JournalEntryDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LuminaApiService {

    @GET("posts")
    suspend fun getEntries(): List<JournalEntryDto>

    @POST("posts")
    suspend fun createEntry(@Body dto: JournalEntryDto): JournalEntryDto

    @PUT("posts/{id}")
    suspend fun updateEntry(@Path("id") id: Long, @Body dto: JournalEntryDto): JournalEntryDto

    @DELETE("posts/{id}")
    suspend fun deleteEntry(@Path("id") id: Long)
}
