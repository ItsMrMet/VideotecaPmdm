package com.thomasvaneemeren.videotecapmdm.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun AddScreen(
    onSave: (title: String, genre: String, synopsis: String, duration: Int, director: String, isFavorite: Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var genre by remember { mutableStateOf(TextFieldValue("")) }
    var synopsis by remember { mutableStateOf(TextFieldValue("")) }
    var duration by remember { mutableStateOf(TextFieldValue("")) }
    var director by remember { mutableStateOf(TextFieldValue("")) }
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
        TextField(value = genre, onValueChange = { genre = it }, label = { Text("Género") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = synopsis, onValueChange = { synopsis = it }, label = { Text("Sinopsis") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duración (min)") },
            singleLine = true,
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = director, onValueChange = { director = it }, label = { Text("Director") })
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Marcar como favorita")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Button(onClick = {
                val dur = duration.text.toIntOrNull() ?: 0
                onSave(title.text.trim(), genre.text.trim(), synopsis.text.trim(), dur, director.text.trim(), isFavorite)
            }, enabled = title.text.isNotBlank() && genre.text.isNotBlank()) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}
