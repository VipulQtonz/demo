package com.example.programingdemo.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programingdemo.R

class ImagePagerAdapter(
    private var imageList: MutableList<String>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_view, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imageList[position]
        Glide.with(holder.itemView.context)
            .load(imagePath)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onImageClick(position)
        }
    }

    override fun getItemCount(): Int = imageList.size

    fun removeImage(position: Int) {
        imageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, imageList.size)
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgViewPagerImage)
    }
}
