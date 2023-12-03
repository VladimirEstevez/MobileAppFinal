package com.example.bookshelf.data.db.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "books")

data class BookEntity (
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Float,
    val volume: Float

)

