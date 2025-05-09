package com.infochim.ChemLearn

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var quizTypeDropdown: Spinner
    private lateinit var quizQuestionInput: EditText
    private lateinit var quizOptionsInput: EditText
    private lateinit var quizAnswerInput: EditText
    private lateinit var createQuizButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        quizTypeDropdown = findViewById(R.id.quizTypeDropdown)
        quizQuestionInput = findViewById(R.id.quizQuestionInput)
        quizOptionsInput = findViewById(R.id.quizOptionsInput)
        quizAnswerInput = findViewById(R.id.quizAnswerInput)
        createQuizButton = findViewById(R.id.addQuizButton)

        val lectureId = intent.getStringExtra("lectureId") ?: return
        val lessonId = intent.getStringExtra("lessonId") ?: return
        val database = FirebaseDatabase.getInstance()
            .getReference("lectures")
            .child(lectureId)
            .child("lessons")
            .child(lessonId)
            .child("quizzes")

        // Setup quiz type dropdown
        val quizTypes = listOf("Multiple Choice", "True/False")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quizTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quizTypeDropdown.adapter = adapter

        quizTypeDropdown.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                quizOptionsInput.visibility = if (position == 0) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        // Handle quiz creation
        createQuizButton.setOnClickListener {
            val quizType = quizTypeDropdown.selectedItem.toString()
            val question = quizQuestionInput.text.toString().trim()
            val options = quizOptionsInput.text.toString().trim().split(",").map { it.trim() }
            val answer = quizAnswerInput.text.toString().trim()

            if (question.isEmpty() || answer.isEmpty() || (quizType == "Multiple Choice" && options.isEmpty())) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quizId = database.push().key ?: return@setOnClickListener
            val quizData = mutableMapOf<String, Any>(
                "type" to if (quizType == "Multiple Choice") "multiple_choice" else "true_false",
                "question" to question,
                "answer" to answer
            )

            if (quizType == "Multiple Choice") {
                quizData["options"] = options
            }

            database.child(quizId).setValue(quizData).addOnSuccessListener {
                Toast.makeText(this, "Quiz created successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to create quiz: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}