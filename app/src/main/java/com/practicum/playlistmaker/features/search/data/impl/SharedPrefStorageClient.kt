package com.practicum.playlistmaker.features.search.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import java.lang.reflect.Type

class SharedPrefStorageClient<T>(
    private val dataKey: String,
    private val type: Type,
    private val gson: Gson,
    private val sharedPref: SharedPreferences
) : StorageClient<T> {

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