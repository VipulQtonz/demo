package com.example.programingdemo.fragments.whatsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.adapters.RecyclerViewForWhatsAppCall
import com.example.programingdemo.databinding.FragmentCallsBinding
import com.example.programingdemo.utlis.GeneralUsage

class FragmentCalls : Fragment() {
    private var _binding: FragmentCallsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecyclerViewForWhatsAppCall
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val callLogItems = GeneralUsage.DataProvider.getCallLogItems()
        recyclerView = binding.rwWhatsAppCall
        adapter = RecyclerViewForWhatsAppCall(callLogItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }
}