package com.example.braguide.ui.viewadapters

import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import com.example.braguide.model.EdgeTip


object EdgeTipViewAdapter {
    fun setImageView(edgeTip: EdgeTip, view: ImageView): Boolean {
        for (medium in edgeTip.media) {
            if (MediaViewAdapter.setImageView(medium, view)) {
                return true
            }
        }
        return false
    }

    fun setVideoView(edgeTip: EdgeTip, view: VideoView): Boolean {
        for (medium in edgeTip.media) {
            if (MediaViewAdapter.setVideoView(medium, view)) {
                return true
            }
        }
        return false
    }

    fun setMediaPlayer(edgeTip: EdgeTip, view: View, mediaPlayer: MediaPlayer): Boolean {
        for (medium in edgeTip.media) {
            if (MediaViewAdapter.setMediaPlayer(medium, view, mediaPlayer)) {
                return true
            }
        }
        return false
    }
}