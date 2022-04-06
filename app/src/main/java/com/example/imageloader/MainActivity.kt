package com.example.imageloader

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.widget.Toast

import com.example.imageloader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("imageloader", Context.MODE_PRIVATE)

        Utils.loadSavedImage(this, binding.imageView)

        val blur = prefs.getBoolean("blur", false)
        binding.blurSwitch.setChecked(blur)
        binding.blurSwitch.setOnCheckedChangeListener { _, isChecked ->
            with(prefs.edit()) {
                putBoolean("blur", isChecked)
                apply()
            }
        }
        val greyscale = prefs.getBoolean("greyscale", false)
        binding.greyscaleSwitch.setChecked(greyscale)
        binding.greyscaleSwitch.setOnCheckedChangeListener { _, isChecked ->
            with(prefs.edit()) {
                putBoolean("greyscale", isChecked)
                apply()
            }
        }
        binding.button.setOnClickListener {
            Utils.loadNewImage(this, binding.imageView, binding.progressBar)
        }
    }
}