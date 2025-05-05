package com.example.shoppinglistapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.ui.viewmodel.ShoppingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable handling the add/edit item dialog box
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemDialog(
    viewModel: ShoppingViewModel,
    onDismiss: () -> Unit
) {
    // formatting!
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    var dateText by remember { mutableStateOf(
        viewModel.itemBestByDate?.let { dateFormat.format(it) } ?: "" // default blank
    ) }

    // actual content stuff
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (viewModel.isEditing) "Edit Item" else "Add New Item")
        },
        // input fields!
        text = {
            Column {
                // item name
                TextField(
                    value = viewModel.itemName,
                    onValueChange = { viewModel.itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // quantity
                TextField(
                    value = viewModel.itemQuantity,
                    onValueChange = { viewModel.itemQuantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // price
                TextField(
                    value = viewModel.itemPrice,
                    onValueChange = { viewModel.itemPrice = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // dept
                TextField(
                    value = viewModel.itemDepartment,
                    onValueChange = { viewModel.itemDepartment = it },
                    label = { Text("Department") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // sku
                TextField(
                    value = viewModel.itemSku,
                    onValueChange = { viewModel.itemSku = it },
                    label = { Text("SKU/UPC") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // best by date
                TextField(
                    value = dateText,
                    onValueChange = { newValue ->
                        dateText = newValue
                        try {
                            viewModel.itemBestByDate = dateFormat.parse(newValue)
                        } catch (e: Exception) {
                            viewModel.itemBestByDate = null
                        }
                    },
                    label = { Text("Best By Date (MM/DD/YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        // buttons (wow!)
        confirmButton = {
            Button(
                onClick = {
                    if (viewModel.isEditing) {
                        viewModel.updateItem()
                    } else {
                        viewModel.addItem()
                    }
                    onDismiss()
                }
            ) {
                Text(if (viewModel.isEditing) "Update" else "Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}