package com.practicum.playlistmaker.di.data

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.features.search.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storageModule = module {

    single<SharedPreferences>{
        androidContext()
            .getSharedPreferences("PLAYLIST_MAKER", Context.MODE_PRIVATE)
    }

    factory<Gson>{
        Gson()
    }

    single<StorageClient<MutableList<TrackHistoryDto>>>(named("history")){
        SharedPrefStorageClient<MutableList<TrackHistoryDto>>(
            dataKey = "HISTORY_KEY",
            type = object : TypeToken<MutableList<TrackHistoryDto>>() {}.type,
            gson = get<Gson>(),
            sharedPref = get<SharedPreferences>()
        )
    }

    single<StorageClient<SettingsDto>>(named("settings")){
        SharedPrefStorageClient<SettingsDto>(
            dataKey = "SETTINGS_KEY",
            type = object : TypeToken<SettingsDto>() {}.type,
            gson = get<Gson>(),
            sharedPref = get<SharedPreferences>()
        )
    }

    single<AppDataBase>{
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "track.db").build()
    }
}