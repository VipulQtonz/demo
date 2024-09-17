package com.example.programingdemo.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FolderPagerAdapter(
    activity: FragmentActivity,
    private val folderList: List<Pair<String, String>>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun createFragment(position: Int): Fragment {
        val folderPath = folderList[position].second
        return ImageFolderFragment.newInstance(folderPath)
    }
}



