package com.infochim.ChemLearn

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.jvm.java

class QuizListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var quizListView: ListView
    private val quizTitles = mutableListOf<String>()
    private val quizIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)

        quizListView = findViewById(R.id.quizListView)
        val lectureId = intent.getStringExtra("lectureId") ?: return
        val lessonId = intent.getStringExtra("lessonId") ?: return

        database = FirebaseDatabase.getInstance()
            .getReference("lectures")
            .child(lectureId)
            .child("lessons")
            .child(lessonId)
            .child("quizzes")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, quizTitles)
        quizListView.adapter = adapter

        // Load quizzes from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                quizTitles.clear()
                quizIds.clear()

                for (quizSnapshot in snapshot.children) {
                    val quizId = quizSnapshot.key
                    val question = quizSnapshot.child("question").getValue(String::class.java)

                    if (quizId != null && question != null) {
                        quizIds.add(quizId)
                        quizTitles.add(question)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizListActivity, "Failed to load quizzes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle quiz selection
        quizListView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuizId = quizIds[position]
            val intent = Intent(this, AnswerQuizActivity::class.java)
            intent.putExtra("lectureId", lectureId)
            intent.putExtra("lessonId", lessonId)
            intent.putExtra("quizId", selectedQuizId)
            startActivity(intent)
        }
    }
}