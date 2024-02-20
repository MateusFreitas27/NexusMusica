package br.com.nexus.nexusmusica.services

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.SERVICE_TAG
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.AlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicasRecentesRepositorio
import br.com.nexus.nexusmusica.util.Repeticao
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class MusicaService: MediaBrowserServiceCompat(), MediaPlayer.OnCompletionListener, KoinComponent {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private var serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var originalListaMusicas: MutableList<Musica> = arrayListOf()
    private var listaMusicas: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
    private var posicao: Int = -1
    private val musicasRepositorio by inject<MusicaRepositorio>()
    private val albumRepositorio by inject<AlbumRepositorio>()
    private val musicasRecentesRepositorio by inject<MusicasRecentesRepositorio>()
    private var repetiTodas: Boolean = false

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val musicas: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
        when(parentId){
            REPRODUCAO_MUSICAS -> {
                musicasRepositorio.musicas().forEach {
                    val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                        .setMediaId(it.id.toString())
                        .setMediaUri(it.data.toUri())
                        .setTitle(it.titulo)
                        .setSubtitle(it.artistaNome)
                    musicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), 0))
                }
                result.sendResult(musicas)
            }
            REPRODUCAO_ALBUM -> {
                val idalbum: Long = SharedPreferenceUtil.idAlbumMusica
                albumRepositorio.album(idalbum).let {album ->
                    album.musicas.forEach {
                        val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                            .setMediaId(it.id.toString())
                            .setMediaUri(it.data.toUri())
                            .setTitle(it.titulo)
                            .setSubtitle(it.artistaNome)
                        musicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), 0))
                    }
                }
                result.sendResult(musicas)
            }
            REPRODUCAO_ADICOES_RECENTES -> {
                musicasRecentesRepositorio.musicasRecentes().forEach {
                    val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                        .setMediaId(it.id.toString())
                        .setMediaUri(it.data.toUri())
                        .setTitle(it.titulo)
                        .setSubtitle(it.artistaNome)
                    musicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), 0))
                }
                result.sendResult(musicas)
            }
            else -> result.detach()
        }
    }

    override fun onCompletion(player: MediaPlayer?) {
        when(SharedPreferenceUtil.modoRepeticaoMusica){
            Repeticao.UMA.toString() -> iniciaReproducao()
            Repeticao.DESATIVADO.toString() -> proximaFaixa()
            Repeticao.TODAS.toString() ->{
                if (posicao == (listaMusicas.size - 1)){
                    posicao = 0
                    iniciaReproducao()
                }else {
                    proximaFaixa()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        initModos()
    }

    private fun initModos() {
        repetiTodas = when ( SharedPreferenceUtil.modoRepeticaoMusica){
            Repeticao.TODAS.toString() -> true
            else -> false
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        serviceScope.cancel()
    }

    private fun initMediaSession(){
        mediaSession = MediaSessionCompat(baseContext, SERVICE_TAG).apply {
            stateBuilder = PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
            setPlaybackState(stateBuilder.build())
            setCallback(MediaSessionCallback(this@MusicaService))
            setSessionToken(sessionToken)
            isActive = true
        }
    }

    private fun iniciaNotificacao() {
        Notificacao.criarNotificacao(this,mediaSession!!.sessionToken)
    }

    private fun atualizarPlaybackState(){
        serviceScope.launch {
            while (true){
                if (mediaPlayer!!.isPlaying){
                    stateBuilder.setActiveQueueItemId(posicao-1L).setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        mediaPlayer!!.currentPosition.toLong(),
                        1F
                    ).setActions(
                        PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_SEEK_TO
                    )
                }else{
                    stateBuilder.setActiveQueueItemId(posicao-1L).setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        mediaPlayer!!.currentPosition.toLong(),
                        1F
                    ).setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_SEEK_TO
                    )
                }
                mediaSession?.setPlaybackState(stateBuilder.build())
                delay(500)
            }
        }
    }

    fun abrirFilaReproducao(musicas: ArrayList<MediaBrowserCompat.MediaItem>, posicaoInicial: Int, iniciarPlayer: Boolean){
        if (musicas.isNotEmpty() && posicaoInicial >= 0){
            listaMusicas = musicas
            posicao = posicaoInicial
            iniciaReproducao()
        }
    }

    private fun iniciaReproducao(){
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer.create(baseContext, listaMusicas[posicao].description.mediaUri).apply {
            setWakeMode(baseContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            setVolume(1.0f,1.0f)
            start()
            carregarMediaMetadata(musicasRepositorio.musica(listaMusicas[posicao].mediaId!!.toLong()))
            setOnCompletionListener(this@MusicaService)
        }
        alterarVelocidadePlayer(SharedPreferenceUtil.velocidadeReproducaoMedia)
        iniciaNotificacao()
        atualizarPlaybackState()
    }

    private fun carregarMediaMetadata(musica: Musica) {
        val mediaMetada = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, musica.id.toString())
            .putText(MediaMetadataCompat.METADATA_KEY_TITLE, musica.titulo)
            .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, musica.albumNome)
            .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, musica.artistaNome)
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, musica.data)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, musica.duracao)
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, retornaBitmapCapaMusica(musica.data))
        mediaSession?.setMetadata(mediaMetada.build())
    }

    fun proximaFaixa(){
        if (repetiTodas) posicao = -1
        if (posicao < (listaMusicas.size-1)){
            posicao += 1
            iniciaReproducao()
        }
    }

    fun faixaAnterior(){
        if (listaMusicas.isNotEmpty()){
            posicao -= 1
            if (posicao > 0) iniciaReproducao()
        }
    }

    fun play(){
        mediaPlayer?.let {
            if (!it.isPlaying) it.start()
        }
    }

    fun pause(){
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
        }
    }

    fun seek(pos: Int){
        mediaPlayer?.seekTo(pos)
    }

    fun trocarModoRepeticao(modoRepeticao: Int){
        repetiTodas = when(modoRepeticao){
            Repeticao.TODAS.codigo -> true
            else -> false
        }

    }

    fun alterarVelocidadePlayer(velodicade: Float){
        val playerParams = mediaPlayer?.playbackParams!!
        playerParams.speed = velodicade
        mediaPlayer?.playbackParams = playerParams
        SharedPreferenceUtil.velocidadeReproducaoMedia = velodicade

    }

    private fun retornaBitmapCapaMusica(uri: String): Bitmap {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val byte = retriever.embeddedPicture
        retriever.release()
        return if (byte != null){
            BitmapFactory.decodeByteArray(byte, 0, byte.size)
        }else{
            val capaVazia = ResourcesCompat.getDrawable(resources,R.drawable.sem_album, theme)
            capaVazia!!.toBitmap()
        }
    }
}