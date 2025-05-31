package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.navigation.Screen
import com.thomasvaneemeren.videotecapmdm.ui.components.MovieCard
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.MainViewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.35f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()

                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.setSearchQuery(it)
                            },
                            label = { Text("Buscar película") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            interactionSource = interactionSource,
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = if (isPressed) MaterialTheme.colorScheme.primary else Color.Gray,
                                unfocusedIndicatorColor = Color.LightGray
                            )
                        )
                        IconToggleButton(
                            checked = favoritesOnly,
                            onCheckedChange = { viewModel.toggleFavoritesOnly() }
                        ) {
                            Icon(
                                imageVector = if (favoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Mostrar solo favoritos",
                                tint = if (favoritesOnly) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(0.65f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(movies, key = { _, movie -> movie.id }) { _, movie ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f)

                            Box(modifier = Modifier
                                .animateContentSize()
                                .scale(scale)
                            ) {
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

            } else {
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
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()

                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.setSearchQuery(it)
                            },
                            label = { Text("Buscar película") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            interactionSource = interactionSource,
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = if (isPressed) MaterialTheme.colorScheme.primary else Color.Gray,
                                unfocusedIndicatorColor = Color.LightGray
                            )
                        )

                        IconToggleButton(
                            checked = favoritesOnly,
                            onCheckedChange = { viewModel.toggleFavoritesOnly() }
                        ) {
                            Icon(
                                imageVector = if (favoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Mostrar solo favoritos",
                                tint = if (favoritesOnly) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    AnimatedVisibility(visible = movies.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No se encontraron películas.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(movies, key = { _, movie -> movie.id }) { _, movie ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f)

                            Box(modifier = Modifier
                                .animateContentSize()
                                .scale(scale)
                            ) {
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
            }

            val fabScale by animateFloatAsState(targetValue = 1f)
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Add.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .scale(fabScale)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir película")
            }
        }
    }
}
