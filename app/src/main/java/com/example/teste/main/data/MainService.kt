package com.example.teste.main.data

import com.example.teste.main.data.response.DataWords
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MainService {

    @GET("api/v2/entries/en/{word}")
    suspend fun getWord(
        @Path("word") word: String
    ): Response<List<DataWords>>
}