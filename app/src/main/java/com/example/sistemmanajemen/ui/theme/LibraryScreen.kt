package com.example.sistemmanajemen.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sistemmanajemen.viewmodel.LibraryViewModel
import com.example.sistemmanajemen.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(viewModel: LibraryViewModel) {
    val categories by viewModel.categories.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // State Lokal untuk Dialog
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

    // Efek Samping untuk Toast Berhasil/Gagal
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            is UiState.Success -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
        if (uiState !is UiState.Idle && uiState !is UiState.Loading) {
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Sistem Manajemen Buku") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.insertDummy() }) {
                Icon(Icons.Default.Add, contentDescription = "Isi Dummy Data")
            }
        }
    ) { padding ->
        // List Kategori
        LazyColumn(contentPadding = padding) {
            if (categories.isEmpty()) {
                item {
                    Text(
                        "Belum ada data. Tekan tombol + untuk dummy data.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            items(categories) { category ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = category.name, style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = {
                            selectedCategoryId = category.id
                            showDialog = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                        }
                    }
                }
            }
        }

        // --- DIALOG OPSI DINAMIS ---
        if (showDialog && selectedCategoryId != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Jika kategori ini dihapus, bagaimana nasib bukunya?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteCategory(selectedCategoryId!!, deleteBooks = true)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Hapus Semua (Buku Ikut)")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.deleteCategory(selectedCategoryId!!, deleteBooks = false)
                            showDialog = false
                        }
                    ) {
                        Text("Hanya Kategori (Unlink)")
                    }
                }
            )
        }
    }
}