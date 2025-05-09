package com.infochim.ChemLearn.models

import java.util.ArrayList

data class Lecture(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val lessons: ArrayList<Lesson> = ArrayList()
)