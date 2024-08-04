package br.com.nexus.nexusmusica.ui.fragments.miniPlayerBottom

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.di.MusicaVazia
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiniPlayerBottomFragmentViewModel(
    private val playerControle: PlayerControle,
    private val musicaRepositorio: Repositorio
): ViewModel() {
    val plabackState = playerControle.playbackState
    val infoMusicaTocando = playerControle.musicaReproduzindo
    val nomeMusica: MutableLiveData<String> = MutableLiveData<String>()
    val nomeAlbum: MutableLiveData<String> = MutableLiveData<String>()
    val capaMusica: MutableLiveData<String> = MutableLiveData<String>()
    var musica: Musica = MusicaVazia

    fun init(){
        if (SharedPreferenceUtil.musicaTocando!!.isNotEmpty()){
            val gson = Gson()
            val json = SharedPreferenceUtil.musicaTocando
            musica = gson.fromJson(json, Musica::class.java)
            carregarDadosTela()
        }
    }

    private fun carregarDadosTela(){
        nomeMusica.value = musica.nomeMusica
        nomeAlbum.value = musica.nomeAlbum
        capaMusica.value = musica.data
    }

    fun buscarMusica(media: MediaMetadataCompat?) {
        CoroutineScope(Dispatchers.Main).launch{
            musica = musicaRepositorio.consultaMusica(media!!.description!!.mediaId!!.toLong())
            carregarDadosTela()
        }
    }

    fun retomarReproducao() {
        if (plabackState.value == null){
            playerControle.retomarReproducao(musica)
        } else {
            playerControle.playPause()
        }
    }
}