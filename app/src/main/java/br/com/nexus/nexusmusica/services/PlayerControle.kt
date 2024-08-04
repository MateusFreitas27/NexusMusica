package br.com.nexus.nexusmusica.services

import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaDescriptionCompat
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_ALEATORIO
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil

class PlayerControle(private val conectorServiceMusica: MusicaConector) {
    val playbackState = conectorServiceMusica.playbackState
    var mediaMusica: Musica = MusicaVazia
    val musicaReproduzindo = conectorServiceMusica.infoMusicaTocando

    fun iniciarReproducao(media: Musica){
        mediaMusica = media
        conectorServiceMusica.transportControls.prepareFromMediaId(mediaMusica.id.toString(), null)
    }

    fun retomarReproducao(media: Musica){
        mediaMusica = media
        conectorServiceMusica.transportControls.playFromMediaId(mediaMusica.id.toString(), null)
    }

    fun playPause(){
        if (playbackState.value?.state == 3 ){
            conectorServiceMusica.transportControls.pause()
        } else {
            conectorServiceMusica.transportControls.play()
        }
    }

    fun proxima(){
        conectorServiceMusica.transportControls.skipToNext()
    }

    fun anterior(){
        conectorServiceMusica.transportControls.skipToPrevious()
    }

    fun alterarTempo(tempo: Long){
        conectorServiceMusica.transportControls.seekTo(tempo)
    }

    fun alterarVelocidadeReproducao(velocidade: Float){
        conectorServiceMusica.transportControls.setPlaybackSpeed(velocidade)
    }

    fun removerListaReproducao(description: MediaDescriptionCompat?){
        conectorServiceMusica.removeMusica(description)
    }

    fun modoRepeticao(modo: Int){
        conectorServiceMusica.transportControls.setRepeatMode(modo)
    }

    fun modoAleatorio(modo: Int){
        conectorServiceMusica.transportControls.setShuffleMode(modo)
    }

    fun tempoExecucao(): Long?{
        return playbackState.value?.position
    }

    fun reproduzirMusicaSelecionada(id: Long){
        conectorServiceMusica.transportControls.skipToQueueItem(id)
    }

    fun criarOuvinteListaMusica(callback: SubscriptionCallback) {
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> conectorServiceMusica.subcribe(REPRODUCAO_MUSICAS, callback)
            REPRODUCAO_ALBUM -> conectorServiceMusica.subcribe(REPRODUCAO_ALBUM, callback)
            REPRODUCAO_ADICOES_RECENTES -> conectorServiceMusica.subcribe(REPRODUCAO_ADICOES_RECENTES, callback)
            REPRODUCAO_ALEATORIO -> conectorServiceMusica.subcribe(REPRODUCAO_ALEATORIO, callback)
        }
    }

    fun removeOuvinteListaMusica(callback: SubscriptionCallback){
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> conectorServiceMusica.unsubscribe(REPRODUCAO_MUSICAS, callback)
            REPRODUCAO_ALBUM -> conectorServiceMusica.unsubscribe(REPRODUCAO_ALBUM, callback)
            REPRODUCAO_ADICOES_RECENTES -> conectorServiceMusica.unsubscribe(REPRODUCAO_ADICOES_RECENTES, callback)
            REPRODUCAO_ALEATORIO -> conectorServiceMusica.unsubscribe(REPRODUCAO_ALEATORIO, callback)
        }
    }
}