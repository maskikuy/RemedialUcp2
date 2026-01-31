package com.example.sistemmanajemen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemmanajemen.Data.Entity.Category
import com.example.sistemmanajemen.data.LibraryDatabase
import com.example.sistemmanajemen.data.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = LibraryRepository(LibraryDatabase.getDatabase(application))


    val categories: StateFlow<List<Category>> = repo.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Fungsi Hapus Kategori
    fun deleteCategory(categoryId: Int, deleteBooks: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { // Asynchronous
            _uiState.value = UiState.Loading
            try {
                repo.deleteCategorySmart(categoryId, deleteBooks)
                _uiState.value = UiState.Success("Berhasil menghapus kategori!")
            } catch (e: Exception) {
                // Menangkap error Rollback jika ada buku dipinjam
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan sistem")
            }
        }
    }

    fun insertDummy() {
        viewModelScope.launch(Dispatchers.IO) { repo.seedDummyData() }
    }

    fun resetState() { _uiState.value = UiState.Idle }
}