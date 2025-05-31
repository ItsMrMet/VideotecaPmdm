package com.thomasvaneemeren.videotecapmdm.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldLayout(
    userName: String?,
    navController: NavHostController,
    currentRoute: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val navigationIcon: @Composable (() -> Unit) = if (showBackButton && onBackClick != null) {
        {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    } else {
        {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Videoteca") },
                navigationIcon = navigationIcon,
                actions = {
                    Column {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            Text(
                                text = "Usuario: ${userName ?: "Cargando..."}",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Principal", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Main.route) {
                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Añadir", color = MaterialTheme.colorScheme.onSurface) },
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
                                text = { Text("Cerrar sesión", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    menuExpanded = false
                                    if (currentRoute != Screen.Onboarding.route) {
                                        navController.navigate(Screen.Onboarding.route) {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Autor", color = MaterialTheme.colorScheme.onSurface) },
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
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = content
    )
}