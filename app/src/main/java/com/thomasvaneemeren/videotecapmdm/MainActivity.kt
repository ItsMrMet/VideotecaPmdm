package com.thomasvaneemeren.videotecapmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.thomasvaneemeren.videotecapmdm.navigation.AppNavGraph
import com.thomasvaneemeren.videotecapmdm.ui.theme.VideotecaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideotecaTheme {
                AppNavGraph()
            }
        }
    }
}
