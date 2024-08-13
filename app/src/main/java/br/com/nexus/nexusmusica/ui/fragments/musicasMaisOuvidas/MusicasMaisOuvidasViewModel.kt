package br.com.nexus.nexusmusica.ui.fragments.musicasMaisOuvidas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.di.REPRODUCAO_MAIS_OUVIDAS
import br.com.nexus.nexusmusica.di.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.modelo.HistoricoMusica
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicasMaisOuvidasViewModel(
    private val repositorio: Repositorio,
    private val playerControle: PlayerControle
) : ViewModel() {
    var listaMusica: MutableLiveData<MutableList<Musica>> =
        MutableLiveData<MutableList<Musica>>()

    fun listaMusicaMaisTocadas() {
        CoroutineScope(Dispatchers.IO).launch {
            val lista = repositorio.listarMaisOuvidas()
            withContext(Dispatchers.Main) {
                listaMusica.value = lista.toMutableList()
            }
        }
    }

    fun abrirPlayer(navController: NavController, musica: Musica) {
        SharedPreferenceUtil.modoReproducaoPlayerAnterior =
            SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_MAIS_OUVIDAS
        playerControle.iniciarReproducao(musica)
        val action = MusicasMaisOuvidasFragmentDirections.actionMaisOuvidasFragmentToPlayerMusicaFragment()
        navController.navigate(action)
    }
}