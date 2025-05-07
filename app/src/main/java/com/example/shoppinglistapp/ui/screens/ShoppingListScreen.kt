package com.example.shoppinglistapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglistapp.data.ShoppingItem
import com.example.shoppinglistapp.ui.viewmodel.ShoppingViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue

/**
 * Main screen composable displaying shopping list and handling user interactions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingViewModel) {
    val items by viewModel.allItems.collectAsState() // collects state from viewmodel
    val totalCost = items.sumOf { it.price * it.quantity }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault())} // should correctly display in user's local currency
    var showAddDialog by remember { mutableStateOf(false) } // dialog is invisible by default

    // material design layout struct
    Scaffold(
        topBar = {
            TopAppBar(
                // title holds both the app name and the add button
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end=24.dp), // padding to push add button towards center
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shopping List")
                        // add button!
                        FloatingActionButton(
                            onClick = { showAddDialog = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Item")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            // floating stuff!
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // total cost
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Total: ${currencyFormat.format(totalCost)}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }


            }
        }
    ) { padding ->
        // ---------------------------------------------------------------- [ MAIN CONTENT HERE ]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // empty list
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // empty state msg
                    Text(
                        text = "There are no items in your shopping list",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            } else {
                // lists items in a LazyColumn
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        ShoppingItemCard(
                            item = item,
                            onEdit = {
                                viewModel.setupEdit(item)
                                showAddDialog = true
                            },
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }
        }
    }

    // add/edit dialog
    if (showAddDialog) {
        AddEditItemDialog(
            viewModel = viewModel,
            onDismiss = {
                showAddDialog = false
                viewModel.clearForm()
            }
        )
    }
}

/**
 * Composable displaying individual shopping item card
 */
@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onEdit: () -> Unit, // unit is like void in Java
    onDelete: () -> Unit
) {
    // checked state
    var checked by remember { mutableStateOf(false) }
    // formats price and calcs total
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val totalPrice = currencyFormat.format(item.price * item.quantity)

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // checkbox for Julissa
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it},
                modifier = Modifier.padding(end = 3.dp)
            )
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                        //.background(color = Color.Blue),
                    verticalArrangement = Arrangement.Top
                ){
                    // Row 1: item name/quantity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // item name
                        Text(
                            text = item.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Qty: ${item.quantity}",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Column{
                    // Row 2: edit/delete buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // total item price
                        Text(
                            text = "Total: $totalPrice",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier
                                .weight(.5f),
                            horizontalArrangement = Arrangement.End
                        ){
                            IconButton(onClick = onEdit) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = onDelete) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }

                    }
                }


            }
        }

    }
}
