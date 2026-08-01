package com.practicum.playlistmaker.features.player.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R
import java.lang.Integer.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    ): View(context,attrs) {
    private val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener(){
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                togglePlayback()
                performClick()
                return true
            }
        }
    )
    private var imageRect = RectF(0f,0f,0f,0f)
    internal var state: PlaybackButtonViewState = PlaybackButtonViewState.Play
        set(value) {
            field = value
            invalidate()
        }
    private val playImageBitMap: Bitmap?
    private val minSizeView = resources.getDimensionPixelSize(R.dimen.playbackButtonViewMinSize)
    private val pauseImageBitMap: Bitmap?
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlayBackButtonView,
            0,
            0
        ).apply {
            try {
                playImageBitMap = getDrawable(R.styleable.PlayBackButtonView_playImageResId)?.toBitmap()
                pauseImageBitMap = getDrawable(R.styleable.PlayBackButtonView_pauseImageResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val contentWidth = resolveSize(widthMeasureSpec)
        val contentHeight =  resolveSize(heightMeasureSpec)
        val size = min(contentWidth, contentHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        when(state){
            PlaybackButtonViewState.Pause ->{
                drawBitmap(pauseImageBitMap, canvas)
            }
            PlaybackButtonViewState.Play ->{
                drawBitmap(playImageBitMap, canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(!isEnabled) return false
        return gestureDetector.onTouchEvent(event)
    }

    private fun drawBitmap(bitMap: Bitmap?, canvas: Canvas){
        bitMap?.let {
            canvas.drawBitmap(bitMap,null, imageRect, null)
        }
    }

    private fun togglePlayback() {
        state = when (state) {
            PlaybackButtonViewState.Pause -> PlaybackButtonViewState.Play
            PlaybackButtonViewState.Play -> PlaybackButtonViewState.Pause
        }
    }

    private fun resolveSize(measureSpec: Int): Int{
        val size = MeasureSpec.getSize(measureSpec)
        val mode = MeasureSpec.getMode(measureSpec)

        return when(mode){
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> minSizeView
            MeasureSpec.AT_MOST -> size
            else -> error("Неизвестный режим ${size}")
        }
    }
}