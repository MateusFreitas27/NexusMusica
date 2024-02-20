package br.com.nexus.nexusmusica.services

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MusicaConector(val context: Context) {
    private val _conectado = MutableLiveData<Boolean>()
    val conectado: LiveData<Boolean> = _conectado
    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?> = _playbackState
    private val _infoMusicaTocando = MutableLiveData<MediaMetadataCompat?>()
    val infoMusicaTocando: LiveData<MediaMetadataCompat?> = _infoMusicaTocando
    lateinit var mediaController: MediaControllerCompat
    private val mediaBrowserMusicaConector = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(context, ComponentName(context, MusicaService::class.java), mediaBrowserMusicaConector, null).apply {
        connect()
    }
    val mediaControllerCallback = MediaControllerCallback()
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun subcribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context): MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
            mediaController = MediaControllerCompat(context,mediaBrowser.sessionToken).apply {
                registerCallback(mediaControllerCallback)
            }
            _conectado.value = true
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            _conectado.value = false
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            _conectado.value = false
        }
    }

    inner class MediaControllerCallback(): MediaControllerCompat.Callback(){
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playbackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            _infoMusicaTocando.value = metadata
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserMusicaConector.onConnectionSuspended()
        }
    }
}