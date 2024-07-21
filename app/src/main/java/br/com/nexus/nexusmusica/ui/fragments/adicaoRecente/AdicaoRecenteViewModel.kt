package br.com.nexus.nexusmusica.ui.fragments.adicaoRecente

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.REPRODUCAO_ADICOES_RECENTES
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdicaoRecenteViewModel(private val repositorio: Repositorio): ViewModel() {
    private val _listaMusicarecente = MutableLiveData<List<Musica>>()
    val listaMusicarecente: LiveData<List<Musica>> = _listaMusicarecente

    fun carregarListaMusicasRecentes(){
        CoroutineScope(Dispatchers.IO).launch {
            val listaMusicas =repositorio.listaMusicasRecentes()
            withContext(Dispatchers.Main){
                _listaMusicarecente.value = listaMusicas
            }
        }
    }

    fun irParaReproducaoMusica(findNavController: NavController, musica: Musica) {
        SharedPreferenceUtil.modoReproducaoPlayerAnterior = SharedPreferenceUtil.modoReproducaoPlayer
        SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_ADICOES_RECENTES
        val action =
            AdicaoRecenteFragmentDirections.actionListaMusicasRecentesFragmentToPlayerMusicaFragment()
        findNavController.navigate(action)
    }
}