package com.example.sistemmanajemen.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sistemmanajemen.Data.Entity.Book
import com.example.sistemmanajemen.Data.Entity.BookInstance
import com.example.sistemmanajemen.Data.Entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM categories WHERE isDeleted = 0")
    fun getAllCategories(): Flow<List<Category>>


    @Query("""
        SELECT COUNT(*) FROM book_instances 
        INNER JOIN books ON book_instances.bookId = books.bookId 
        WHERE books.categoryId = :categoryId 
        AND book_instances.status = 'Borrowed' 
        AND book_instances.isDeleted = 0
    """)
    suspend fun countBorrowedBooksInCategory(categoryId: Int): Int


    @Query("UPDATE categories SET isDeleted = 1 WHERE id = :categoryId")
    suspend fun softDeleteCategory(categoryId: Int)


    @Query("UPDATE books SET isDeleted = 1 WHERE categoryId = :categoryId")
    suspend fun softDeleteBooksByCategory(categoryId: Int)


    @Query("UPDATE books SET categoryId = NULL WHERE categoryId = :categoryId")
    suspend fun unlinkBooksFromCategory(categoryId: Int)


    @Insert suspend fun insertCategory(category: Category)
    @Insert suspend fun insertBook(book: Book): Long
    @Insert suspend fun insertInstance(instance: BookInstance)
}