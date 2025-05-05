package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.ShoppingItem
import com.example.shoppinglistapp.data.ShoppingItemDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Interface defining data operations for shopping items
 * Abstraction layer between ViewModel and data sources
 */
interface ShoppingRepository {
    // grabs all items as a flow for reactive updates
    fun getAllItems(): Flow<List<ShoppingItem>>
    // gets item by ID
    suspend fun getItemById(id: String): ShoppingItem?
    // adds a new item
    suspend fun insertItem(item: ShoppingItem)
    // updates existing item
    suspend fun updateItem(item: ShoppingItem)
    // removes item
    suspend fun deleteItem(item: ShoppingItem)
}

/**
 * implements ShoppingRepository using Room db
 */
class ShoppingRepositoryImpl(
    // data access object, defines all CRUD ops that can be performed on data entities
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingRepository {
    override fun getAllItems(): Flow<List<ShoppingItem>> = shoppingItemDao.getAllItems()
    override suspend fun getItemById(id: String): ShoppingItem? = shoppingItemDao.getItemById(id)
    override suspend fun insertItem(item: ShoppingItem) = shoppingItemDao.insert(item)
    override suspend fun updateItem(item: ShoppingItem) = shoppingItemDao.update(item)
    override suspend fun deleteItem(item: ShoppingItem) = shoppingItemDao.delete(item)
}