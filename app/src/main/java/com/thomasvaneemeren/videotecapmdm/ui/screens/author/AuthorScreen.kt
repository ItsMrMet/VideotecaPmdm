package com.thomasvaneemeren.videotecapmdm.ui.screens.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.R
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    navController: NavHostController,
    userName: String = ""
) {
    ScaffoldLayout(
        userName = userName,
        navController = navController,
        currentRoute = "author"
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.author_photo_pmdm),
                contentDescription = "Foto del autor",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Thomas Van Eemeren",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Desarrollador de la Videoteca PMDM",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
