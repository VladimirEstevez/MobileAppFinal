package com.example.bookshelf.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.data.db.entities.BookEntity


@Dao
interface BookDao {


    @Query("SELECT * FROM books")
    fun getAll(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: BookEntity)

    @Delete
    fun delete(book: BookEntity)

    @Query("SELECT * FROM books WHERE id = :id")
fun getBookById(id: String): BookEntity
}