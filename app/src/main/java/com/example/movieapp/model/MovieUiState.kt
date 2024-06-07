package com.example.movieapp.data

data class MovieUiState(
    val years: List<Int> = DataSource.year,
    val finalYear : Int = 2023,
    val genre: String = "",
    val whatContent: String = "",
    val genreID: String = "",
)