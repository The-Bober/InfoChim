package com.infochim.ChemLearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infochim.ChemLearn.models.Lecture

class LecturesAdapter(
    private val lectures: List<Lecture>,
    private val onClick: (Lecture) -> Unit
) : RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.bind(lecture, onClick)
    }

    override fun getItemCount(): Int = lectures.size

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(lecture: Lecture, onClick: (Lecture) -> Unit) {
            titleTextView.text = lecture.title
            itemView.setOnClickListener { onClick(lecture) }
        }
    }
}