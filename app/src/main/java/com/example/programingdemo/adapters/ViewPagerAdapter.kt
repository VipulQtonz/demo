package com.example.programingdemo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.programingdemo.fragments.FragmentGreen
import com.example.programingdemo.fragments.FragmentRed
import com.example.programingdemo.fragments.FragmentYellow

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentGreen()
            1 -> FragmentYellow()
            2 -> FragmentRed()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
