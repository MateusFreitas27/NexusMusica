package br.com.nexus.nexusmusica.ui.fragments.miniPlayerBottom

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.RealMusicaRepositorio
import br.com.nexus.nexusmusica.services.MusicaConector
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiniPlayerBottomFragmentViewModel(
    private val musicaConector: MusicaConector,
    private val musicaRepositorio: RealMusicaRepositorio
): ViewModel() {
    val plabackState = musicaConector.playbackState
    //val conector = musicaConector.conectado
    val infoMusicaTocando = musicaConector.infoMusicaTocando
    val nomeMusica: MutableLiveData<String> = MutableLiveData<String>()
    val nomeAlbum: MutableLiveData<String> = MutableLiveData<String>()
    val capaMusica: MutableLiveData<String> = MutableLiveData<String>()
    var musica: Musica = MusicaVazia
    var recomecaReproducao: Boolean = true

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
            musica = musicaRepositorio.musica(media!!.description!!.mediaId!!.toLong())
            carregarDadosTela()
        }
    }

    fun retomarReproducao() {
        if (recomecaReproducao){
            musicaConector.transportControls.playFromMediaId(musica.id.toString(),null)
            recomecaReproducao = false
        } else {
            if (plabackState.value?.state == 3){
                musicaConector.transportControls.pause()
            }else {
                musicaConector.transportControls.play()
            }
        }
    }
}