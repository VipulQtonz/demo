package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.data.Category

class CategoryAdapterForParent(private val categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapterForParent.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.tvCategoryName)
        val itemsRecyclerView: RecyclerView = view.findViewById(R.id.rvItem)
        val hideShow: ImageView = view.findViewById(R.id.imgShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_layout_for_parent, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.categoryName

        holder.itemsRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemsRecyclerView.context, RecyclerView.HORIZONTAL, false)
        holder.itemsRecyclerView.adapter = ItemAdapterForChild(category.items)

        if (category.isExpanded) {
            holder.itemsRecyclerView.visibility = View.VISIBLE
            holder.hideShow.setImageResource(R.drawable.arrow_drop_up)
        } else {
            holder.itemsRecyclerView.visibility = View.GONE
            holder.hideShow.setImageResource(R.drawable.arrow_drop_down)
        }

        holder.hideShow.setOnClickListener {
            category.isExpanded = !category.isExpanded

            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}
