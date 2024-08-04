package br.com.nexus.nexusmusica.ui.fragments.detalheAlbum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.di.REPRODUCAO_ALBUM
import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil

class DetalheAlbumFragmentViewModel: ViewModel() {
    var infoalbum: Album = Album(0, listOf())
    var capaAlbum: String = ""
    var totalDuracaoAlbum: Long = 0
    private val _listaMusica = MutableLiveData<List<Musica>>()
    val listaMusica: LiveData<List<Musica>> = _listaMusica

    fun setAlbum(args: DetalheAlbumFragmentArgs) {
        infoalbum = args.album
        capaAlbum = infoalbum.musicas[0].data
        _listaMusica.value = infoalbum.musicas
        for (musica in infoalbum.musicas){
            totalDuracaoAlbum += musica.duracao
        }
    }

    fun abrirTelaPlayer(navController: NavController, musica: Musica) {
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_ALBUM
        SharedPreferenceUtil.modoReproducaoPlayerAnterior = SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.idAlbumMusica = musica.idAlbum
        val action =
            DetalheAlbumFragmentDirections.actionDetalheAlbumFragmentToPlayerMusicaFragment()
        navController.navigate(action)
    }
}