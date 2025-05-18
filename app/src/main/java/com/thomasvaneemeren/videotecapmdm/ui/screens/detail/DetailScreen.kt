package com.thomasvaneemeren.videotecapmdm.ui.screens.detail

import androidx.compose.foundation.layout.*
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
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.DetailViewModel
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val movie by viewModel.getMovieById(movieId).collectAsState(initial = null)
    val userName by userPreferencesViewModel.userName.collectAsState(initial = "")

    movie?.let {
        ScaffoldLayout(
            userName = userName ?: "",
            navController = navController,
            currentRoute = "detail/$movieId"
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Row {
                        IconButton(onClick = {
                            viewModel.deleteMovie(it)
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }
                        IconButton(onClick = {
                            navController.navigate("edit/${it.id}")
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                DetailText(label = "Género", value = it.genre)
                DetailText(label = "Director", value = it.director)
                DetailText(label = "Duración", value = "${it.duration} min")
                DetailText(label = "Favorita", value = if (it.isFavorite) "Sí" else "No")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Sinopsis", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(it.synopsis, style = MaterialTheme.typography.bodyMedium)
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
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label:", style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
