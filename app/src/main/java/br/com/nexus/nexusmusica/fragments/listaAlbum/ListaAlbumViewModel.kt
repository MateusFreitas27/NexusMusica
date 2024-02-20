package br.com.nexus.nexusmusica.fragments.listaAlbum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.repositorio.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaAlbumViewModel(private val repositorio: Repositorio) : ViewModel() {
    private val _listaAlbums = MutableLiveData<List<Album>>()
    val listaAlbums: LiveData<List<Album>> = _listaAlbums

    fun carregarAlbums(){
        CoroutineScope(Dispatchers.IO).launch {
            val albums = repositorio.listaTodosAlbums()
            withContext(Dispatchers.Main){
                _listaAlbums.value = albums
            }

        }
    }

    fun abrirTelaDetalheAlbum(navController: NavController, album: Album) {
        val action = ListaAlbumFragmentDirections.actionMenuItemAlbumToDetalheAlbumFragment(album)
        navController.navigate(action)
    }


}