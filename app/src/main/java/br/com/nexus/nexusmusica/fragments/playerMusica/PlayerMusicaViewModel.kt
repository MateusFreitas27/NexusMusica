package br.com.nexus.nexusmusica.fragments.playerMusica

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
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
import br.com.nexus.nexusmusica.util.Repeticao
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
    private var _modoRepeticao: MutableLiveData<String> = MutableLiveData<String>()
    val modoRepeticao: MutableLiveData<String> = _modoRepeticao
    private var _modoAleatorio: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val modoAleatorio: MutableLiveData<Boolean> = _modoAleatorio
    private var _listaMusicas: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = MutableLiveData()
    val listaMusica: MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>> = _listaMusicas
    private val _tocandoMusica: MutableLiveData<Int> = MutableLiveData<Int>()
    val tocandoMusica: MutableLiveData<Int> = _tocandoMusica

    private var id: String = ""

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
        id = args.id.toString()
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
        _duracaoMusica.value = media!!.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
        _nomeMusica.value = media.description?.title.toString()
        _nomeAlbum.value = media.description?.subtitle.toString()
        _imgCapa.value = FuncoesUtil.carregarCapaMusica(media.description?.mediaUri.toString())
    }

    fun seekToMusica(posicao: Long) {
        musicaConector.transportControls.seekTo(posicao)
    }

    fun alterarVelocidadePlayer(valorSlider: Float) {
        musicaConector.transportControls.setPlaybackSpeed(valorSlider)
    }


    fun trocarModorepetirMusica() {
        when(_modoRepeticao.value){
            Repeticao.DESATIVADO.toString() -> {
                SharedPreferenceUtil.modoRepeticaoMusica = Repeticao.TODAS.toString()
                _modoRepeticao.value = Repeticao.TODAS.toString()
                musicaConector.transportControls.setRepeatMode(Repeticao.TODAS.codigo)
            }
            Repeticao.TODAS.toString() -> {
                SharedPreferenceUtil.modoRepeticaoMusica = Repeticao.UMA.toString()
                _modoRepeticao.value = Repeticao.UMA.toString()
                musicaConector.transportControls.setRepeatMode(Repeticao.UMA.codigo)
            }
            Repeticao.UMA.toString() -> {
                SharedPreferenceUtil.modoRepeticaoMusica = Repeticao.DESATIVADO.toString()
                _modoRepeticao.value = Repeticao.DESATIVADO.toString()
                musicaConector.transportControls.setRepeatMode(Repeticao.DESATIVADO.codigo)
            }
        }
    }

    fun modoAleatorio() {
        if (_modoAleatorio.value == true){
            SharedPreferenceUtil.modoAleatorio = false
            _modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
            SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_ALEATORIO
        } else {
            SharedPreferenceUtil.modoAleatorio = true
            _modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
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