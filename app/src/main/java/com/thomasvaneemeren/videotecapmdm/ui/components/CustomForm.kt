package com.thomasvaneemeren.videotecapmdm.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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
        label = { Text("Título", style = MaterialTheme.typography.labelSmall) },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )

    Spacer(modifier = Modifier.height(5.dp))

    ExposedDropdownMenuBox(
        expanded = expandedGenre,
        onExpandedChange = { onExpandedChange(!expandedGenre) }
    ) {
        OutlinedTextField(
            value = genre,
            onValueChange = {},
            label = { Text("Género", style = MaterialTheme.typography.labelSmall) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = MaterialTheme.shapes.medium
        )
        ExposedDropdownMenu(
            expanded = expandedGenre,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            genres.forEach { genreOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            genreOption,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onGenreChange(genreOption)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = synopsis,
        onValueChange = onSynopsisChange,
        label = { Text("Sinopsis", style = MaterialTheme.typography.labelSmall) },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )

    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = duration,
        onValueChange = { if (it.all { c -> c.isDigit() }) onDurationChange(it) },
        label = { Text("Duración (min)", style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )

    Spacer(modifier = Modifier.height(5.dp))

    OutlinedTextField(
        value = director,
        onValueChange = onDirectorChange,
        label = { Text("Director", style = MaterialTheme.typography.labelSmall) },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    )

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = isFavorite,
            onCheckedChange = onFavoriteChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            "Marcar como favorita",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FormButtons(
    isFormValid: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifierSave: Modifier = Modifier,
    modifierCancel: Modifier = Modifier,
    interactionSourceSave: MutableInteractionSource = remember { MutableInteractionSource() },
    interactionSourceCancel: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSave,
            enabled = isFormValid,
            modifier = modifierSave.weight(1f),
            shape = MaterialTheme.shapes.medium,
            interactionSource = interactionSourceSave
        ) {
            Text(
                "Guardar",
                style = MaterialTheme.typography.labelLarge
            )
        }
        OutlinedButton(
            onClick = onCancel,
            modifier = modifierCancel.weight(1f),
            shape = MaterialTheme.shapes.medium,
            interactionSource = interactionSourceCancel
        ) {
            Text(
                "Cancelar",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}