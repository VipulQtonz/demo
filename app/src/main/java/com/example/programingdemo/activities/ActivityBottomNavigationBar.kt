package com.example.programingdemo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityBottomNavigationBarBinding

class ActivityBottomNavigationBar : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavigationBarBinding
    private lateinit var navController: NavController
    private var isNavigationInProgress = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBottomNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        bottomNavigationBarItemOnClickListener()
        bottomMenuChanger()
    }

    private fun bottomMenuChanger() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!isNavigationInProgress) {
                isNavigationInProgress = true
                binding.bottomNavigation.selectedItemId = when (destination.id) {
                    R.id.fragmentHome -> R.id.imgHome
                    R.id.fragmentSearch -> R.id.imgSearch
                    R.id.fragmentCart -> R.id.imgCart
                    else -> R.id.imgHome
                }
                isNavigationInProgress = false
            }
        }
    }

    private fun bottomNavigationBarItemOnClickListener() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.imgHome -> {
                    navController.navigate(
                        R.id.fragmentHome, null, NavOptions.Builder()
                            .setPopUpTo(navController.graph.startDestinationId, true)
                            .build()
                    )
                    true
                }

                R.id.imgSearch -> {
                    navController.navigate(R.id.fragmentSearch, null, navOptions)
                    true
                }

                R.id.imgCart -> {
                    navController.navigate(R.id.fragmentCart, null, navOptions)
                    true
                }
                else -> false
            }
        }
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }
}