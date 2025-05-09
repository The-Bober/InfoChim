package com.infochim.ChemLearn.models

data class Quiz(
    val id: String = "",
    val questions: List<Question> = emptyList()
)

data class Question(
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = ""
)