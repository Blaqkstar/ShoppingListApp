package com.example.shoppinglistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for shopping items that defines database operations
 */
@Dao
interface ShoppingItemDao {
    // inserts a new item into db
    @Insert
    suspend fun insert(item: ShoppingItem)
    // updates existing item
    @Update
    suspend fun update(item: ShoppingItem)
    // deletes item from db
    @Delete
    suspend fun delete(item: ShoppingItem)
    // grabs all items, sorted by creation date (newest first)
    @Query("select * from shopping_items order by createdAt desc")
    fun getAllItems(): Flow<List<ShoppingItem>>
    // gets a specific item by ID
    @Query("select * from shopping_items where id = :id")
    suspend fun getItemById(id: String): ShoppingItem?
}