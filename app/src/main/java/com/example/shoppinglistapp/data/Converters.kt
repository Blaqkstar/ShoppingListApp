package com.example.shoppinglistapp.data

import androidx.room.TypeConverter
import java.util.Date

/**
 * Converts complex types that Room doesn't natively support
 * These methods are apparently automatically used by Room when storing/retrieving Dates
 */
class Converters {
    //converts timestamp (long) to Date when reading from db
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    // converts date to timestamp (long) when writing to db
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}