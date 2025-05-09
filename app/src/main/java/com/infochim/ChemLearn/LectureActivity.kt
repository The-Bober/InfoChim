package com.infochim.ChemLearn

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class LectureActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var addLectureButton: Button
    private val database = FirebaseDatabase.getInstance().getReference("lectures")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)

        titleInput = findViewById(R.id.lectureTitleInput)
        descriptionInput = findViewById(R.id.lectureDescriptionInput)
        addLectureButton = findViewById(R.id.addLectureButton)

        addLectureButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val lectureId = database.push().key ?: return@setOnClickListener
            val lecture = mapOf("title" to title, "description" to description)

            database.child(lectureId).setValue(lecture).addOnSuccessListener {
                Toast.makeText(this, "Lecture added successfully!", Toast.LENGTH_SHORT).show()
                titleInput.text.clear()
                descriptionInput.text.clear()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to add lecture: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}