package com.example.shoppinglistapp.ui.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import com.example.shoppinglistapp.data.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * ViewModel managing the UI-related data and business logic
 * Exposes state to the UI and handles user interactions
 */
class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {
    // state for list of items (auto-updates when db changes)
    val allItems = repository.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    // form state variables
    var itemName by mutableStateOf("")
    var itemPrice by mutableStateOf("")
    var itemQuantity by mutableStateOf("1") // default 1
    var itemDepartment by mutableStateOf("")
    var itemSku by mutableStateOf("")
    var itemBestByDate by mutableStateOf<Date?>(null)

    // editing state
    var isEditing by mutableStateOf(false)
    var currentItemId by mutableStateOf<String?>(null)


    /**
     * Validates input and adds new item to db
     * Runs in a coroutine to avoid blocking UI thread
     */
    fun addItem() {
        viewModelScope.launch {
            if (itemName.isBlank()) return@launch

            val price = itemPrice.toDoubleOrNull() ?: 0.0 // default 0.0
            val quantity = itemQuantity.toIntOrNull() ?: 1 // default 1

            val item = ShoppingItem(
                name = itemName,
                price = price,
                quantity = quantity,
                department = itemDepartment,
                sku = itemSku,
                bestByDate = itemBestByDate
            )
            // insert happens here
            repository.insertItem(item)
            clearForm()
        }
    }


    /**
     * Updates existing item
     * Runs in a coroutine to avoid blocking UI thread
     */
    fun updateItem() {
        viewModelScope.launch {
            if (itemName.isBlank() || itemPrice.isBlank()) return@launch

            val price = itemPrice.toDoubleOrNull() ?: 0.0 // default 0.0
            val quantity = itemQuantity.toIntOrNull() ?: 1 // default 1

            val item = ShoppingItem(
                id = currentItemId!!,
                name = itemName,
                price = price,
                quantity = quantity,
                department = itemDepartment,
                sku = itemSku,
                bestByDate = itemBestByDate
            )
            // update happens here
            repository.updateItem(item)
            clearForm()
        }
    }

    /**
     * Deletes item from db
     * Runs in a coroutine to avoid blocking UI thread
     */
    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteItem(item) // boop
        }
    }

    fun setupEdit(item: ShoppingItem) {
        currentItemId = item.id
        itemName = item.name
        itemPrice = item.price.toString()
        itemQuantity = item.quantity.toString()
        itemDepartment = item.department
        itemSku = item.sku
        itemBestByDate = item.bestByDate
        isEditing = true
    }

    fun clearForm() {
        itemName = ""
        itemPrice = ""
        itemQuantity = "1"
        itemDepartment = ""
        itemSku = ""
        itemBestByDate = null
        isEditing = false
        currentItemId = null
    }
}