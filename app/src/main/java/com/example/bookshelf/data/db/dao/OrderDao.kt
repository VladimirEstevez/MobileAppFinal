package com.example.bookshelf.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.data.db.entities.OrderEntity


@Dao
interface OrderDao {


    @Query("SELECT * FROM orders")
    fun getAll(): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: OrderEntity)

//    @Delete
//    fun delete(order: OrderEntity)

//    @Query("SELECT * FROM orders WHERE id = :id")
//    fun getBookById(id: String): OrderEntity
}