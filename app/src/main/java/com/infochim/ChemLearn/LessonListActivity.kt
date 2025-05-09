package com.infochim.ChemLearn

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.jvm.java

class LessonListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var lessonListView: ListView
    private val lessonTitles = mutableListOf<String>()
    private val lessonIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_list)

        lessonListView = findViewById(R.id.lessonListView)
        val lectureId = intent.getStringExtra("lectureId") ?: return

        database = FirebaseDatabase.getInstance().getReference("lectures").child(lectureId).child("lessons")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lessonTitles)
        lessonListView.adapter = adapter

        // Fetch lessons from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lessonTitles.clear()
                lessonIds.clear()

                for (lessonSnapshot in snapshot.children) {
                    val lessonId = lessonSnapshot.key
                    val title = lessonSnapshot.child("description").getValue(String::class.java) // Use description as the title

                    if (lessonId != null && title != null) {
                        lessonIds.add(lessonId)
                        lessonTitles.add(title)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LessonListActivity, "Failed to load lessons: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle lesson selection
        lessonListView.setOnItemClickListener { _, _, position, _ ->
            val selectedLessonId = lessonIds[position]
            val intent = Intent(this, LessonDetailActivity::class.java)
            intent.putExtra("lectureId", lectureId)
            intent.putExtra("lessonId", selectedLessonId)
            startActivity(intent)
        }
    }
}