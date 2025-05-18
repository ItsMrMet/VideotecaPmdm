package com.thomasvaneemeren.videotecapmdm.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldLayout(
    userName: String,
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Videoteca") },
                actions = {
                    Column {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Principal") },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Main.route) {
                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Añadir") },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Add.route) {
                                        navController.navigate(Screen.Add.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Onboarding.route) {
                                        navController.navigate(Screen.Onboarding.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Autor") },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Author.route) {
                                        navController.navigate(Screen.Author.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        content = content
    )
}
