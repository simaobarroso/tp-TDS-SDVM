package com.example.braguide.ui.fragments


import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider
import com.example.braguide.model.EdgeTip
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.braguide.R
import com.example.braguide.ui.viewadapters.EdgeTipViewAdapter
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory

import java.util.concurrent.atomic.AtomicBoolean


class PinFragment : Fragment() {
    private var id = 0
    var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!context?.let { MapsFragment.meetsPreRequisites(it) }!!) {
            val toast: Toast =
                Toast.makeText(context, "Install Google Maps to navigate", Toast.LENGTH_SHORT)
            toast.show()
        }
        val view: View = inflater.inflate(R.layout.fragment_pin, container, false)
        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        val trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]
        trailViewModel.getPinsById(listOf(id)).observe(getViewLifecycleOwner()) { x ->
            if (x != null && x.isNotEmpty()) {
                loadView(view, x[0])
            }
        }
        return view
    }

    private fun loadView(view: View, edgeTip: EdgeTip) {
        val titulo = view.findViewById<TextView>(R.id.pin_title)
        titulo.text = edgeTip.pinName
        val imagem = view.findViewById<ImageView>(R.id.pin_img)
        EdgeTipViewAdapter.setImageView(edgeTip, imagem)
        val video = view.findViewById<VideoView>(R.id.pin_vid)
        Log.e("Debug", "boolean=" + EdgeTipViewAdapter.setVideoView(edgeTip, video))
        val playButton = view.findViewById<Button>(R.id.audio_playbutton)
        mediaPlayer = MediaPlayer()
        if (edgeTip.hasAudio()) {
            playButton.visibility = View.VISIBLE
            EdgeTipViewAdapter.setMediaPlayer(edgeTip, view, mediaPlayer!!)
            val isPlaying = AtomicBoolean(false)
            playButton.setOnClickListener { v: View? ->
                if (isPlaying.get()) {
                    mediaPlayer!!.pause()
                    isPlaying.set(false)
                } else {
                    mediaPlayer!!.start()
                    isPlaying.set(true)
                }
            }
        } else {
            playButton.visibility = View.GONE
        }


        // Add touch listener to the video view
        val description = view.findViewById<TextView>(R.id.pin_description)
        description.text = edgeTip.pinDesc
        val childFragmentManager: FragmentManager = getChildFragmentManager()
        val mapsFragment = MapsFragment.newInstance(listOf(edgeTip))
        val transaction2: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction2.replace(R.id.pin_mapView, mapsFragment)
        transaction2.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    companion object {
        // Factory method to create a new instance of the fragment
        fun newInstance(id: Int): PinFragment {
            val fragment = PinFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.setArguments(args)
            return fragment
        }
    }
}