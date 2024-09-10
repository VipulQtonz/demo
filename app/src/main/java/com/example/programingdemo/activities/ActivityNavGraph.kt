package com.example.programingdemo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityNavGraphBinding

@Suppress("DEPRECATION")
class ActivityNavGraph : AppCompatActivity() {
    private lateinit var binding: ActivityNavGraphBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityNavGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clNavGraphActivityMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentNavGraph) as? NavHostFragment
        navController = navHostFragment!!.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("This method has been deprecated in favor of using the\n{@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\nThe OnBackPressedDispatcher controls how back button events are dispatched\nto one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val startDestinationId = navController.graph.startDestinationId
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == startDestinationId) {
            super.onBackPressed()
        } else {
            val navOptions = NavOptions.Builder().setPopUpTo(
                    startDestinationId, true
                ).setLaunchSingleTop(true).build()
            navController.navigate(startDestinationId, null, navOptions)
        }
    }

    fun navigateToFragmentGreen() {
        val startDestinationId = navController.graph.startDestinationId
        val navOptions = NavOptions.Builder().setPopUpTo(
                startDestinationId, true
            ).setLaunchSingleTop(true).build()

        navController.navigate(R.id.fragmentGreen, null, navOptions)
    }
}