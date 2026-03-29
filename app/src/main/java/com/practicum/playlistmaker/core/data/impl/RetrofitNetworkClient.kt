package com.practicum.playlistmaker.core.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.core.data.api.ITunesApi
import com.practicum.playlistmaker.core.data.api.NetworkClient
import com.practicum.playlistmaker.core.data.dto.Response
import com.practicum.playlistmaker.core.data.dto.TrackRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context): NetworkClient {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(I_TUNES_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi = retrofit.create(ITunesApi::class.java)

    override fun requestTracks(dto: TrackRequest): Response {
        if(!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        val response = iTunesApi.search(dto.expression).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
    }
    companion object{
        private const val I_TUNES_BASE_URL = "https://itunes.apple.com"
    }
    private fun isConnected(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        capabilities?.let {
            when{
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}