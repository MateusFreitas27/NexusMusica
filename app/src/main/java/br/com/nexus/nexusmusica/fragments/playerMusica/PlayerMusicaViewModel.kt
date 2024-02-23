package br.com.nexus.nexusmusica.fragments.playerMusica

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.nexus.nexusmusica.DELAY_INTERVALO_PLAYER_POSICAO
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.REPRODUCAO_ALEATORIO
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.services.MusicaConector
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerMusicaViewModel(private val musicaConector: MusicaConector): ViewModel() {
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
    private var _listaMusicas: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = MutableLiveData()
    val listaMusica: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = _listaMusicas
    private val _tocandoMusica: MutableLiveData<Int> = MutableLiveData<Int>()
    val tocandoMusica: MutableLiveData<Int> = _tocandoMusica
    private var id: String = ""
    private var alterarInfoMusica = false

    private val subcribeCallback: SubscriptionCallback = object : SubscriptionCallback(){
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
            while (true){
                _tocandoMusica.value = playbackState.value?.state
                val pos = playbackState.value?.position
                if (_progressoMusica.value != pos){
                    _progressoMusica.value = pos!!
                }
                delay(DELAY_INTERVALO_PLAYER_POSICAO)
            }
        }
    }

    fun setMusica(args: PlayerMusicaFragmentArgs) {
        val musica = args.musica
        id = musica.id.toString()
        _duracaoMusica.value = musica.duracao.toInt()
        _nomeMusica.value = musica.titulo
        _nomeAlbum.value = musica.albumNome
        _imgCapa.value = FuncoesUtil.carregarCapaMusica(musica.data)
    }

    fun iniciar(){
       musicaConector.transportControls.playFromMediaId(id,null)
    }

    fun playPlause(){
        if (playbackState.value?.state == 3){
            musicaConector.transportControls.pause()
        }else{
            musicaConector.transportControls.play()
        }
    }

    fun proximaMusica(){
        musicaConector.transportControls.skipToNext()
    }

    fun musicaAnterior(){
        musicaConector.transportControls.skipToPrevious()
    }

    fun carregarDadosMusica(media: MediaMetadataCompat?){
        if (media?.description?.mediaId != id && alterarInfoMusica){
            _duracaoMusica.value =
                media!!.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
            _nomeMusica.value = media.description?.title.toString()
            _nomeAlbum.value = media.description?.subtitle.toString()
            _imgCapa.value = FuncoesUtil.carregarCapaMusica(media.description?.mediaUri.toString())
        } else alterarInfoMusica = true

    }

    fun seekToMusica(posicao: Long) {
        musicaConector.transportControls.seekTo(posicao)
    }

    fun alterarVelocidadePlayer(valorSlider: Float) {
        musicaConector.transportControls.setPlaybackSpeed(valorSlider)
    }


    fun trocarModorepetirMusica() {
        when(_modoRepeticao.value){
            PlaybackStateCompat.REPEAT_MODE_NONE -> {
                SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_ALL
                _modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_ALL
                musicaConector.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
            }
            PlaybackStateCompat.REPEAT_MODE_ALL -> {
                SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_ONE
                _modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_ONE
                musicaConector.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
            }
            PlaybackStateCompat.REPEAT_MODE_ONE -> {
                SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_NONE
                _modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_NONE
                musicaConector.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
            }
        }
    }

    fun modoAleatorio() {
        if (_modoAleatorio.value == PlaybackStateCompat.SHUFFLE_MODE_ALL){
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
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> musicaConector.subcribe(REPRODUCAO_MUSICAS, subcribeCallback)
            REPRODUCAO_ALBUM -> musicaConector.subcribe(REPRODUCAO_ALBUM, subcribeCallback)
            REPRODUCAO_ADICOES_RECENTES -> musicaConector.subcribe(REPRODUCAO_ADICOES_RECENTES, subcribeCallback)
        }
    }

    override fun onCleared() {
        when(SharedPreferenceUtil.modoReproducaoPlayer){
            REPRODUCAO_MUSICAS -> musicaConector.unsubscribe(REPRODUCAO_MUSICAS, subcribeCallback)
            REPRODUCAO_ALBUM -> musicaConector.unsubscribe(REPRODUCAO_ALBUM, subcribeCallback)
            REPRODUCAO_ADICOES_RECENTES -> musicaConector.unsubscribe(REPRODUCAO_ADICOES_RECENTES, subcribeCallback)
        }
        super.onCleared()
    }
}