package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.data.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val movies by viewModel.movies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Videoteca de $username") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_movie") }) {
                Icon(Icons.Default.Add, "Añadir película")
            }
        }
    ) { padding ->
        if (movies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay películas añadidas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(
                        movie = movie,
                        onClick = { navController.navigate("detail/${movie.id}") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(movie.title, style = MaterialTheme.typography.titleLarge)
            Text(movie.genre.name, style = MaterialTheme.typography.bodyMedium)
            Text("${movie.duration} min", style = MaterialTheme.typography.bodySmall)
        }
    }
}