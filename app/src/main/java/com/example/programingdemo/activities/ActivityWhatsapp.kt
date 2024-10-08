package com.example.programingdemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.programingdemo.R
import com.example.programingdemo.adapters.RecyclerViewForWhatAppChat
import com.example.programingdemo.adapters.ViewPagerAdapterForWhatsapp
import com.example.programingdemo.data.ChatItem
import com.example.programingdemo.databinding.ActivityWhatsappBinding
import com.example.programingdemo.fragments.whatsapp.activity.ActivityWhatsAppChatDetails
import com.example.programingdemo.utlis.GeneralUsage.DataProvider.getChatItems
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ActivityWhatsapp : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var menuIcon: ImageView
    private lateinit var binding: ActivityWhatsappBinding
    private lateinit var chatAdapter: RecyclerViewForWhatAppChat
    private lateinit var chatItems: List<ChatItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWhatsappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityWhatsappMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
//        chatItems = getChatItems()
//        chatAdapter = RecyclerViewForWhatAppChat(chatItems,this@ActivityWhatsapp)

    }

    private fun init() {
        val imgSearch = binding.imgSearch
        val searchView = binding.searchView

//        imgSearch.setOnClickListener {
//            if (searchView.isVisible) {
//                searchView.visibility = View.GONE
//            } else {
//                searchView.visibility = View.VISIBLE
//                searchView.requestFocus()
//            }
//        }
//        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                searchView.visibility = View.GONE
//            }
//        }
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                filterChatItems(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterChatItems(newText)
//                return true
//            }
//        })


        tabLayout = binding.tlMain
        viewPager = binding.vpMain
        menuIcon = binding.imgMenu
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }

        val adapter = ViewPagerAdapterForWhatsapp(this)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(1, false)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.camera_filled_svg)
                1 -> tab.text = getString(R.string.chats)
                2 -> tab.text = getString(R.string.status)
                3 -> tab.text = getString(R.string.calls)
            }
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateFabIcon(position)
            }
        })
        updateFabIcon(viewPager.currentItem)
    }

//    private fun filterChatItems(query: String?) {
//        if (query.isNullOrBlank()) {
//            chatAdapter.updateList(chatItems)
//        } else {
//            val filteredList = chatItems.filter {
//                it.displayName.contains(query, ignoreCase = true) ||
//                        it.message.contains(query, ignoreCase = true)
//            }
//            chatAdapter.updateList(filteredList)
//        }
//    }

    private fun showPopupMenu(it: View?) {
        val popupMenu = PopupMenu(this, it)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.whatsapp_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menuNewGroup -> {
                    true
                }

                R.id.menuNewBroadcast -> {
                    true
                }

                R.id.menuWhatsappWeb -> {
                    true
                }

                R.id.menuStarredMessages -> {
                    true
                }

                R.id.menuSettings -> {
                    true
                }

                else -> false
            }
        }
        popupMenu.show()

    }

    private fun updateFabIcon(position: Int) {
        when (position) {
            1 -> setFabIcon(R.drawable.message_square_linessvg)
            2 -> setFabIcon(R.drawable.camera_filled_svg)
            3 -> setFabIcon(R.drawable.call_svg_one)
            else -> binding.fabIcon.hide()
        }
    }

    override fun onBackPressed() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        if (searchView.isVisible) {
            searchView.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    private fun setFabIcon(icon: Int) {
        binding.fabIcon.setImageResource(icon)
        binding.fabIcon.show()
    }
}
