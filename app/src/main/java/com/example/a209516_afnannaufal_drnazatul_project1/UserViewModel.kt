package com.example.a209516_afnannaufal_drnazatul_project1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    // FIXED: Replaced TODO() with actual state variables to prevent NotImplementedError during rendering
    var isEditing by mutableStateOf(false)
    var draftColor by mutableStateOf<Color?>(null)

    var profile by mutableStateOf(StudentProfile())
    var searchQuery by mutableStateOf("")

    // Shared State for Subject Records
    val subjects = mutableStateListOf<SubjectRecord>()

    // Filter logic for the Search Bar
    val filteredSubjects: List<SubjectRecord>
        get() = if (searchQuery.isEmpty()) subjects
        else subjects.filter { it.name.contains(searchQuery, ignoreCase = true) }

    // GPA Calculation Logic
    val totalGPA: Double
        get() {
            if (subjects.isEmpty()) return 0.0
            var totalPoints = 0.0
            var totalCredits = 0
            for (sub in subjects) {
                val gradeValue = when(sub.grade.uppercase()) {
                    "A" -> 4.0; "A-" -> 3.67; "B+" -> 3.33; "B" -> 3.0
                    "B-" -> 2.67; "C+" -> 2.33; "C" -> 2.0; else -> 0.0
                }
                totalPoints += (gradeValue * sub.credits)
                totalCredits += sub.credits
            }
            return if (totalCredits == 0) 0.0 else totalPoints / totalCredits
        }

    // Form States
    var draftName by mutableStateOf("")
    var draftCredits by mutableStateOf("")
    var draftGrade by mutableStateOf("")

    // Update the function signature to accept a color
    fun addSubject(selectedColor: Color) {
        val newSubject = SubjectRecord(
            name = draftName,
            credits = draftCredits.toIntOrNull() ?: 0,
            grade = draftGrade,
            color = selectedColor // Pass the selected color here
        )
        subjects.add(newSubject)
        // Clear drafts after saving
        draftName = ""
        draftCredits = ""
        draftGrade = ""
    }

    // Timer States
    var isTimerRunning by mutableStateOf(false)
    var timeLeft by mutableStateOf(1500)

    fun updateName(newName: String) { profile = profile.copy(name = newName) }
    fun logout() { profile = StudentProfile(name = "") }
    fun clearDrafts() {
        // Implementation for clearing drafts if needed
        draftName = ""
        draftCredits = ""
        draftGrade = ""
        draftColor = null
        isEditing = false
    }

    fun startEditing(subject: SubjectRecord) {
        isEditing = true
        draftName = subject.name
        draftCredits = subject.credits.toString()
        draftGrade = subject.grade
        draftColor = subject.color
    }
}