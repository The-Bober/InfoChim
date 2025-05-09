package com.infochim.ChemLearn

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class LessonActivity : AppCompatActivity() {

    private lateinit var lectureDropdown: Spinner
    private lateinit var lessonTextInput: EditText
    private lateinit var lessonDescriptionInput: EditText
    private lateinit var youtubeUrlInput: EditText
    private lateinit var chemicalReactionInput: EditText
    private lateinit var addLessonButton: Button
    private val database = FirebaseDatabase.getInstance().getReference("lectures")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        // Initialize Views
        lectureDropdown = findViewById(R.id.lectureDropdown)
        lessonTextInput = findViewById(R.id.lessonTextInput)
        lessonDescriptionInput = findViewById(R.id.lessonDescriptionInput)
        youtubeUrlInput = findViewById(R.id.youtubeUrlInput)
        chemicalReactionInput = findViewById(R.id.chemicalReactionInput)
        addLessonButton = findViewById(R.id.addLessonButton)

        // Add Lesson Button Logic
        addLessonButton.setOnClickListener {
            val lectureId = getSelectedLectureId()
            val lessonText = lessonTextInput.text.toString().trim()
            val lessonDescription = lessonDescriptionInput.text.toString().trim()
            val youtubeUrl = youtubeUrlInput.text.toString().trim()
            val chemicalReaction = chemicalReactionInput.text.toString().trim()

            if (lectureId == null || lessonText.isEmpty() || lessonDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val lessonId = database.child(lectureId).child("lessons").push().key
            if (lessonId == null) {
                Toast.makeText(this, "Failed to generate lesson ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val lesson = mapOf(
                "text" to lessonText,
                "description" to lessonDescription,
                "videoUrl" to youtubeUrl,
                "chemicalReaction" to chemicalReaction
            )

            database.child(lectureId).child("lessons").child(lessonId).setValue(lesson)
                .addOnSuccessListener {
                    Toast.makeText(this, "Lesson added successfully!", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add lesson: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getSelectedLectureId(): String? {
        // Fetch the selected lecture's ID from the dropdown (Spinner)
        // You should populate the dropdown dynamically with lecture data from Firebase
        // For now, we'll assume a placeholder implementation
        return "someLectureId" // Replace with actual logic
    }

    private fun clearFields() {
        lessonTextInput.text.clear()
        lessonDescriptionInput.text.clear()
        youtubeUrlInput.text.clear()
        chemicalReactionInput.text.clear()
    }
}