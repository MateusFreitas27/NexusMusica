package br.com.nexus.nexusmusica.ui.fragments.historicoMusica

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.di.REPRODUCAO_HISTORICO
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricoMusicaViewModel(
    private val repositorio: Repositorio,
    private val playerControle: PlayerControle
): ViewModel() {
    var listaMusica: MutableLiveData<MutableList<Musica>> = MutableLiveData<MutableList<Musica>>()

    fun carregarListaHistorico(){
        CoroutineScope(Dispatchers.IO).launch {
            val listaHistorico = repositorio.listaHistorico()
            withContext(Dispatchers.Main){
                listaMusica.value = listaHistorico.toMutableList()
            }
        }
    }

    fun abrirPlayerMusica(findNavController: NavController, musica: Musica){
        SharedPreferenceUtil.modoReproducaoPlayerAnterior = SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_HISTORICO
        playerControle.iniciarReproducao(musica)
        val action = HistoricoMusicaFragmentDirections.actionHistoricoMusicaFragmentToPlayerMusicaFragment()
        findNavController.navigate(action)
    }
}