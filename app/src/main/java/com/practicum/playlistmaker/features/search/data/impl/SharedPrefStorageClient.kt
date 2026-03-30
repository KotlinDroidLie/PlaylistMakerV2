package com.practicum.playlistmaker.features.search.data.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import java.lang.reflect.Type

class SharedPrefStorageClient<T>(
    context: Context,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("PLAYLIST_MAKER", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun storeData(data: T) {
        val json = gson.toJson(data, type)
        sharedPref.edit {
            putString(dataKey, json)
        }
    }

    override fun getData(): T? {
        val json = sharedPref.getString(dataKey, null) ?: return null
        return gson.fromJson(json, type)
    }

}