package com.example.shoppinglistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID


/**
 * defines data structure for shopping items stored in the db
 * Each field corresponds to a column in the 'shopping_items' table
 *
 * @property id Unique identifier for each item
 * @property name Name of the product/item
 * @property price Price per unit of the item
 * @property quantity Number of units to purchase
 * @property department Category/department the item belongs to
 * @property sku Stock keeping unit identifier
 * @property bestByDate Optional expiration date for the item
 * @property createdAt Timestamp when the item was added to the list
 */
@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val price: Double,
    val quantity: Int,
    val department: String,
    val sku: String,
    val bestByDate: Date? = null, // should be supported via converter
    val createdAt: Long = System.currentTimeMillis()
)