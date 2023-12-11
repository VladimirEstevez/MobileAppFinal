package com.example.bookshelf.data.db.entities
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "orders")

data class OrderEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerEmail: String,
    val pickupDate: Long,
    val beerIds: String,
    val totalPrice: Double,
    val orderStatus: String,
)

