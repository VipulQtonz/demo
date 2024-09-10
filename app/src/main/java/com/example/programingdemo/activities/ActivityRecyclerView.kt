package com.example.programingdemo.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.adapters.RecyclerViewAdapter
import com.example.programingdemo.data.Item
import com.example.programingdemo.databinding.ActivityRecyclerViewBinding

class ActivityRecyclerView : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    private lateinit var binding: ActivityRecyclerViewBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter

    private val itemList = listOf(
        Item(R.drawable.user_profile, "Apple", "A tasty red fruit"),
        Item(R.drawable.user_profile, "Banana", "A yellow fruit rich in potassium"),
        Item(R.drawable.user_profile, "Cherry", "A small, round, and red fruit"),
        Item(R.drawable.user_profile, "Dragon Fruit", "An exotic fruit with a unique appearance"),
        Item(R.drawable.user_profile, "Elderberry", "A berry often used in syrups and jams"),
        Item(R.drawable.user_profile, "Fig", "A fruit with a unique texture and sweetness"),
        Item(R.drawable.user_profile, "Grapes", "Small, round, and juicy fruits"),
        Item(R.drawable.user_profile, "Honeydew", "A sweet and green melon"),
        Item(R.drawable.user_profile, "Kiwi", "A small fruit with a brown skin and green flesh"),
        Item(R.drawable.user_profile, "Lemon", "A yellow citrus fruit with a tart flavor"),
        Item(R.drawable.user_profile, "Mango", "A tropical fruit with sweet and juicy flesh"),
        Item(R.drawable.user_profile, "Nectarine", "A smooth-skinned variety of peach"),
        Item(R.drawable.user_profile, "Orange", "A citrus fruit rich in vitamin C"),
        Item(R.drawable.user_profile, "Papaya", "A tropical fruit with a sweet taste"),
        Item(R.drawable.user_profile, "Quince", "A fruit related to apples and pears"),
        Item(R.drawable.user_profile, "Raspberry", "A small, red, and tart berry")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityRecyclerViewMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(itemList, this@ActivityRecyclerView)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(item: Item) {
        showAlertDialog(item)
    }

    private fun showAlertDialog(item: Item) {
        val customView = LayoutInflater.from(this).inflate(R.layout.single_item, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(customView)

        val imageView = customView.findViewById<ImageView>(R.id.imgImage)
        val titleTextView = customView.findViewById<TextView>(R.id.tvTitle)
        val descriptionTextView = customView.findViewById<TextView>(R.id.tvContentDescription)

        imageView.setImageResource(item.imageResource)
        titleTextView.text = item.text
        descriptionTextView.text = item.disc

        val alertDialog = builder.create()
        alertDialog.show()
    }
}