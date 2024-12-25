package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R

class HighlightAdapter(private val items: List<String>) :
    RecyclerView.Adapter<HighlightAdapter.ViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)  // Enables stable IDs
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()  // Use the position as the stable ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedItemPosition)
    }

    override fun getItemCount(): Int = items.size

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.itemText)

        fun bind(text: String, isSelected: Boolean) {
            textView.text = text
            textView.alpha = if (isSelected) 1.0f else 0.5f
        }
    }
}
