package com.thomasvaneemeren.videotecapmdm.ui.screens.edit

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import com.thomasvaneemeren.videotecapmdm.navigation.Screen
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.launch
import com.thomasvaneemeren.videotecapmdm.ui.components.FormFields
import com.thomasvaneemeren.videotecapmdm.ui.components.FormButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    movieId: Int,
    navController: NavHostController,
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel(),
    addEditViewModel: AddEditViewModel = hiltViewModel()
) {
    val movie by addEditViewModel.movie.collectAsState()
    val userName by userPreferencesViewModel.userName.collectAsState(initial = "")
    val scope = rememberCoroutineScope()

    var title by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var expandedGenre by rememberSaveable { mutableStateOf(false) }
    var synopsis by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var director by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }

    val genres = getGenreList()

    LaunchedEffect(movieId) {
        addEditViewModel.loadMovie(movieId)
        isAdmin = addEditViewModel.isAdmin()
    }

    LaunchedEffect(movie) {
        movie?.let {
            title = it.title
            genre = it.genre.replaceFirstChar { c -> c.uppercase() }
            synopsis = it.synopsis
            duration = it.duration.toString()
            director = it.director
            addEditViewModel.isFavorite(movieId).collect { favorite ->
                isFavorite = favorite
            }
        }
    }

    val isFormValid = title.isNotBlank() && genre.isNotBlank() &&
            synopsis.isNotBlank() &&
            duration.isNotBlank() && duration.toIntOrNull() != null &&
            director.isNotBlank()

    // Interaction sources para animar botones
    val interactionSourceSave = remember { MutableInteractionSource() }
    val interactionSourceCancel = remember { MutableInteractionSource() }
    val interactionSourceDelete = remember { MutableInteractionSource() }

    val isPressedSave by interactionSourceSave.collectIsFocusedAsState()
    val scaleSave by animateFloatAsState(
        targetValue = if (isPressedSave) 0.95f else 1f,
        animationSpec = tween(100),
        label = "buttonScaleSave"
    )

    val isPressedCancel by interactionSourceCancel.collectIsFocusedAsState()
    val scaleCancel by animateFloatAsState(
        targetValue = if (isPressedCancel) 0.95f else 1f,
        animationSpec = tween(100),
        label = "buttonScaleCancel"
    )

    val isPressedDelete by interactionSourceDelete.collectIsFocusedAsState()
    val scaleDelete by animateFloatAsState(
        targetValue = if (isPressedDelete) 0.95f else 1f,
        animationSpec = tween(100),
        label = "buttonScaleDelete"
    )

    ScaffoldLayout(
        userName = userName,
        navController = navController,
        currentRoute = Screen.Edit.passId(movieId),
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (movie == null) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .widthIn(max = 600.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Editar Película",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    FormFields(
                        title = title,
                        onTitleChange = { title = it },
                        genre = genre,
                        onGenreChange = { genre = it },
                        expandedGenre = expandedGenre,
                        onExpandedChange = { expandedGenre = it },
                        genres = genres,
                        synopsis = synopsis,
                        onSynopsisChange = { synopsis = it },
                        duration = duration,
                        onDurationChange = { duration = it },
                        director = director,
                        onDirectorChange = { director = it },
                        isFavorite = isFavorite,
                        onFavoriteChange = { isFavorite = it }
                    )

                    FormButtons(
                        isFormValid = isFormValid,
                        onSave = {
                            scope.launch {
                                val durationInt = duration.toIntOrNull() ?: movie?.duration ?: 0
                                addEditViewModel.saveMovie(
                                    title = title.trim(),
                                    genre = genre.uppercase(),
                                    synopsis = synopsis.trim(),
                                    duration = durationInt,
                                    director = director.trim(),
                                    isFavorite = isFavorite
                                )
                                navController.popBackStack()
                            }
                        },
                        onCancel = { navController.popBackStack() },
                        modifierSave = Modifier.graphicsLayer(scaleX = scaleSave, scaleY = scaleSave),
                        modifierCancel = Modifier.graphicsLayer(scaleX = scaleCancel, scaleY = scaleCancel),
                        interactionSourceSave = interactionSourceSave,
                        interactionSourceCancel = interactionSourceCancel
                    )

                    if (isAdmin) {
                        Button(
                            onClick = {
                                scope.launch {
                                    addEditViewModel.deleteMovie {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.graphicsLayer(scaleX = scaleDelete, scaleY = scaleDelete),
                            interactionSource = interactionSourceDelete
                        ) {
                            Text("Eliminar", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}




