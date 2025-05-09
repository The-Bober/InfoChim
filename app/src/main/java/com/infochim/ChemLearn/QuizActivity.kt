package com.infochim.ChemLearn

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class QuizActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var questionText: TextView
    private lateinit var quizImage: ImageView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var trueButton: RadioButton
    private lateinit var falseButton: RadioButton
    private lateinit var submitButton: Button
    private lateinit var nextButton: Button

    private val quizList = mutableListOf<Map<String, Any>>()
    private var currentQuizIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        // Initialize Views
        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButto                            n)
        submitButton = findViewById(R.id.submitButton)

        val lectureId = intent.getStringExtra("lectureId") ?: return
        val lessonId = intent.getStringExtra("lessonId") ?: return
        database = FirebaseDatabase.getInstance().getReference("lectures")
            .child(lectureId).child("lessons").child(lessonId).child("quizzes")

        // Load quizzes from Firebase
        loadQuizzes()

        // Submit Button Listener
        submitButton.setOnClickListener {
            checkAnswer()
        }

        // Next Button Listener
        nextButton.setOnClickListener {
            loadNextQuiz()
        }
    }

    private fun loadQuizzes() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                quizList.clear()
                for (quizSnapshot in snapshot.children) {
                    val quizData = quizSnapshot.value as? Map<String, Any>
                    if (quizData != null) {
                        quizList.add(quizData)
                    }
                }

                if (quizList.isNotEmpty()) {
                    displayQuiz(quizList[currentQuizIndex])
                } else {
                    Toast.makeText(this@QuizActivity, "No quizzes available", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, "Failed to load quizzes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayQuiz(quiz: Map<String, Any>) {
        questionText.text = quiz["question"] as? String

        // Load quiz image if available
        val imageUrl = quiz["imageUrl"] as? String
        if (!imageUrl.isNullOrEmpty()) {
            quizImage.visibility = View.VISIBLE
            Picasso.get().load(imageUrl).into(quizImage)
        } else {
            quizImage.visibility = View.GONE
        }

        val type = quiz["type"] as? String
        if (type == "multiple_choice") {
            optionsGroup.visibility = View.VISIBLE
            trueButton.visibility = View.GONE
            falseButton.visibility = View.GONE

            val options = quiz["options"] as? List<String>
            if (options != null) {
                for ((index, option) in options.withIndex()) {
                    val radioButton = optionsGroup.getChildAt(index) as RadioButton
                    radioButton.text = option
                    radioButton.visibility = View.VISIBLE
                }
                for (i in options.size until optionsGroup.childCount) {
                    optionsGroup.getChildAt(i).visibility = View.GONE
                }
            }
        } else if (type == "true_false") {
            optionsGroup.visibility = View.GONE
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
        }
    }

    private fun checkAnswer() {
        val quiz = quizList[currentQuizIndex]
        val type = quiz["type"] as? String
        val correctAnswer = quiz["answer"] as? String

        val userAnswer = when (type) {
            "multiple_choice" -> {
                val selectedId = optionsGroup.checkedRadioButtonId
                if (selectedId != -1) {
                    val selectedRadioButton = findViewById<RadioButton>(selectedId)
                    selectedRadioButton.text.toString()
                } else {
                    null
                }
            }
            "true_false" -> {
                if (trueButton.isChecked) "true" else if (falseButton.isChecked) "false" else null
            }
            else -> null
        }

        if (userAnswer == correctAnswer) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong! Correct answer is $correctAnswer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNextQuiz() {
        currentQuizIndex++
        if (currentQuizIndex < quizList.size) {
            displayQuiz(quizList[currentQuizIndex])
        } else {
            Toast.makeText(this, "You have completed all quizzes!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}