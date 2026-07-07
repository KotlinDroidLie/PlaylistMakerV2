package com.practicum.playlistmaker.features.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationOwner {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }

    override fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    override fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }
}