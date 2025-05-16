package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val userName by viewModel.userName.collectAsState()
    val movies by viewModel.movies.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(searchQuery.text) {
        viewModel.filterMovies(searchQuery.text)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Videoteca", style = MaterialTheme.typography.h6)
                        if (userName != null) {
                            Text("Usuario: $userName", style = MaterialTheme.typography.caption)
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("main")
                                    expanded = false
                                },
                                text = { Text("Principal") }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("add")
                                    expanded = false
                                },
                                text = { Text("Añadir") }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    viewModel.logout()
                                    navController.navigate("onboarding")
                                    expanded = false
                                },
                                text = { Text("Cerrar sesión") }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("author")
                                    expanded = false
                                },
                                text = { Text("Autor") }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar película...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            AnimatedVisibility(
                visible = movies.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(movies) { movie ->
                        MovieItem(movie = movie, onClick = {
                            navController.navigate("detail/${movie.id}")
                        })
                    }
                }
            }

            if (movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron películas")
                }
            }
        }
    }
}


