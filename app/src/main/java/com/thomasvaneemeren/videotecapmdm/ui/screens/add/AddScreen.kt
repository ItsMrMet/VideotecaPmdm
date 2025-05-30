package com.thomasvaneemeren.videotecapmdm.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    ScaffoldLayout(
        userName = userName,
        navController = navController,
        currentRoute = "add",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FormButtons(
                        isFormValid = isFormValid,
                        onSave = {
                            addEditViewModel.saveMovie(
                                title = title.trim(),
                                genre = genre.uppercase(),
                                synopsis = synopsis.trim(),
                                duration = duration.toInt(),
                                director = director.trim(),
                                isFavorite = isFavorite
                            )
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        onCancel = {
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text("Añadir Película", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
                FormButtons(
                    isFormValid = isFormValid,
                    onSave = {
                        addEditViewModel.saveMovie(
                            title = title.trim(),
                            genre = genre.uppercase(),
                            synopsis = synopsis.trim(),
                            duration = duration.toInt(),
                            director = director.trim(),
                            isFavorite = isFavorite
                        )
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    },
                    onCancel = {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFields(
    title: String, onTitleChange: (String) -> Unit,
    genre: String, onGenreChange: (String) -> Unit,
    expandedGenre: Boolean, onExpandedChange: (Boolean) -> Unit,
    genres: List<String>,
    synopsis: String, onSynopsisChange: (String) -> Unit,
    duration: String, onDurationChange: (String) -> Unit,
    director: String, onDirectorChange: (String) -> Unit,
    isFavorite: Boolean, onFavoriteChange: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text("Título") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

    ExposedDropdownMenuBox(
        expanded = expandedGenre,
        onExpandedChange = { onExpandedChange(!expandedGenre) }
    ) {
        OutlinedTextField(
            value = genre,
            onValueChange = {},
            label = { Text("Género") },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expandedGenre,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            genres.forEach { genreOption ->
                DropdownMenuItem(
                    text = { Text(genreOption) },
                    onClick = {
                        onGenreChange(genreOption)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = synopsis,
        onValueChange = onSynopsisChange,
        label = { Text("Sinopsis") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = duration,
        onValueChange = { if (it.all { c -> c.isDigit() }) onDurationChange(it) },
        label = { Text("Duración (min)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = director,
        onValueChange = onDirectorChange,
        label = { Text("Director") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isFavorite, onCheckedChange = onFavoriteChange)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Marcar como favorita")
    }
}

@Composable
fun FormButtons(isFormValid: Boolean, onSave: () -> Unit, onCancel: () -> Unit) {
    Row {
        Button(onClick = onSave, enabled = isFormValid) {
            Text("Guardar")
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(onClick = onCancel) {
            Text("Cancelar")
        }
    }
}


