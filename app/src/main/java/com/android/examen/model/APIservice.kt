package com.android.examen.model

import com.android.examen.model.DogResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIservice {
    @GET
    suspend fun getDogsByBreeds(@Url url: String):retrofit2.Response<DogResponse>
}