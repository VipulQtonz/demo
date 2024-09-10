package com.example.programingdemo.fragments.whatsapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.adapters.RecyclerViewForWhatAppChat
import com.example.programingdemo.data.ChatItem
import com.example.programingdemo.databinding.FragmentChatsBinding
import com.example.programingdemo.fragments.whatsapp.activity.ActivityWhatsAppChatDetails
import com.example.programingdemo.utlis.Const.CHAT_ITEM
import com.example.programingdemo.utlis.GeneralUsage


class FragmentChats : Fragment(), RecyclerViewForWhatAppChat.OnChatItemClickListener {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecyclerViewForWhatAppChat
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val callLogItems = GeneralUsage.DataProvider.getChatItems()
        recyclerView = binding.rwWhatsAppChat
        adapter = RecyclerViewForWhatAppChat(callLogItems, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onChatItemClick(chatItem: ChatItem) {
        val intent = Intent(requireContext(), ActivityWhatsAppChatDetails::class.java)
        intent.putExtra(CHAT_ITEM, chatItem)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}