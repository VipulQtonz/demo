package com.example.programingdemo.fragments.whatsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.adapters.RecyclerViewForWhatsAppCall
import com.example.programingdemo.adapters.RecyclerViewForWhatsAppStatus
import com.example.programingdemo.databinding.FragmentCallsBinding
import com.example.programingdemo.databinding.FragmentStatusBinding
import com.example.programingdemo.utlis.GeneralUsage

class FragmentStatus : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecyclerViewForWhatsAppStatus
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val callLogItems = GeneralUsage.DataProvider.getStatusItems()
        recyclerView = binding.rwWhatsAppStatus
        adapter = RecyclerViewForWhatsAppStatus(callLogItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }
}