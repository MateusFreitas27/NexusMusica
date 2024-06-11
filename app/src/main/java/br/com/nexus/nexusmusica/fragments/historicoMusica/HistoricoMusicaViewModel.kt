package br.com.nexus.nexusmusica.fragments.historicoMusica

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricoMusicaViewModel(private val repositorio: Repositorio): ViewModel() {
    var listaMusica: MutableLiveData<MutableList<Musica>> = MutableLiveData<MutableList<Musica>>()

    fun carregarListaHistorico(){
        CoroutineScope(Dispatchers.IO).launch {
            val listaHistorico = repositorio.listaHistorico()
            withContext(Dispatchers.Main){
                listaMusica.value = listaHistorico.toMutableList()
            }
        }
    }
}