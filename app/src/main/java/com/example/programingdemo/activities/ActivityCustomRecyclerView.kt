package com.example.programingdemo.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.adapters.HighlightAdapter
import com.example.programingdemo.utlis.CenterZoomLayoutManager

class ActivityCustomRecyclerView : AppCompatActivity() {


    private lateinit var highlightAdapter: HighlightAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_recycler_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val items = List(100) { "Item ${it + 1}" }

        recyclerView = findViewById(R.id.recyclerView)
        highlightAdapter = HighlightAdapter(items)
        recyclerView.adapter = highlightAdapter

        // Use the custom CenterZoomLayoutManager
        val layoutManager = CenterZoomLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // Handle center item change
        val handler = Handler(Looper.getMainLooper())
        layoutManager.onCenterItemChanged = { position ->
            handler.post {
                highlightAdapter.setSelectedPosition(position)
            }
        }

    }
}