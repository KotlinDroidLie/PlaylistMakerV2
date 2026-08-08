package com.practicum.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.practicum.playlistmaker.R

class NetworkStateReceiver: BroadcastReceiver() {
    companion object{
        const val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action != CONNECTIVITY_CHANGE_ACTION){
            return
        }
        val isConnected = context.isConnected()
        if(!isConnected){
            Toast.makeText(context, R.string.placeholder_text_error_network,Toast.LENGTH_SHORT).show()
        }
    }
}