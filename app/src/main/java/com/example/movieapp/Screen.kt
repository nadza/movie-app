@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.movieapp

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.data.DataSource
import com.example.movieapp.ui.screens.MovieAppViewModel
import com.example.movieapp.ui.screens.MovieScreen
import com.example.movieapp.ui.screens.PickContent
import com.example.movieapp.ui.screens.SelectGenre
import com.example.movieapp.ui.screens.SelectReleaseYear
import com.example.movieapp.ui.theme.*

enum class Screen(@StringRes val title: Int) {
    Start(title = R.string.movie),
    Genre(title = R.string.genre),
    ReleaseYear(title = R.string.year_of_release),
    Summary(title = R.string.all)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieAppBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                stringResource(currentScreen.title),
                color = onPrimaryLight) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = onPrimaryContainerLight

        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun MovieApp(
    viewModel: MovieAppViewModel = viewModel(factory = MovieAppViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Start.name
    )

    Scaffold(
        topBar = {
            MovieAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            composable(route = Screen.Start.name) {
                val context = LocalContext.current

                PickContent(
                    onNextButtonClicked = {
                        viewModel.setTypeOfContent(it)
                        navController.navigate(Screen.Genre.name)
                    },
                    options = DataSource.typeOfContent.map { id -> context.resources.getString(id) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = Screen.Genre.name) {
                val context = LocalContext.current
                val opcije = when (viewModel.getTypeOfContent()) {
                    "Film" -> DataSource.genres.map { pair ->
                        Pair(context.resources.getString(pair.first), pair.second)
                    }
                    "Serija" -> DataSource.tvShowList.map { pair ->
                        Pair(context.resources.getString(pair.first), pair.second)
                    }
                    else -> emptyList()
                }
                SelectGenre(
                    onNextButtonClicked = {
                        navController.navigate(Screen.ReleaseYear.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options =opcije,
                    onSelectionChanged = {pair->
                        viewModel.setGenre(pair.first)
                        viewModel.setGenreID(pair.second.toString())
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = Screen.ReleaseYear.name) {
                SelectReleaseYear(
                    onNextButtonClicked = { navController.navigate(Screen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.years,
                    onSelectionChanged = {
                        viewModel.setYear(it)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = Screen.Summary.name) {
                val movieViewModel: MovieAppViewModel = viewModel(factory = MovieAppViewModel.Factory)
                val uiState by viewModel.uiState.collectAsState()
                val sadrzaj = uiState.whatContent
                MovieScreen(s = sadrzaj, finalYear = uiState.finalYear, genre = uiState.genreID, movieViewModel = movieViewModel)
            }


        }
    }
}


/**
 * Resets the [OrderUiState] and pops up to [Screen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: MovieAppViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(Screen.Start.name, inclusive = false)
}

/**
 * Creates an intent to share order details
 */
private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
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
