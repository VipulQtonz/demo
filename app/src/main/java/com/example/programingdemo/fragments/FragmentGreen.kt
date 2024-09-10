package com.example.programingdemo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.programingdemo.R
import com.example.programingdemo.databinding.FragmentGreenBinding
import com.example.programingdemo.utlis.Const.FRAGMENT_GREEN

class FragmentGreen : Fragment(), View.OnClickListener {
    private var _binding: FragmentGreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnGotoRed.setOnClickListener(this)
        binding.btnGotoYellow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnGotoRed -> {
                findNavController().navigate(R.id.action_fragmentGreen_to_fragmentRed)
            }

            R.id.btnGotoYellow -> {
                findNavController().navigate(R.id.action_fragmentGreen_to_fragmentYellow)
            }
        }
    }

}