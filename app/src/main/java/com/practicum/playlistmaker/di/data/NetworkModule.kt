package com.practicum.playlistmaker.di.data

import com.practicum.playlistmaker.features.search.data.api.ITunesApi
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.impl.RetrofitNetworkClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<HttpLoggingInterceptor>{
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<OkHttpClient>{
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<ITunesApi>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient>{
        RetrofitNetworkClient(context = androidContext(), iTunesApiService = get<ITunesApi>())
    }

}