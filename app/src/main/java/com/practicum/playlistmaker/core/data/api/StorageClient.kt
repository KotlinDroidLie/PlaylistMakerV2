package com.practicum.playlistmaker.core.data.api

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}