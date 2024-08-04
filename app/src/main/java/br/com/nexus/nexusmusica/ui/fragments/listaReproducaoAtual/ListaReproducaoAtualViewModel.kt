package br.com.nexus.nexusmusica.ui.fragments.listaReproducaoAtual

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_ALEATORIO
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.services.MusicaConector
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil

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