package br.com.nexus.nexusmusica.fragments.listaMusica

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaMusicaViewModel(private val repositorio: Repositorio): ViewModel() {
    private val _listaMusicaLiveData = MutableLiveData<MutableList<Musica>>()
    val listaMusicas: LiveData<MutableList<Musica>> = _listaMusicaLiveData

    fun carregarListaMusica(){
        CoroutineScope(Dispatchers.IO).launch {
            val listaMusicas = repositorio.listaMusicas()
            withContext(Dispatchers.Main){
                _listaMusicaLiveData.value = listaMusicas.toMutableList()
            }

        }
    }

    fun abrirPlayerMusica(findNavController: NavController, musica: Musica) {
        SharedPreferenceUtil.modoReproducaoPlayerAnterior = SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_MUSICAS
        val action = ListaMusicaFragmentDirections.actionMenuItemMusicaToPlayerMusicaFragment(musica, true)
        findNavController.navigate(action)
    }

}