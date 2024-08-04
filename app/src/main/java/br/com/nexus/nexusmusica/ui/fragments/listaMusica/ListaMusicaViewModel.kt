package br.com.nexus.nexusmusica.ui.fragments.listaMusica

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.di.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaMusicaViewModel(
    private val repositorio: Repositorio,
    private val playerControle: PlayerControle
) : ViewModel() {
    private val _listaMusicaLiveData = MutableLiveData<MutableList<Musica>>()
    val listaMusicas: LiveData<MutableList<Musica>> = _listaMusicaLiveData

    fun carregarListaMusica() {
        CoroutineScope(Dispatchers.IO).launch {
            val listaMusicas = repositorio.listaMusicas()
            withContext(Dispatchers.Main) {
                _listaMusicaLiveData.value = listaMusicas.toMutableList()
            }

        }
    }

    fun abrirPlayerMusica(findNavController: NavController, musica: Musica) {
        SharedPreferenceUtil.modoReproducaoPlayerAnterior =
            SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_MUSICAS
        playerControle.iniciarReproducao(musica)
        val action =
            ListaMusicaFragmentDirections.actionMenuItemMusicaToPlayerMusicaFragment()
        findNavController.navigate(action)
    }

}