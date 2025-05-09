package com.infochim.ChemLearn

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LessonDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var lessonContentText: TextView
    private lateinit var lessonChemicalReactionText: TextView
    private lateinit var lessonVideoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)

        lessonContentText = findViewById(R.id.lessonContentText)
        lessonChemicalReactionText = findViewById(R.id.lessonChemicalReactionText)
        lessonVideoText = findViewById(R.id.lessonVideoText)

        val lectureId = intent.getStringExtra("lectureId") ?: return
        val lessonId = intent.getStringExtra("lessonId") ?: return

        database = FirebaseDatabase.getInstance().getReference("lectures").child(lectureId).child("lessons").child(lessonId)

        // Fetch lesson details
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val text = snapshot.child("text").getValue(String::class.java)
                val chemicalReaction = snapshot.child("chemicalReaction").getValue(String::class.java)
                val videoUrl = snapshot.child("videoUrl").getValue(String::class.java)

                lessonContentText.text = text ?: "No content available."
                lessonChemicalReactionText.text = chemicalReaction ?: "No chemical reaction available."
                lessonVideoText.text = videoUrl ?: "No video available."
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LessonDetailActivity, "Failed to load lesson details: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}