package com.infochim.ChemLearn.models

import com.infochim.ChemLearn.models.Quiz

data class Lesson(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val quiz: Quiz? = null
)