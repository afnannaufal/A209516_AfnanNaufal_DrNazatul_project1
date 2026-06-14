package com.example.a209516_afnannaufal_drnazatul_project1

import androidx.compose.ui.graphics.Color
import java.util.UUID

// Add the 'color' property to your data class
data class SubjectRecord(
    val name: String,
    val credits: Int,
    val grade: String,
    val lecturer: String = "Dr. Nazatul", // Default value
    val color: Color = Color(0xFF2196F3) // Add this default color
)
data class StudentProfile(
    val name: String = "Afnan Naufal",
    val studentId: String = "A209516",
    val faculty: String = "FTSM" // Added to match Lab 4 UI requirements
)