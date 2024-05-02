package com.example.braguide.ui.viewadapters


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import com.example.braguide.model.Medium
import com.squareup.picasso.Picasso
import java.io.File
import java.util.Objects
import com.example.braguide.R

object MediaViewAdapter {
    fun setImageView(medium: Medium, view: ImageView): Boolean {
        val context = view.context.applicationContext
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val isConnected = activeNetwork != null
        return if (medium.mediaType == "I") {
            // Get stored file
            val filename: String = medium.mediaFile!!.replace("http://", "").replace("/", "")
            val file = File(context.filesDir, filename)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                view.setImageBitmap(bitmap)
            } else if (isConnected) {
                Picasso.get().load(medium.mediaFile?.replace("http", "https"))
                    .error(R.drawable.no_preview_image).into(view)
            } else {
                val defaultDrawable =
                    ContextCompat.getDrawable(context, R.drawable.no_preview_image)
                view.setImageDrawable(defaultDrawable)
            }
            true
        } else false
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setVideoView(medium: Medium, view: VideoView): Boolean {
        val context = view.context.applicationContext
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val isConnected = activeNetwork != null
        if (medium.mediaFile == "V") {
            // Get stored file
            val filename: String = medium.mediaFile!!.replace("http://", "").replace("/", "")
            val file = File(context.filesDir, filename)
            if (file.exists()) {
                view.setVideoPath(file.absolutePath)
            } else if (isConnected) {
                view.setVideoPath(medium.mediaFile!!.replace("http", "https"))
            } else {
                Toast.makeText(context, "No video available", Toast.LENGTH_SHORT).show()
            }
            view.seekTo(1)
            view.setOnTouchListener(object : OnTouchListener {
                var isPaused = true
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    if (event.action == MotionEvent.ACTION_UP) {
                        isPaused = if (isPaused) {
                            // Video is currently paused, start playback
                            view.start()
                            false
                        } else {
                            // Video is currently playing, pause playback
                            view.pause()
                            true
                        }
                    }
                    return true
                }
            })
        } else {
            view.setOnTouchListener { view1: View, event: MotionEvent ->
                if (event.action == MotionEvent.ACTION_UP) {
                    Toast.makeText(view1.context, "No video available", Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            return false
        }
        return true
    }

    fun setMediaPlayer(medium: Medium, view: View, mediaPlayer: MediaPlayer): Boolean {
        val context = view.context.applicationContext
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val isConnected = activeNetwork != null
        return if (Objects.equals(medium.mediaType, "R")) {
            try {
                val filename: String =
                    medium.mediaFile!!.replace("http://", "").replace("/", "")
                val file = File(context.filesDir, filename)
                if (file.exists()) {
                    mediaPlayer.setDataSource(file.absolutePath)
                } else if (isConnected) {
                    mediaPlayer.setDataSource(medium.mediaFile!!.replace("http", "https"))
                } else {
                    Toast.makeText(context, "No audio available", Toast.LENGTH_SHORT).show()
                }
                mediaPlayer.prepareAsync()
            } catch (e: Exception) {
                Log.e("MediaViewAdapter", "Media Error")
                return false
            }
            true
        } else false
    }
}