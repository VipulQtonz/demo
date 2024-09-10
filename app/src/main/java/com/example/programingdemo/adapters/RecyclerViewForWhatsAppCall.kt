package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programingdemo.R
import com.example.programingdemo.data.CallLogItem
import com.example.programingdemo.databinding.SingleItemForCallBinding

class RecyclerViewForWhatsAppCall(
    private val items: List<CallLogItem>,
) : RecyclerView.Adapter<RecyclerViewForWhatsAppCall.ViewHolder>() {

    inner class ViewHolder(private val binding: SingleItemForCallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CallLogItem) {
            Glide.with(itemView.context)
                .load(item.profileImageResource)
                .into(binding.imgImageDp)
            binding.tvDisplayName.text = item.displayName
            binding.tvCallDetails.text = item.callDetails
            if (item.callTypeActionIconResource == R.drawable.arrow_left_down_svg) {
                binding.imgCallTypeAction.setColorFilter(
                    itemView.context.getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.imgCallTypeAction.setColorFilter(
                    itemView.context.getColor(R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            binding.imgCallTypeAction.setImageResource(item.callTypeActionIconResource)
            binding.imgCallType.setImageResource(item.callTypeIconResource)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleItemForCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
