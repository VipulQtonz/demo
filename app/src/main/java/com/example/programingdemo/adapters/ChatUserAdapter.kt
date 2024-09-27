package com.example.programingdemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.data.UserData

class ChatUserAdapter(
    private var userList: List<UserData>,
    private val listener: OnUserClickListener
) :
    RecyclerView.Adapter<ChatUserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val userEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.userName
        holder.userEmail.text = user.email
        holder.itemView.setOnClickListener {
            listener.onUserClick(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    interface OnUserClickListener {
        fun onUserClick(user: UserData)
    }

    fun updateUsers(newUsers: List<UserData>) {
        userList = newUsers
        notifyDataSetChanged()
    }

}
