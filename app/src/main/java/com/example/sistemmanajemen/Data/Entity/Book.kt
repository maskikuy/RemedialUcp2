package com.example.sistemmanajemen.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val bookId: Int = 0,
    val title: String,
    val categoryId: Int?, // Nullable untuk opsi "Tanpa Kategori"
    val isDeleted: Boolean = false
)