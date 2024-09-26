package com.example.programingdemo.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programingdemo.R

class ImageAdapterForFirebaseStorage(private val listener: OnImageClickListener) :
    ListAdapter<String, ImageAdapterForFirebaseStorage.ImageViewHolder>(ImageDiffCallback()) {

    interface OnImageClickListener {
        fun onImageClick(imagePath: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_for_firebase_storage, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = getItem(position)
        Glide.with(holder.itemView.context)
            .load(imagePath)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            listener.onImageClick(imagePath)
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgView)
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}