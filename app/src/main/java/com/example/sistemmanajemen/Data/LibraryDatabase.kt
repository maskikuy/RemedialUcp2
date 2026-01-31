package com.example.sistemmanajemen.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sistemmanajemen.Data.LibraryDao
import com.example.sistemmanajemen.data.entity.Book
import com.example.sistemmanajemen.data.entity.BookInstance
import com.example.sistemmanajemen.data.entity.Category

@Database(entities = [Category::class, Book::class, BookInstance::class], version = 1, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}