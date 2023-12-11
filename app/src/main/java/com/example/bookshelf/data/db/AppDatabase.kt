package com.example.bookshelf.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.bookshelf.data.db.dao.BookDao
import com.example.bookshelf.data.db.dao.OrderDao
import com.example.bookshelf.data.db.entities.BookEntity
import com.example.bookshelf.data.db.entities.OrderEntity

@Database(entities = [BookEntity::class, OrderEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun orderDao(): OrderDao
}