package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.data.ItemOne

class ItemAdapterForChild(private val itemList: List<ItemOne>) :
    RecyclerView.Adapter<ItemAdapterForChild.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_for_child, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemName.text = itemList[position].itemName
        holder.itemName.setOnClickListener {
            Toast.makeText(holder.itemName.context, itemList[position].itemName, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun getItemCount(): Int = itemList.size
}
