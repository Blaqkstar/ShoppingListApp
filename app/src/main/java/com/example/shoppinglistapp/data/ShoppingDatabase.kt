package com.example.shoppinglistapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room database class. Serves as main access point to the persistent data
 * @see Converters for handling types like Date
 */
@Database(
    entities = [ShoppingItem::class], // tables
    version = 1, // db ver
    exportSchema = false // schema export disabled
)
@TypeConverters(Converters::class) // custom type converters. Neat!
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}