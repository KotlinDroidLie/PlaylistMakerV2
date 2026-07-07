package com.practicum.playlistmaker.features.media.data.db.converters

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(time: Long?): Date? = time?.let { Date(it) }

    @TypeConverter
    fun fromList(list: List<Int>): String{
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(string: String): List<Int>{
        return string.split(",").map { it.toInt() }
    }
}