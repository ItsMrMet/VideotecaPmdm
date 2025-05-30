package com.thomasvaneemeren.videotecapmdm.ui.screens.detail

import android.graphics.Movie
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.DetailViewModel
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@Composable
fun DetailScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val movie by viewModel.getMovieById(movieId).collectAsState(initial = null)
    val userName by userPreferencesViewModel.userName.collectAsState()
    val isAdmin by userPreferencesViewModel.isAdmin.collectAsState(initial = false)
    val isFavorite by viewModel.isFavorite(movieId).collectAsState(initial = false)

    movie?.let { movie ->
        ScaffoldLayout(
            userName = userName,
            navController = navController,
            currentRoute = "detail/$movieId"
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Barra superior con botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }

                    Row {
                        if (isAdmin) {
                            IconButton(
                                onClick = {
                                    viewModel.deleteMovie(movie)
                                    navController.navigate("main")
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar película")
                            }
                        }

                        IconButton(onClick = {
                            navController.navigate("edit/${movie.id}")
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                }

                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                DetailText(label = "Género", value = movie.genre)
                DetailText(label = "Director", value = movie.director)
                DetailText(label = "Duración", value = "${movie.duration} min")
                DetailText(label = "Favorita", value = if (isFavorite) "Sí" else "No")

                Text("Sinopsis", style = MaterialTheme.typography.titleMedium)
                Text(movie.synopsis, style = MaterialTheme.typography.bodyMedium)

                // Para debug
                Text("isAdmin: $isAdmin")
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DetailText(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}


