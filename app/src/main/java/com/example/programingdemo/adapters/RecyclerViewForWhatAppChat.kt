package com.example.programingdemo.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.programingdemo.R
import com.example.programingdemo.data.ChatItem
import com.example.programingdemo.databinding.SingleItemForChatBinding
import com.example.programingdemo.utlis.Const.MESSAGE_COUNT_PLUS

class RecyclerViewForWhatAppChat(
    private var items: List<ChatItem>,
    private val listener: OnChatItemClickListener
) : RecyclerView.Adapter<RecyclerViewForWhatAppChat.ViewHolder>() {

    inner class ViewHolder(private val binding: SingleItemForChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {
            Glide.with(itemView.context)
                .load(item.profileImageResId)
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .into(binding.imgImageDp)
            binding.tvDisplayName.text = item.displayName
            binding.tvCallDetails.text = item.message
            binding.tvMessageTime.text = item.messageTime
            if (item.messageCount > 100) {
                binding.tvMessageCount.text = MESSAGE_COUNT_PLUS
                binding.tvMessageCount.visibility = View.VISIBLE
                binding.imgReadReciepe.visibility = View.VISIBLE
            } else if (item.messageCount == 0) {
                binding.tvMessageCount.visibility = View.GONE
                binding.imgReadReciepe.visibility = View.VISIBLE

                Glide.with(itemView.context)
                    .load(item.messageReadRecepe)
                    .apply(RequestOptions().fitCenter())
                    .into(binding.imgReadReciepe)

                binding.imgReadReciepe.setColorFilter(
                    ContextCompat.getColor(itemView.context, R.color.redMessage),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.tvMessageCount.text = item.messageCount.toString()
                binding.tvMessageCount.visibility = View.VISIBLE
                binding.imgReadReciepe.visibility = View.GONE
            }
        }
    }

    interface OnChatItemClickListener {
        fun onChatItemClick(chatItem: ChatItem)
    }

//    fun updateList(newList: List<ChatItem>) {
//        items = newList
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleItemForChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.invalidate()
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            listener.onChatItemClick(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}
