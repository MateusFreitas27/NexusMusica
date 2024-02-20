package br.com.nexus.nexusmusica.services

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.net.toUri
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.repositorio.AlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicasRecentesRepositorio
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MediaSessionCallback(private val musicaService: MusicaService): MediaSessionCompat.Callback(), KoinComponent {
    private val musicaRepositorio by inject<MusicaRepositorio>()
    private val albumRepositorio by inject<AlbumRepositorio>()
    private val musicasRecentesRepositorio by inject<MusicasRecentesRepositorio>()

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

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        super.onPlayFromMediaId(mediaId, extras)
        var listaMusicas: ArrayList<MediaBrowserCompat.MediaItem> = ArrayList()
        var posicaoMusicaId = 0
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> {
                val musicas = ArrayList(musicaRepositorio.musicas())
                for ( indice in musicas.indices){
                    if (mediaId == musicas[indice].id.toString()){
                        posicaoMusicaId = indice
                    }
                }
                musicas.forEach { musica ->
                    val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                        .setMediaId(musica.id.toString())
                        .setMediaUri(musica.data.toUri())
                        .setTitle(musica.titulo)
                        .setSubtitle(musica.albumNome)
                    listaMusicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),MediaBrowserCompat.MediaItem.FLAG_PLAYABLE))
                }
            }
            REPRODUCAO_ALBUM -> {
                val idAlbum: Long = SharedPreferenceUtil.idAlbumMusica
                val musicas = ArrayList(albumRepositorio.album(idAlbum).musicas)
                for ( indice in musicas.indices){
                    if (mediaId == musicas[indice].id.toString()){
                        posicaoMusicaId = indice
                    }
                }
                musicas.forEach { musica ->
                    val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                        .setMediaId(musica.id.toString())
                        .setMediaUri(musica.data.toUri())
                        .setTitle(musica.titulo)
                        .setSubtitle(musica.albumNome)
                    listaMusicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),MediaBrowserCompat.MediaItem.FLAG_PLAYABLE))
                }
            }
            REPRODUCAO_ADICOES_RECENTES -> {
                val musicas = ArrayList(musicasRecentesRepositorio.musicasRecentes())
                for ( indice in musicas.indices){
                    if (mediaId == musicas[indice].id.toString()){
                        posicaoMusicaId = indice
                    }
                }
                musicas.forEach { musica ->
                    val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                        .setMediaId(musica.id.toString())
                        .setMediaUri(musica.data.toUri())
                        .setTitle(musica.titulo)
                        .setSubtitle(musica.albumNome)
                    listaMusicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),MediaBrowserCompat.MediaItem.FLAG_PLAYABLE))
                }
            }
        }
        musicaService.abrirFilaReproducao(listaMusicas, posicaoMusicaId, true)
    }
}