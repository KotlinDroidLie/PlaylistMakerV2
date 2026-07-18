package com.practicum.playlistmaker.di.data

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.features.media.data.api.IFileStorageClient
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.features.media.data.db.dao.TrackDao
import com.practicum.playlistmaker.features.media.data.db.dao.TracksInPlaylistsDao
import com.practicum.playlistmaker.features.media.data.impl.FileStorageClient
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.features.search.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val storageModule = module {

    single<File>(named("external_directory_pictures")){
        androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: androidContext().filesDir
    }

    single<ContentResolver>{
        androidContext().contentResolver
    }

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

    single<TrackDao> {
        get<AppDataBase>().trackDao()
    }

    single<TracksInPlaylistsDao> {
        get<AppDataBase>().tracksInPlaylistsDao()
    }

    single<PlaylistDao> {
        get<AppDataBase>().playlistDao()
    }

    single<IFileStorageClient>(named("playlist_poster")){
        FileStorageClient(
            fileDir = get(named("external_directory_pictures")),
            contentResolver = get<ContentResolver>(),
            catalogName = "playlist_poster"
        )
    }
}