package com.thomasvaneemeren.videotecapmdm.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    movieId: Int,
    initialTitle: String,
    initialGenre: String,
    initialSynopsis: String,
    initialDuration: Int,
    initialDirector: String,
    initialIsFavorite: Boolean,
    onSave: (
        title: String,
        genre: String,
        synopsis: String,
        duration: Int,
        director: String,
        isFavorite: Boolean
    ) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var genre by remember { mutableStateOf(initialGenre) }
    var synopsis by remember { mutableStateOf(initialSynopsis) }
    var duration by remember { mutableStateOf(initialDuration.toString()) }
    var director by remember { mutableStateOf(initialDirector) }
    var isFavorite by remember { mutableStateOf(initialIsFavorite) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Película") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = synopsis,
                onValueChange = { synopsis = it },
                label = { Text("Sinopsis") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it.filter { c -> c.isDigit() } },
                label = { Text("Duración (min)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = director,
                onValueChange = { director = it },
                label = { Text("Director") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Favorita?")
                Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val dur = duration.toIntOrNull() ?: 0
                    onSave(title, genre, synopsis, dur, director, isFavorite)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }

            TextButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cancelar")
            }
        }
    }
}
