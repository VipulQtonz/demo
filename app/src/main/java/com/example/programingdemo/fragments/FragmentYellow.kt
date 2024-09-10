package com.example.programingdemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.programingdemo.activities.ActivityNavGraph
import com.example.programingdemo.R
import com.example.programingdemo.databinding.FragmentYellowBinding

class FragmentYellow : Fragment(), View.OnClickListener {

    private var _binding: FragmentYellowBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYellowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnGotoRed.setOnClickListener(this)
        binding.btnGotoGreen.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnGotoGreen -> {
                (requireActivity() as ActivityNavGraph).navigateToFragmentGreen()
//                findNavController().navigate(R.id.action_fragmentYellow_to_fragmentGreen2)
            }

            R.id.btnGotoRed -> {
                findNavController().navigate(R.id.action_fragmentYellow_to_fragmentRed)
            }
        }
    }
}