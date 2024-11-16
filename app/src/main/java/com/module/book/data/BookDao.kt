package com.module.book.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.module.book.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>

    @Insert
    suspend fun insertBook(book: Book)
} 