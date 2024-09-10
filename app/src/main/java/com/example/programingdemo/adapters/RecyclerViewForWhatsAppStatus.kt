package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programingdemo.data.StatusItem
import com.example.programingdemo.databinding.SingleItemForStatusBinding

class RecyclerViewForWhatsAppStatus(
    private val items: List<StatusItem>,
) : RecyclerView.Adapter<RecyclerViewForWhatsAppStatus.ViewHolder>() {

    inner class ViewHolder(private val binding: SingleItemForStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StatusItem) {
            Glide.with(itemView.context)
                .load(item.profileImage)
                .into(binding.imgImageDp)
            binding.tvDisplayName.text = item.displayName
            binding.tvCallDetails.text = item.uploadedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleItemForStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

