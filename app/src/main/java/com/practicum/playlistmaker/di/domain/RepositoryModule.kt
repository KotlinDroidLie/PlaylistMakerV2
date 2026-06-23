package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.di.data.networkModule
import com.practicum.playlistmaker.di.data.storageModule
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.impl.FavouriteRepo
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteRepo
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.features.search.data.impl.RemoteTrackRepository
import com.practicum.playlistmaker.features.search.data.impl.SearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.features.settings.data.impl.SettingsRepository
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module{
    includes(networkModule, storageModule)

    single<IRemoteTrackRepository>{
        RemoteTrackRepository(get<NetworkClient>())
    }

    single<ISearchHistoryRepository>{
        SearchHistoryRepository(get<StorageClient<MutableList<TrackHistoryDto>>>(named("history")))
    }

    single<ISettingsRepository>{
        SettingsRepository(get<StorageClient<SettingsDto>>(named("settings")))
    }

    single<IFavouriteRepo>{
        FavouriteRepo(get<AppDataBase>())
    }
}