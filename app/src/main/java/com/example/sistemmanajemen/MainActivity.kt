package com.example.sistemmanajemen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.sistemmanajemen.ui.LibraryScreen
import com.example.sistemmanajemen.viewmodel.LibraryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewModel
        val viewModel: LibraryViewModel by viewModels()

        setContent {
            MaterialTheme {
                // Panggil UI Screen Utama
                LibraryScreen(viewModel)
            }
        }
    }
}