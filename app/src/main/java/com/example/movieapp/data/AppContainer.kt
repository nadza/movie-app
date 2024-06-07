package com.example.movieapp.data

import com.example.movieapp.network.MovieAppApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

interface AppContainer {
    val movieAppRepository: MovieAppRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://api.themoviedb.org/3/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MovieAppApiService by lazy {
        retrofit.create(MovieAppApiService::class.java)
    }

    override val movieAppRepository: MovieAppRepository by lazy {
        NetworkMovieAppRepository(retrofitService)
    }
}
