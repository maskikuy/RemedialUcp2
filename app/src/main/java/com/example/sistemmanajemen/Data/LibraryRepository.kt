package com.example.sistemmanajemen.data

import androidx.room.withTransaction
import com.example.sistemmanajemen.Data.Entity.Book
import com.example.sistemmanajemen.Data.Entity.BookInstance
import com.example.sistemmanajemen.Data.Entity.Category

import kotlinx.coroutines.flow.Flow

class LibraryRepository(private val db: LibraryDatabase) {

    private val dao = db.libraryDao()

    val allCategories: Flow<List<Category>> = dao.getAllCategories()

    // Logika Transaksi & Rollback Otomatis
    suspend fun deleteCategorySmart(categoryId: Int, deleteBooks: Boolean) {
        db.withTransaction {

            val borrowedCount = dao.countBorrowedBooksInCategory(categoryId)
            if (borrowedCount > 0) {

                throw IllegalStateException("GAGAL: Ada $borrowedCount buku sedang dipinjam! Operasi dibatalkan.")
            }

            dao.softDeleteCategory(categoryId)

            if (deleteBooks) {
                dao.softDeleteBooksByCategory(categoryId) // Hapus buku
            } else {
                dao.unlinkBooksFromCategory(categoryId) // Set jadi "Tanpa Kategori"
            }
        }
    }


    suspend fun seedDummyData() {
        val catSains = Category(name = "Sains")
        dao.insertCategory(catSains)
        val catFiksi = Category(name = "Fiksi")
        dao.insertCategory(catFiksi)


        val bookId = dao.insertBook(Book(title = "Fisika Dasar", categoryId = 1))
        dao.insertInstance(
            BookInstance(
                bookId = bookId.toInt(),
                uniqueCode = "FIS-001",
                status = "Borrowed"
            )
        ) // Buku ini akan memblokir hapus
    }
}