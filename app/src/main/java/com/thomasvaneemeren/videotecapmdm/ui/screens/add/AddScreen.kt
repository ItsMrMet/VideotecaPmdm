package com.thomasvaneemeren.videotecapmdm.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var genre by remember { mutableStateOf("") }
    val genres = getGenreList()
    var synopsis by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Añadir Película", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = genre,
                onValueChange = {},
                label = { Text("Género") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genres.forEach { genreOption ->
                    DropdownMenuItem(
                        text = { Text(genreOption) },
                        onClick = {
                            genre = genreOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = synopsis, onValueChange = { synopsis = it }, label = { Text("Sinopsis") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duración (min)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = director, onValueChange = { director = it }, label = { Text("Director") })
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Marcar como favorita")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Button(
                onClick = {
                    val dur = duration.toIntOrNull() ?: 0
                    viewModel.saveMovie(
                        title = title.trim(),
                        genre = genre.uppercase(),
                        synopsis = synopsis.trim(),
                        duration = dur,
                        director = director.trim(),
                        isFavorite = isFavorite
                    )
                    navController.popBackStack()
                },
                enabled = title.isNotBlank() && genre.isNotBlank()
            ) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = {
                navController.popBackStack()
            }) {
                Text("Cancelar")
            }
        }
    }
}
