package com.example.movieapp.data

import com.example.movieapp.model.Movie
import com.example.movieapp.model.Tv
import com.example.movieapp.network.MovieAppApiService


interface MovieAppRepository {
    suspend fun getMovieList(apiKey: String, year: Int, genre:String): List<Movie>
    suspend fun getTvList(apiKey: String, year: Int, genre:String): List<Movie>//List<Tv>
}

class NetworkMovieAppRepository(
    private val movieAppApiService: MovieAppApiService
) : MovieAppRepository {

    override suspend fun getMovieList(apiKey: String, year: Int, genre:String): List<Movie> {
        val response = movieAppApiService.getMovies(apiKey, year,genre)
        return response.results
    }

    /*override suspend fun getTvList(apiKey: String, year: Int, genre:String): List<Tv> {
        val response = movieAppApiService.getTV(apiKey, year,genre)
        return response.results
    }*/

    override suspend fun getTvList(apiKey: String, year: Int, genre: String): List<Movie> {
        val response = movieAppApiService.getTV(apiKey, year, genre)
        return response.results.map { tv ->
            TvToMovieMapper.map(tv)
        }
    }

    object TvToMovieMapper {
        fun map(tv: Tv): Movie {
            return Movie(
                adult = tv.adult,
                backdrop_path = tv.backdrop_path,
                genre_ids = tv.genre_ids,
                id = tv.id,
                original_language = tv.original_language,
                original_title = tv.name, // Assuming "name" from TV becomes "original_title" for Movie
                overview = tv.overview,
                popularity = tv.popularity,
                poster_path = tv.poster_path,
                release_date = tv.first_air_date, // Assuming "first_air_date" from TV becomes "release_date" for Movie
                title = tv.name,
                video = false, // Assuming "video" is always false for TV shows
                vote_average = tv.vote_average,
                vote_count = tv.vote_count
            )
        }
    }
}