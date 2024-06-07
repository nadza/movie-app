package com.example.movieapp.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.movieapp.R
import com.example.movieapp.ui.screens.MovieAppUiState
import com.example.movieapp.ui.screens.MovieAppViewModel
import com.example.movieapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MovieScreen(
    s: String,
    finalYear: Int,
    genre: String,
    movieViewModel: MovieAppViewModel = viewModel(factory = MovieAppViewModel.Factory)
) {
    val movieAppUiState = movieViewModel.movieAppUiState

    LaunchedEffect(Unit) {
        if(s == "Film")
            movieViewModel.getMovies("d2061bb798cdcea271dd0e449406106c", finalYear, genre)
        else
            movieViewModel.getSeries("d2061bb798cdcea271dd0e449406106c", finalYear, genre)

    }

    when (movieAppUiState) {
        is MovieAppUiState.Loading -> {
            CircularProgressIndicator()
        }
        is MovieAppUiState.Success -> {
            val movies = (movieAppUiState as MovieAppUiState.Success).movies

            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                Modifier.height(20.dp)
            ) {
                items(count = movies.size) { index ->
                    val movie = movies[index]

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = tertiaryContainerLightMediumContrast
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .height(240.dp)
                            .width(150.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .wrapContentSize(Alignment.Center)
                                .align(Alignment.CenterHorizontally) // Center the image horizontally within the column
                        ) {
                            movie.poster_path?.let { posterUrl ->
                                Image(
                                    painter = rememberImagePainter(
                                        data = "https://image.tmdb.org/t/p/w500$posterUrl",
                                        builder = {
                                            crossfade(true)
                                        }
                                    ),
                                    contentDescription = "Poster: ${movie.title}",
                                    modifier = Modifier
                                        .width(100.dp) // Width of the movie poster image
                                        .height(150.dp) // Height of the movie poster imag
                                )
                            }
                            Text(text = movie.title)
                            Text(text = formatReleaseDate(movie.release_date))

                            val a = LocalContext.current
                            IconButton(onClick = { shareOrder(a, "Chosen movie:", movie.title + "\n" + formatReleaseDate(movie.release_date)) }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share"
                                )

                            }
                        }
                    }
                }
            }
        }
        is MovieAppUiState.Error -> {
            ErrorScreen()
        }
    }
}

fun formatReleaseDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd. MMMM yyyy.", Locale("bs", "BA"))
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.l2),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

    }
}

private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}