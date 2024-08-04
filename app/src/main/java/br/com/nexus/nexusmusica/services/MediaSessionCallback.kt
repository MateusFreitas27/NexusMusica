package br.com.nexus.nexusmusica.services

import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import br.com.nexus.nexusmusica.di.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.di.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.di.REPRODUCAO_HISTORICO
import br.com.nexus.nexusmusica.di.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.AlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicasRecentesRepositorio
import br.com.nexus.nexusmusica.repositorio.RoomRepository
import br.com.nexus.nexusmusica.room.toMusica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MediaSessionCallback(private val musicaService: MusicaService): MediaSessionCompat.Callback(), KoinComponent {
    private val musicaRepositorio by inject<MusicaRepositorio>()
    private val albumRepositorio by inject<AlbumRepositorio>()
    private val musicasRecentesRepositorio by inject<MusicasRecentesRepositorio>()
    private val roomRepositorio by inject<RoomRepository>()

    override fun onPlay() {
        super.onPlay()
        musicaService.play()
    }

    override fun onPause() {
        super.onPause()
        musicaService.pause()
    }

    override fun onSkipToNext() {
        super.onSkipToNext()
        musicaService.proximaFaixa()
    }

    override fun onSkipToPrevious() {
        super.onSkipToPrevious()
        musicaService.faixaAnterior()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        musicaService.seek(pos.toInt())
    }

    override fun onSetRepeatMode(repeatMode: Int) {
        super.onSetRepeatMode(repeatMode)
        musicaService.trocarModoRepeticao(repeatMode)
    }

    override fun onSetPlaybackSpeed(speed: Float) {
        super.onSetPlaybackSpeed(speed)
        musicaService.alterarVelocidadePlayer(speed)
    }

    override fun onSetShuffleMode(shuffleMode: Int) {
        musicaService.alterarModoAleatorio(shuffleMode)
    }

    override fun onRemoveQueueItem(description: MediaDescriptionCompat?) {
        super.onRemoveQueueItem(description)
        musicaService.removeMusica(description)
    }

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        super.onPlayFromMediaId(mediaId, extras)
        musicaService.retomaReproducaoMusica(mediaId)
    }

    override fun onSkipToQueueItem(id: Long) {
        super.onSkipToQueueItem(id)
        musicaService.reproduzirMusicaSelecionada(id.toString())
    }

    override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
        super.onPrepareFromMediaId(mediaId, extras)
        val listaMusicas: ArrayList<Musica> = ArrayList()
        var posicaoMusicaId = 0
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> {
                val lista = ArrayList(musicaRepositorio.musicas())
                for ((indice, musica) in lista.withIndex()){
                    if (mediaId == musica.id.toString()){
                        posicaoMusicaId = indice
                    }
                    listaMusicas.add(musica)
                }
            }
            REPRODUCAO_ALBUM -> {
                val idAlbum: Long = SharedPreferenceUtil.idAlbumMusica
                val lista = ArrayList(albumRepositorio.album(idAlbum).musicas)
                for ((indice, musica) in lista.withIndex()) {
                    if (mediaId == musica.id.toString()) {
                        posicaoMusicaId = indice
                    }
                    listaMusicas.add(musica)
                }
            }
            REPRODUCAO_ADICOES_RECENTES -> {
                val lista = ArrayList(musicasRecentesRepositorio.musicasRecentes())
                for ((indice, musica) in lista.withIndex()){
                    if (mediaId == musica.id.toString()){
                        posicaoMusicaId = indice
                    }
                    listaMusicas.add(musica)
                }
            }
            REPRODUCAO_HISTORICO -> {
                val lista = ArrayList(roomRepositorio.listaHistorico())
                for ((indice, musica) in lista.withIndex()){
                    if (mediaId == musica.id.toString()){
                        posicaoMusicaId = indice
                    }
                    listaMusicas.add(musica.toMusica())
                }
            }
        }
        musicaService.abrirFilaReproducao(listaMusicas, posicaoMusicaId)
    }

}