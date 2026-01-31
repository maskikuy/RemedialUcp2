package com.example.sistemmanajemen.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_instances")
data class BookInstance(
    @PrimaryKey(autoGenerate = true) val instanceId: Int = 0,
    val bookId: Int,
    val uniqueCode: String, // ID Unik
    val status: String, // "Available" atau "Borrowed"
    val isDeleted: Boolean = false
)