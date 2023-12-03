package com.example.bookshelf.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.bookshelf.data.db.dao.BookDao
import com.example.bookshelf.data.db.entities.BookEntity

@Database(entities = [BookEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}