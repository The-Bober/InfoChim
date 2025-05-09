package com.infochim.ChemLearn

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LectureListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var lectureListView: ListView
    private val lectureTitles = mutableListOf<String>()
    private val lectureIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_list)

        lectureListView = findViewById(R.id.lectureListView)
        database = FirebaseDatabase.getInstance().getReference("lectures")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lectureTitles)
        lectureListView.adapter = adapter

        // Fetch lectures from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lectureTitles.clear()
                lectureIds.clear()

                for (lectureSnapshot in snapshot.children) {
                    val lectureId = lectureSnapshot.key
                    val title = lectureSnapshot.child("title").getValue(String::class.java)

                    if (lectureId != null && title != null) {
                        lectureIds.add(lectureId)
                        lectureTitles.add(title)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LectureListActivity, "Failed to load lectures: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle lecture selection
        lectureListView.setOnItemClickListener { _, _, position, _ ->
            val selectedLectureId = lectureIds[position]
            val intent = Intent(this, LessonListActivity::class.java)
            intent.putExtra("lectureId", selectedLectureId)
            startActivity(intent)
        }
    }
}