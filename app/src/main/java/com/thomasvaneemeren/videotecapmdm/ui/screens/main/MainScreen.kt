package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.navigation.Screen
import com.thomasvaneemeren.videotecapmdm.ui.components.MovieCard
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val movies by viewModel.movies.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredMovies = if (searchQuery.isBlank()) {
        movies
    } else {
        movies.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    ScaffoldLayout(
        userName = viewModel.userName.collectAsState().value ?: "",
        navController = navController,
        currentRoute = Screen.Main.route
    ) { padding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar película") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredMovies.isEmpty()) {
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
                        items(filteredMovies.size) { index ->
                            val movie = filteredMovies[index]
                            MovieCard(
                                movie = movie,
                                onClick = {
                                    navController.navigate("detail/${movie.id}")
                                }
                            )
                        }
                    }
                }
            }

            // Floating Action Button para añadir película
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Add.route) }, // igual que menú, navegación simple
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
