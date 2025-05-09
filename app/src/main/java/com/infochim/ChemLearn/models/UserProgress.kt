package com.infochim.ChemLearn.models

data class UserProgress(
    val userId: String = "",
    val lectureId: String = "",
    val lessonId: String = "",
    val quizScore: Int = 0,
    val isCompleted: Boolean = false
)