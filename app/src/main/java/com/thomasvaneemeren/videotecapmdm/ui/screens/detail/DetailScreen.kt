package com.thomasvaneemeren.videotecapmdm.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.ui.navigation.Screen
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
            currentRoute = Screen.Detail.passId(movieId)
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .widthIn(max = 600.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(500))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row {
                                if (isAdmin) {
                                    IconButton(onClick = {
                                        viewModel.deleteMovie(movie)
                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(Screen.Main.route) { inclusive = true }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar película",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                IconButton(onClick = {
                                    navController.navigate(Screen.Edit.passId(movie.id))
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Editar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(600))
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .graphicsLayer(scaleX = 1.05f, scaleY = 1.05f),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(700))
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailText(label = "Género", value = movie.genre)
                                DetailText(label = "Director", value = movie.director)
                                DetailText(label = "Duración", value = "${movie.duration} min")
                                DetailText(label = "Favorita", value = if (isFavorite) "Sí" else "No")
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(800))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Sinopsis",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                movie.synopsis,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun DetailText(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}