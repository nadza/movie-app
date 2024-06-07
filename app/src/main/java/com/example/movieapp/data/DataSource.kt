package com.example.movieapp.data

import com.example.movieapp.R

object DataSource {
    val genres = listOf(
        Pair(R.string.Action, 28),
        Pair(R.string.Adventure, 12),
        Pair(R.string.Animation, 16),
        Pair(R.string.Comedy, 35),
        Pair(R.string.Crime, 80),
        Pair(R.string.Documentary, 99),
        Pair(R.string.Drama, 18),
        Pair(R.string.Family, 10751),
        Pair(R.string.Fantasy, 14),
        Pair(R.string.History, 36),
        Pair(R.string.Horror, 27),
        Pair(R.string.Music, 10402),
        Pair(R.string.Mystery, 9648),
        Pair(R.string.Romance, 10749),
        Pair(R.string.sf, 878),
        Pair(R.string.tv_movie, 10770),
        Pair(R.string.Thriller, 53),
        Pair(R.string.War, 10752),
        Pair(R.string.Western, 37)
    )
    val additionalGenres = listOf(
        Pair(R.string.Action_and_Adventure, 10759),
        Pair(R.string.Kids, 10762),
        Pair(R.string.Mystery, 9648),
        Pair(R.string.News, 10763),
        Pair(R.string.Reality, 10764),
        Pair(R.string.Sci_Fi_and_Fantasy, 10765),
        Pair(R.string.Soap, 10766),
        Pair(R.string.Talk, 10767),
        Pair(R.string.War_and_Politics, 10768)
    )
    val tvShowList = genres + additionalGenres

    val year = listOf(
        2024,2023,2022,2021,2020,2019,2018,2017,2016,2015,2014,2013,2012,2011,2010
    )

    val typeOfContent = listOf(
        R.string.movie,
        R.string.tv_show

    )
}