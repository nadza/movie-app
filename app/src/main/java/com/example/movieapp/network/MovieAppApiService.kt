package com.example.movieapp.network

import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.TvResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAppApiService  {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("primary_release_year") year: Int,
        @Query("with_genres") genre: String

    ): MovieResponse

    @GET("discover/tv")
    suspend fun getTV(
        @Query("api_key") apiKey: String,
        @Query("first_air_date_year") year: Int,
        @Query("with_genres") genre: String

    ): TvResponse
}