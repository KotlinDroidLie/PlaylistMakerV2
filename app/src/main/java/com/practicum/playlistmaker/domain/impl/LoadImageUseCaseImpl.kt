package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.R
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.domain.api.usecase.LoadImageUseCase

class LoadImageUseCaseImpl(private val cornerRadius: Int) : LoadImageUseCase {
    override fun execute(url: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(imageView)
    }
}