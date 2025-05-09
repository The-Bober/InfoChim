package com.infochim.ChemLearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.infochim.ChemLearn.R;
class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val startLearningButton = findViewById<Button>(R.id.startLearningButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        // Navigate to LessonsActivity when "Start Learning" is clicked
        startLearningButton.setOnClickListener {
            val intent = Intent(this, LectureActivity::class.java)
            startActivity(intent)
        }

        // Navigate to SettingsActivity when "Settings" is clicked
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}