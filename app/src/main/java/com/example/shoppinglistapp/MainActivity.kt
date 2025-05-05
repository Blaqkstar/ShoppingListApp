package com.example.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.shoppinglistapp.data.ShoppingDatabase
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import com.example.shoppinglistapp.data.repository.ShoppingRepositoryImpl
import com.example.shoppinglistapp.ui.screens.ShoppingListScreen
import com.example.shoppinglistapp.ui.viewmodel.ShoppingViewModel

/**
 * Main activity sets up application and inits dependencies
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // inits db prior to launching main app screen
        val db = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            "shopping-db"
        ).build()
        // creates repo and viewmodel
        val repository = ShoppingRepositoryImpl(db.shoppingItemDao())
        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        )[ShoppingViewModel::class.java]

        // compose UI setup
        setContent {
            ShoppingListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListScreen(viewModel = viewModel) // calls the first screen
                }
            }
        }
    }
}

/**
 * Factory creating ViewModels with custom constructor params
 */
class ViewModelFactory(
    private val repository: ShoppingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

