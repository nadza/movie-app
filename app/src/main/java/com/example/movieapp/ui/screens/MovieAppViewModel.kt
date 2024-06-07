package com.example.movieapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movieapp.MovieAppApplication
import com.example.movieapp.data.MovieAppRepository
import com.example.movieapp.data.MovieUiState
import com.example.movieapp.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MovieAppUiState {
    data class Success(val movies: List<Movie>) : MovieAppUiState
    object Error : MovieAppUiState
    object Loading : MovieAppUiState
}

class MovieAppViewModel(private val marsPhotosRepository: MovieAppRepository) : ViewModel() {
    var movieAppUiState: MovieAppUiState by mutableStateOf(MovieAppUiState.Loading)
        private set

    init {
        getMovies("d2061bb798cdcea271dd0e449406106c", 2024, "28")
    }

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun setTypeOfContent(typeContent: String) {
        _uiState.update { currentState ->
            currentState.copy(
                whatContent = typeContent
            )
        }
    }

    fun getTypeOfContent(): String {
        return _uiState.value.whatContent
    }

    fun setGenre(contentGenre: String) {
        _uiState.update { currentState ->
            currentState.copy(
                genre = contentGenre
            )
        }
    }

    fun setGenreID(contentGenreID: String) {
        _uiState.update { currentState ->
            currentState.copy(
                genreID = contentGenreID
            )
        }
    }

    fun setYear(year: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                finalYear = year
            )
        }
    }

    fun resetOrder() {
        _uiState.value = MovieUiState()
    }

    fun getMovies(apiKey: String, year: Int, genre: String) {
        viewModelScope.launch {
            movieAppUiState = MovieAppUiState.Loading
            try {
                val response = marsPhotosRepository.getMovieList(apiKey, year, genre)
                movieAppUiState = MovieAppUiState.Success(response)
            } catch (e: IOException) {
                movieAppUiState= MovieAppUiState.Error
            } catch (e: HttpException) {
                movieAppUiState = MovieAppUiState.Error
            }
        }
    }

    fun getSeries(apiKey: String, year: Int, genre: String) {
        viewModelScope.launch {
            movieAppUiState = MovieAppUiState.Loading
            try {
                val response = marsPhotosRepository.getTvList(apiKey, year, genre)
                movieAppUiState = MovieAppUiState.Success(response)
            } catch (e: IOException) {
                movieAppUiState= MovieAppUiState.Error
            } catch (e: HttpException) {
                movieAppUiState = MovieAppUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieAppApplication)
                val movieAppRepository = application.container.movieAppRepository
                MovieAppViewModel(marsPhotosRepository = movieAppRepository)
            }
        }
    }
}