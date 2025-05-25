package ru.otus.basicarchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this) {
            if (!findNavController(R.id.main).popBackStack()) {
                finish()
            }
        }
    }
}