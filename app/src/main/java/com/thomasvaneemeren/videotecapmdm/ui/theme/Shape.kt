package com.thomasvaneemeren.videotecapmdm.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),   // para botones pequeños o chips
    medium = RoundedCornerShape(8.dp),  // para tarjetas, cajas, diálogos
    large = RoundedCornerShape(12.dp)   // para grandes contenedores o modales
)
