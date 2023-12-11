package com.example.bookshelf.data.db.entities
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
//@Entity(
//    tableName = "book_order_cross_reference",
//    primaryKeys = ["bookId", "orderId"],
//    foreignKeys = [
//        ForeignKey(
//            entity = BookEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["bookId"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = OrderEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["orderId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class BookOrderCrossRef(
//    val bookId: String,
//    val orderId: String
//)