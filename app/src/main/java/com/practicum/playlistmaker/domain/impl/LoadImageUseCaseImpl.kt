package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.util.TypedValue
import com.practicum.playlistmaker.R
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.domain.api.usecase.LoadImageUseCase

class LoadImageUseCaseImpl(context: Context) : LoadImageUseCase {
    val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        8f,
        context.resources.displayMetrics
    ).toInt()

    override fun execute(url: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(imageView)
    }
}