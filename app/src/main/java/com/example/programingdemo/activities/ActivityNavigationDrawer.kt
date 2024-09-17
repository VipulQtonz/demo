package com.example.programingdemo.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityNavigationDrawerBinding
import com.example.programingdemo.fragments.FragmentMyBottomSheet
import com.google.android.material.navigation.NavigationView

class ActivityNavigationDrawer : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityNavigationDrawerBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        addOnClickListener()

    }

    private fun addOnClickListener() {
        binding.imgMenu.setOnClickListener(this)
        binding.imgMenuProfileAdd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgMenu -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }

            R.id.imgMenuProfileAdd -> {
                val bottomSheetFragment = FragmentMyBottomSheet()
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }

        }
    }

    private fun init() {
        drawerLayout = binding.dlMain
        navigationView = binding.nvMain
        toolbar = binding.tbMain

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcwMain) as? NavHostFragment
        navController = navHostFragment!!.navController

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemSelected(menuItem)
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, v.paddingBottom)
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.setOnApplyWindowInsetsListener(navigationView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, v.paddingBottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun handleNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragmentHome -> {
                navigateToFragment(R.id.fragmentHome)
            }

            R.id.fragmentCart -> {
                navigateToFragment(R.id.fragmentCart)
            }

            R.id.fragmentSearch -> {
                navigateToFragment(R.id.fragmentSearch)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToFragment(fragmentId: Int) {
        val navOptions =
            NavOptions.Builder().setPopUpTo(navController.graph.startDestinationId, true)
                .setLaunchSingleTop(true).build()
        navController.navigate(fragmentId, null, navOptions)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val startDestinationId = navController.graph.startDestinationId
            val currentDestinationId = navController.currentDestination?.id

            if (currentDestinationId == startDestinationId) {
                super.onBackPressed()
            } else {
                val navOptions = NavOptions.Builder().setPopUpTo(startDestinationId, true)
                    .setLaunchSingleTop(true).build()
                navController.navigate(startDestinationId, null, navOptions)
            }
        }
    }

}