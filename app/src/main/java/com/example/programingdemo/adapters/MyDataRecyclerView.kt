package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.data.MyData

class MyDataRecyclerView(
    private val items: List<MyData>,
    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyDataRecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val delete: ImageView = itemView.findViewById(R.id.imgDelete)
        val update: ImageView = itemView.findViewById(R.id.imgEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.delete.setOnClickListener {
            itemClickListener.onDeleteClick(item)
        }
        holder.update.setOnClickListener {
            itemClickListener.onUpdateClick(item)
        }
    }

    interface OnItemClickListener {
        fun onDeleteClick(item: MyData)
        fun onUpdateClick(item: MyData)
    }

    override fun getItemCount(): Int = items.size
}
