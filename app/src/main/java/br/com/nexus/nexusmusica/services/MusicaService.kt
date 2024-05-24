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
import androidx.media.MediaBrowserServiceCompat
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.SERVICE_TAG
import br.com.nexus.nexusmusica.helper.EmbaralharHelper
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.google.gson.Gson
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

    private var listaMusicasReproducao: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
    private var listaMusicasOriginal: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
    private var posicaoAtualReproducao: Int = -1
    private val musicasRepositorio by inject<MusicaRepositorio>()
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
        if (parentId.isNotEmpty()) result.sendResult(listaMusicasReproducao) else result.detach()
    }

    override fun onCompletion(player: MediaPlayer?) {
        if (SharedPreferenceUtil.modoRepeticaoMusica == PlaybackStateCompat.REPEAT_MODE_ALL){
            if (posicaoAtualReproducao == (listaMusicasReproducao.size - 1)){
                posicaoAtualReproducao = 0
                iniciaReproducao()
            }else {
                proximaFaixa()
            }
        } else {
            proximaFaixa()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
        initModos()
    }

    private fun initModos() {
        repetiTodas = when ( SharedPreferenceUtil.modoRepeticaoMusica){
            PlaybackStateCompat.REPEAT_MODE_ALL -> true
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
            setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
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
                    stateBuilder.setActiveQueueItemId(posicaoAtualReproducao-1L).setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        mediaPlayer!!.currentPosition.toLong(),
                        1F
                    ).setActions(
                        PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_SEEK_TO
                    )
                    serviceScope.launch {
                        SharedPreferenceUtil.salvarTempoExecucao(mediaPlayer!!.currentPosition.toLong())
                    }
                }else{
                    stateBuilder.setActiveQueueItemId(posicaoAtualReproducao-1L).setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        mediaPlayer!!.currentPosition.toLong(),
                        1F
                    ).setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_SEEK_TO
                    )
                    serviceScope.launch {
                        SharedPreferenceUtil.salvarTempoExecucao(mediaPlayer!!.currentPosition.toLong())
                    }
                }
                mediaSession?.setPlaybackState(stateBuilder.build())
                delay(500)
            }
        }
    }

    fun abrirFilaReproducao(musicas: ArrayList<MediaBrowserCompat.MediaItem>, posicaoInicial: Int){
        if (musicas.isNotEmpty() && posicaoInicial >= 0){
            listaMusicasOriginal = ArrayList(musicas)
            listaMusicasReproducao = ArrayList(listaMusicasOriginal)
            posicaoAtualReproducao = posicaoInicial
            iniciaReproducao()
            salvarListaReproducao(listaMusicasReproducao)
        }
    }

    fun reproduzirMusicaSelecionada(mediaId: String?) {
        for ( indice in listaMusicasReproducao.indices){
            if (mediaId == listaMusicasReproducao[indice].mediaId){
                posicaoAtualReproducao = indice
            }
        }
        iniciaReproducao()
    }

    private fun iniciaReproducao(){
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer.create(baseContext, listaMusicasReproducao[posicaoAtualReproducao].description.mediaUri).apply {
            setWakeMode(baseContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            setVolume(1.0f,1.0f)
            start()
            carregarMediaMetadata(musicasRepositorio.musica(listaMusicasReproducao[posicaoAtualReproducao].mediaId!!.toLong()))
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
        salvarInfoMusica(musica)
    }

    fun proximaFaixa(){
        //if (repetiTodas) posicaoAtualReproducao = -1
        if (posicaoAtualReproducao < (listaMusicasReproducao.size-1)){
            posicaoAtualReproducao += 1
            iniciaReproducao()
        }
    }

    fun faixaAnterior(){
        if (listaMusicasReproducao.isNotEmpty()){
            posicaoAtualReproducao -= 1
            if (posicaoAtualReproducao > 0) iniciaReproducao()
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

    fun removeMusica(description: MediaDescriptionCompat?) {
        val novaListaReproducao: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
        listaMusicasReproducao.forEach {
            if (it.mediaId != description?.mediaId) novaListaReproducao.add(it)
        }
        listaMusicasReproducao = novaListaReproducao
        listaMusicasOriginal = novaListaReproducao
        iniciaReproducao()
        salvarListaReproducao(listaMusicasReproducao)
    }

    fun trocarModoRepeticao(modoRepeticao: Int){
        repetiTodas = when(modoRepeticao){
            PlaybackStateCompat.REPEAT_MODE_ALL -> true
            else -> false
        }

    }

    fun alterarVelocidadePlayer(velodicade: Float){
        val playerParams = mediaPlayer?.playbackParams!!
        playerParams.speed = velodicade
        mediaPlayer?.playbackParams = playerParams
        SharedPreferenceUtil.velocidadeReproducaoMedia = velodicade

    }

    fun alterarModoAleatorio(modoAleatorio: Int){
        when(modoAleatorio){
            PlaybackStateCompat.SHUFFLE_MODE_ALL ->{
                EmbaralharHelper.embaralharLista(listaMusicasReproducao, posicaoAtualReproducao)
                posicaoAtualReproducao = 0
            } else -> {
                val musica = listaMusicasReproducao[posicaoAtualReproducao]
                listaMusicasReproducao = ArrayList(listaMusicasOriginal)
                val posicao = listaMusicasReproducao.indexOfFirst { it.description.mediaId == musica.description.mediaId }
                posicaoAtualReproducao = posicao
            }
        }
    }

    private fun salvarInfoMusica(musica: Musica){
        val gson = Gson()
        val musicaJson = gson.toJson(musica)
        SharedPreferenceUtil.musicaTocando = musicaJson
        SharedPreferenceUtil.posicaoReproducaoLista = posicaoAtualReproducao
    }

    private fun salvarListaReproducao(lista: MutableList<MediaBrowserCompat.MediaItem>) {
        val gson = Gson()
        val listaMusicas = gson.toJson(lista)
        serviceScope.launch {
            SharedPreferenceUtil.salvarListaReproducao(listaMusicas)
        }
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