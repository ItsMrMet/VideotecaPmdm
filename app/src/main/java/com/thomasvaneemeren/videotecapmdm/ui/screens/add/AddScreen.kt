package com.thomasvaneemeren.videotecapmdm.ui.screens.add

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import com.thomasvaneemeren.videotecapmdm.navigation.Screen
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel
import com.thomasvaneemeren.videotecapmdm.ui.components.FormFields
import com.thomasvaneemeren.videotecapmdm.ui.components.FormButtons
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    addEditViewModel: AddEditViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val userName by userPreferencesViewModel.userName.collectAsState(initial = "")
    var title by rememberSaveable { mutableStateOf("") }
    var expandedGenre by rememberSaveable { mutableStateOf(false) }
    var genre by rememberSaveable { mutableStateOf("") }
    val genres = getGenreList()
    var synopsis by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var director by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() &&
            genre.isNotBlank() &&
            synopsis.isNotBlank() &&
            duration.isNotBlank() &&
            duration.toIntOrNull() != null &&
            director.isNotBlank()

    val scope = rememberCoroutineScope()

    ScaffoldLayout(
        userName = userName,
        navController = navController,
        currentRoute = Screen.Add.route,
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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título animado
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(700)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(700)
                    )
                ) {
                    Text(
                        text = "Añadir Película",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Card con campos y animación de aparición
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                        initialOffsetY = { it / 3 },
                        animationSpec = tween(800)
                    )
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // FormFields con animación para dropdown de género
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
                        }
                    }
                }

                // Botones con animación y efecto de escala al pulsar
                val interactionSourceSave = remember { MutableInteractionSource() }
                val isPressedSave by interactionSourceSave.collectIsFocusedAsState()
                val scaleSave by animateFloatAsState(
                    targetValue = if (isPressedSave) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "buttonScaleSave"
                )

                val interactionSourceCancel = remember { MutableInteractionSource() }
                val isPressedCancel by interactionSourceCancel.collectIsFocusedAsState()
                val scaleCancel by animateFloatAsState(
                    targetValue = if (isPressedCancel) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "buttonScaleCancel"
                )

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(900)) + slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(900)
                    )
                ) {
                    FormButtons(
                        isFormValid = isFormValid,
                        onSave = {
                            scope.launch {
                                addEditViewModel.saveMovie(
                                    title = title.trim(),
                                    genre = genre.uppercase(),
                                    synopsis = synopsis.trim(),
                                    duration = duration.toInt(),
                                    director = director.trim(),
                                    isFavorite = isFavorite
                                )
                                navController.navigate(Screen.Main.route) {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        },
                        onCancel = {
                            navController.navigate(Screen.Main.route) {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        modifierSave = Modifier
                            .scale(scaleSave)
                            .then(Modifier),
                        modifierCancel = Modifier
                            .scale(scaleCancel)
                            .then(Modifier),
                        interactionSourceSave = interactionSourceSave,
                        interactionSourceCancel = interactionSourceCancel
                    )
                }
            }
        }
    }
}
