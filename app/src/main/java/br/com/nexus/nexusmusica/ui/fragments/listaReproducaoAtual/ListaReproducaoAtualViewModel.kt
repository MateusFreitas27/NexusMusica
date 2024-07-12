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
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil

class ListaReproducaoAtualViewModel(private val musicaConector: MusicaConector): ViewModel(){
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
        musicaConector.transportControls.skipToQueueItem(mediaId)
    }

    fun carregarListaMusica() {
        when (SharedPreferenceUtil.modoReproducaoPlayer) {
            REPRODUCAO_MUSICAS -> musicaConector.subcribe(REPRODUCAO_MUSICAS, subcribeCallback)
            REPRODUCAO_ALBUM -> musicaConector.subcribe(REPRODUCAO_ALBUM, subcribeCallback)
            REPRODUCAO_ADICOES_RECENTES -> musicaConector.subcribe(
                REPRODUCAO_ADICOES_RECENTES,
                subcribeCallback
            )

            REPRODUCAO_ALEATORIO -> musicaConector.subcribe(REPRODUCAO_ALEATORIO, subcribeCallback)
        }
    }

    override fun onCleared() {
        when (SharedPreferenceUtil.modoReproducaoPlayer) {
            REPRODUCAO_MUSICAS -> musicaConector.unsubscribe(REPRODUCAO_MUSICAS, subcribeCallback)
            REPRODUCAO_ALBUM -> musicaConector.unsubscribe(REPRODUCAO_ALBUM, subcribeCallback)
            REPRODUCAO_ADICOES_RECENTES -> musicaConector.unsubscribe(
                REPRODUCAO_ADICOES_RECENTES,
                subcribeCallback
            )
            REPRODUCAO_ALEATORIO -> musicaConector.subcribe(REPRODUCAO_ALEATORIO, subcribeCallback)
        }
        super.onCleared()
    }
}