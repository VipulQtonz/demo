package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ItemUserBinding
import com.example.programingdemo.room.User

class RoomAdapter(private val listener: OnChatItemClickListener) :
    RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private var dataList: List<User> = emptyList()

    class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.bind(user)

        holder.itemView.findViewById<ImageView>(R.id.imgEdit).setOnClickListener {
            listener.onUpdateClick(user)
        }

        holder.itemView.findViewById<ImageView>(R.id.imgDelete).setOnClickListener {
            listener.onDeleteClick(user)
        }
    }

    interface OnChatItemClickListener {
        fun onUpdateClick(user: User)
        fun onDeleteClick(user: User)
    }

    fun submitList(it: List<User>) {
        dataList = it
        notifyDataSetChanged()
    }

    fun getAllData(user: List<User>) {
        dataList = user
        notifyDataSetChanged()
    }

}