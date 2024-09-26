package com.example.programingdemo.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.data.UserMessage

class ChatAdapter(
    private val messageList: List<UserMessage>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_RIGHT = 1
    private val VIEW_TYPE_LEFT = 2

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.userId == currentUserId) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_right, parent, false)
            RightMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_left, parent, false)
            LeftMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is RightMessageViewHolder) {
            holder.bind(message)
        } else {
            (holder as LeftMessageViewHolder).bind(message)
        }
    }

    override fun getItemCount() = messageList.size

    inner class RightMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: UserMessage) {
            itemView.findViewById<TextView>(R.id.tvRight).text = message.message
        }
    }

    inner class LeftMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: UserMessage) {
            itemView.findViewById<TextView>(R.id.tvLeft).text = message.message
            itemView.findViewById<TextView>(R.id.tvUserNameLeft).text = message.userName
        }
    }
}
