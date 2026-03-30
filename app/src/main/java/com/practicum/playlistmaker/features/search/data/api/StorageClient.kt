package com.practicum.playlistmaker.features.search.data.api

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}