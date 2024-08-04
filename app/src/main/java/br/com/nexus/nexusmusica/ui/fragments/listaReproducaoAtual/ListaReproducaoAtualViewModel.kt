package br.com.nexus.nexusmusica.ui.fragments.listaReproducaoAtual

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.services.PlayerControle

class ListaReproducaoAtualViewModel(private val playerControle: PlayerControle): ViewModel(){
    var listaMusicas: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = MutableLiveData()

    private val subcribeCallback: SubscriptionCallback = object : SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            listaMusicas.value = children
        }
    }

    fun reproduzirMusicaSelecionada(mediaId: Long) {
        playerControle.reproduzirMusicaSelecionada(mediaId)
    }

    fun carregarListaMusica() {
        playerControle.criarOuvinteListaMusica(subcribeCallback)
    }

    override fun onCleared() {
        playerControle.removeOuvinteListaMusica(subcribeCallback)
        super.onCleared()
    }
}