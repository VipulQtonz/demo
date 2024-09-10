package com.example.programingdemo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.programingdemo.fragments.whatsapp.FragmentCalls
import com.example.programingdemo.fragments.whatsapp.FragmentCamera
import com.example.programingdemo.fragments.whatsapp.FragmentChats
import com.example.programingdemo.fragments.whatsapp.FragmentStatus

class ViewPagerAdapterForWhatsapp(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentCamera()
            1 -> FragmentChats()
            2 -> FragmentStatus()
            3 -> FragmentCalls()
            else -> FragmentChats()
        }
    }
}