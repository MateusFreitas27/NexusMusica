package br.com.nexus.nexusmusica.services

import android.support.v4.media.MediaDescriptionCompat
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.modelo.Musica

class PlayerControle(private val conector: MusicaConector) {
    private val playbackState = conector.playbackState
    var mediaMusica: Musica = MusicaVazia
    val musicaReproduzindo = conector.infoMusicaTocando

    fun iniciarReproducao(media: Musica){
        mediaMusica = media
        conector.transportControls.prepareFromMediaId(mediaMusica.id.toString(), null)
    }

    fun retomarReproducao(media: Musica){
        mediaMusica = media
        conector.transportControls.playFromMediaId(mediaMusica.id.toString(), null)
    }

    fun playPause(){
        if (playbackState.value?.state == 3 ){
            conector.transportControls.pause()
        } else {
            conector.transportControls.play()
        }
    }

    fun proxima(){
        conector.transportControls.skipToNext()
    }

    fun anterior(){
        conector.transportControls.skipToPrevious()
    }

    fun alterarTempo(tempo: Long){
        conector.transportControls.seekTo(tempo)
    }

    fun alterarVelocidadeReproducao(velocidade: Float){
        conector.transportControls.setPlaybackSpeed(velocidade)
    }

    fun removerListaReproducao(description: MediaDescriptionCompat?){
        conector.removeMusica(description)
    }

    fun modoRepeticao(modo: Int){
        conector.transportControls.setRepeatMode(modo)
    }

    fun modoAleatorio(modo: Int){
        conector.transportControls.setShuffleMode(modo)
    }

    fun tempoExecucao(): Long?{
        return playbackState.value?.position
    }

    fun estadoReproducao(): Int{
        return if (playbackState.value == null) 0 else playbackState.value!!.state
    }

}