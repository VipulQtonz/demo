package com.example.programingdemo.realTimeDatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R

class FirestoreDatabaseAdapter(
    private var items: List<UserProfileInfo>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FirestoreDatabaseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.tvFirstName)
        val lastName: TextView = itemView.findViewById(R.id.tvMiddleName)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val dateOfBirth: TextView = itemView.findViewById(R.id.tvDateOfBirth)
        val address: TextView = itemView.findViewById(R.id.tvAddress)
        val delete: Button = itemView.findViewById(R.id.btnDelete)
        val update: Button = itemView.findViewById(R.id.btnUpdate)
    }

    private fun anim(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1000
        view.startAnimation(animation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        anim(holder.itemView)
        val address = item.address.city + " , " + item.address.state + " " + item.address.pinCode
        holder.firstName.text = item.name.firstName
        holder.lastName.text = item.name.middleName
        holder.email.text = item.email
        holder.dateOfBirth.text = item.dateOfBirth
        holder.address.text = address

        holder.delete.setOnClickListener {
            itemClickListener.onDeleteClick(item)
        }

        holder.update.setOnClickListener {
            itemClickListener.onUpdateClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<UserProfileInfo>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onDeleteClick(item: UserProfileInfo)
        fun onUpdateClick(item: UserProfileInfo)
    }
}


