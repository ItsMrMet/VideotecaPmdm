package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.navigation.Screen
import com.thomasvaneemeren.videotecapmdm.ui.components.MovieCard
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.MainViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val movies by viewModel.movies.collectAsState()
    val favoriteIds by viewModel.favoriteIdsFlow.collectAsState(initial = emptySet())
    val userName by viewModel.userName.collectAsState()
    val favoritesOnly by viewModel.favoritesOnly.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    ScaffoldLayout(
        userName = userName,
        navController = navController,
        currentRoute = Screen.Main.route
    ) { padding ->

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.35f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.setSearchQuery(it)
                        },
                        label = { Text("Buscar película") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IconToggleButton(
                        checked = favoritesOnly,
                        onCheckedChange = { viewModel.toggleFavoritesOnly() }
                    ) {
                        Icon(
                            imageVector = if (favoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Mostrar solo favoritos"
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(0.65f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies.size) { index ->
                        val movie = movies[index]
                        MovieCard(
                            movie = movie,
                            isFavorite = movie.id in favoriteIds,
                            onToggleFavorite = { viewModel.toggleFavorite(movie.id) },
                            onClick = { navController.navigate("detail/${movie.id}") }
                        )
                    }
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.setSearchQuery(it)
                            },
                            label = { Text("Buscar película") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        IconToggleButton(
                            checked = favoritesOnly,
                            onCheckedChange = { viewModel.toggleFavoritesOnly() }
                        ) {
                            Icon(
                                imageVector = if (favoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Mostrar solo favoritos"
                            )
                        }
                    }

                    if (movies.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron películas.")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(movies.size) { index ->
                                val movie = movies[index]
                                MovieCard(
                                    movie = movie,
                                    isFavorite = movie.id in favoriteIds,
                                    onToggleFavorite = { viewModel.toggleFavorite(movie.id) },
                                    onClick = { navController.navigate("detail/${movie.id}") }
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Add.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir película")
                }
            }
        }
    }
}
