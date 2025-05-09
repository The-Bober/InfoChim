package com.infochim.ChemLearn

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AnswerQuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var trueButton: RadioButton
    private lateinit var falseButton: RadioButton
    private lateinit var submitButton: Button

    private lateinit var database: DatabaseReference
    private var correctAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_quiz)

        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButton)
        submitButton = findViewById(R.id.submitButton)

        val lectureId = intent.getStringExtra("lectureId") ?: return
        val lessonId = intent.getStringExtra("lessonId") ?: return
        val quizId = intent.getStringExtra("quizId") ?: return

        database = FirebaseDatabase.getInstance()
            .getReference("lectures")
            .child(lectureId)
            .child("lessons")
            .child(lessonId)
            .child("quizzes")
            .child(quizId)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val question = snapshot.child("question").getValue(String::class.java)
                val type = snapshot.child("type").getValue(String::class.java)
                correctAnswer = snapshot.child("answer").getValue(String::class.java)

                questionText.text = question
                if (type == "multiple_choice") {
                    optionsGroup.visibility = View.VISIBLE
                    trueButton.visibility = View.GONE
                    falseButton.visibility = View.GONE

                    val options = snapshot.child("options").children.mapNotNull { it.getValue(String::class.java) }
                    for ((index, option) in options.withIndex()) {
                        val radioButton = optionsGroup.getChildAt(index) as RadioButton
                        radioButton.text = option
                        radioButton.visibility = View.VISIBLE
                    }
                } else if (type == "true_false") {
                    optionsGroup.visibility = View.GONE
                    trueButton.visibility = View.VISIBLE
                    falseButton.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnswerQuizActivity, "Failed to load quiz: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        submitButton.setOnClickListener {
            val selectedAnswer = when {
                trueButton.isChecked -> "true"
                falseButton.isChecked -> "false"
                else -> {
                    val selectedId = optionsGroup.checkedRadioButtonId
                    if (selectedId != -1) findViewById<RadioButton>(selectedId).text.toString() else null
                }
            }

            if (selectedAnswer == correctAnswer) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Wrong! Correct answer is $correctAnswer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}