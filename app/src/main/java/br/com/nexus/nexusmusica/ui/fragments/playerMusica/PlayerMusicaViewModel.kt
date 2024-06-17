package br.com.nexus.nexusmusica.ui.fragments.playerMusica

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.nexus.nexusmusica.APP
import br.com.nexus.nexusmusica.DELAY_INTERVALO_PLAYER_POSICAO
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_ALEATORIO
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.MusicaConector
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import br.com.nexus.nexusmusica.util.VersaoUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerMusicaViewModel(
    private val musicaConector: MusicaConector,
    private val repositorio: Repositorio
) : ViewModel() {
    private val playbackState = musicaConector.playbackState
    val conectado = musicaConector.conectado
    val infoMusicaTocando = musicaConector.infoMusicaTocando
    private val _duracaoMusica: MutableLiveData<Int> = MutableLiveData<Int>()
    val duracaoMusica: MutableLiveData<Int> = _duracaoMusica
    private val _nomeMusica: MutableLiveData<String> = MutableLiveData<String>()
    val nomeMusica: MutableLiveData<String> = _nomeMusica
    private val _nomeAlbum: MutableLiveData<String> = MutableLiveData<String>()
    val nomeAlbum: MutableLiveData<String> = _nomeAlbum
    private val _imgCapa: MutableLiveData<ByteArray?> = MutableLiveData<ByteArray?>()
    val imgCapa: MutableLiveData<ByteArray?> = _imgCapa
    private val _progressoMusica: MutableLiveData<Long> = MutableLiveData<Long>()
    val progressoMusica: MutableLiveData<Long> = _progressoMusica
    private var _modoRepeticao: MutableLiveData<Int> = MutableLiveData<Int>()
    val modoRepeticao: MutableLiveData<Int> = _modoRepeticao
    private var _modoAleatorio: MutableLiveData<Int> = MutableLiveData<Int>()
    val modoAleatorio: MutableLiveData<Int> = _modoAleatorio
    private var _listaMusicas: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> =
        MutableLiveData()
    val listaMusica: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = _listaMusicas
    private val _tocandoMusica: MutableLiveData<Int> = MutableLiveData<Int>()
    val tocandoMusica: MutableLiveData<Int> = _tocandoMusica
    var media: Musica = MusicaVazia
    private var alterarInfoMusica = false
    private var novaReproducao: Boolean = false

    private val subcribeCallback: SubscriptionCallback = object : SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            _listaMusicas.value = children
        }
    }

    init {
        atualizaPosicaoMediaPlayer()
        inicializarModos()
    }

    private fun inicializarModos() {
        _modoRepeticao.value = SharedPreferenceUtil.modoRepeticaoMusica
        _modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
    }

    private fun atualizaPosicaoMediaPlayer() {
        viewModelScope.launch {
            while (true) {
                _tocandoMusica.value = playbackState.value?.state
                val pos = playbackState.value?.position
                if (_progressoMusica.value != pos ) {
                    _progressoMusica.value = pos ?: SharedPreferenceUtil.tempoExecucaoMusica
                }
                delay(DELAY_INTERVALO_PLAYER_POSICAO)
            }
        }
    }

    fun setMusica(args: PlayerMusicaFragmentArgs) {
        var musica = MusicaVazia
        media = args.musica
        if (SharedPreferenceUtil.musicaTocando!!.isNotEmpty()){
            val gson = Gson()
            val json = SharedPreferenceUtil.musicaTocando
            musica = gson.fromJson(json, Musica::class.java)
            _progressoMusica.value = SharedPreferenceUtil.tempoExecucaoMusica
        }
        novaReproducao = musica.id != media.id
        atualizaMediaReproducao()
    }

    fun iniciar() {
        if (novaReproducao)
            musicaConector.transportControls.prepareFromMediaId(media.id.toString(), null)
    }

    fun playPlause() {
        if (!novaReproducao){
            musicaConector.transportControls.playFromMediaId(media.id.toString(), null)
            novaReproducao = true
        }else {
            if (playbackState.value?.state == 3) {
                musicaConector.transportControls.pause()
            } else {
                musicaConector.transportControls.play()
            }
        }
    }

    fun proximaMusica() {
        musicaConector.transportControls.skipToNext()
    }

    fun musicaAnterior() {
        musicaConector.transportControls.skipToPrevious()
    }

    fun carregarDadosMusica(infoMedia: MediaMetadataCompat?) {
        if (infoMedia?.description?.mediaId != media.id.toString() && alterarInfoMusica) {
            CoroutineScope(Dispatchers.IO).launch {
                val musica =
                    repositorio.consultaMusica(infoMedia!!.description!!.mediaId!!.toLong())
                withContext(Dispatchers.Main) {
                    media = musica
                    atualizaMediaReproducao()
                }
            }
        } else alterarInfoMusica = true

    }

    private fun atualizaMediaReproducao() {
        _duracaoMusica.value = media.duracao.toInt()
        _nomeMusica.value = media.nomeMusica
        _nomeAlbum.value = media.nomeAlbum
        _imgCapa.value = FuncoesUtil.carregarCapaMusica(media.data)
    }

    fun seekToMusica(posicao: Long) {
        musicaConector.transportControls.seekTo(posicao)
    }

    fun alterarVelocidadePlayer(valorSlider: Float) {
        musicaConector.transportControls.setPlaybackSpeed(valorSlider)
    }

    fun reproduzirMusicaSelecionada(mediaId: Long) {
        musicaConector.transportControls.skipToQueueItem(mediaId)
    }

    fun removerMusicaListaReproducao(media: MediaMetadataCompat?) {
        musicaConector.removeMusica(media?.description)
    }

    fun trocarModorepetirMusica() {
        if (_modoRepeticao.value == PlaybackStateCompat.REPEAT_MODE_ALL) {
            SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_NONE
            _modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_NONE
            musicaConector.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
        } else {
            SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_ALL
            _modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_ALL
            musicaConector.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
        }
    }

    fun modoAleatorio() {
        if (_modoAleatorio.value == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
            SharedPreferenceUtil.modoAleatorio = PlaybackStateCompat.SHUFFLE_MODE_NONE
            _modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
            SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_ALEATORIO
            musicaConector.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        } else {
            SharedPreferenceUtil.modoAleatorio = PlaybackStateCompat.SHUFFLE_MODE_ALL
            _modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
            musicaConector.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
        }
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

    fun deletarMusicaDispositivo(
        musica: Musica,
        intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val uri: Uri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musica.id)
        val contentResolver = APP.getContext().contentResolver
        try {
            contentResolver.delete(uri, null, null)
        } catch (e: SecurityException) {
            val intentSender = when {
                VersaoUtil.androidR() -> {
                    MediaStore.createDeleteRequest(contentResolver, listOf(uri)).intentSender
                }

                VersaoUtil.androidQ() -> {
                    val recoverableSecurityException = e as? RecoverableSecurityException
                    recoverableSecurityException?.userAction?.actionIntent?.intentSender
                }

                else -> null
            }
            intentSender?.let { sender ->
                intentSenderLauncher.launch(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }
}