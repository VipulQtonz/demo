package com.example.programingdemo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.programingdemo.activities.ActivityNavGraph
import com.example.programingdemo.R
import com.example.programingdemo.databinding.FragmentRedBinding
import com.example.programingdemo.utlis.Const.FRAGMENT_GREEN

class FragmentRed : Fragment(), View.OnClickListener {
    private var _binding: FragmentRedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(FRAGMENT_GREEN, getString(R.string.oncreateview_called))
        _binding = FragmentRedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnClickListener()
        Log.d(FRAGMENT_GREEN, getString(R.string.onviewcreated_called))
    }

    private fun addOnClickListener() {
        binding.btnGotoGreen.setOnClickListener(this)
        binding.btnGotoYellow.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(FRAGMENT_GREEN, getString(R.string.ondestroyview_called))
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnGotoGreen -> {
                (requireActivity() as ActivityNavGraph).navigateToFragmentGreen()
//                findNavController().navigate(R.id.action_fragmentRed_to_fragmentGreen)
            }

            R.id.btnGotoYellow -> {
                findNavController().navigate(R.id.action_fragmentRed_to_fragmentYellow)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(FRAGMENT_GREEN, getString(R.string.onstart_called))
    }

    override fun onResume() {
        super.onResume()
        Log.d(FRAGMENT_GREEN, getString(R.string.onresume_called))
    }

    override fun onPause() {
        super.onPause()
        Log.d(FRAGMENT_GREEN, getString(R.string.onpause_called))
    }

    override fun onStop() {
        super.onStop()
        Log.d(FRAGMENT_GREEN, getString(R.string.onstop_called))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(FRAGMENT_GREEN, getString(R.string.ondestroy_called))
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(FRAGMENT_GREEN, getString(R.string.ondetach_called))
    }
}