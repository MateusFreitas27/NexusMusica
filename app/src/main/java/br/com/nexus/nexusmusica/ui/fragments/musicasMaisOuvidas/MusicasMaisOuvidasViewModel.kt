package br.com.nexus.nexusmusica.ui.fragments.musicasMaisOuvidas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.nexus.nexusmusica.modelo.HistoricoMusica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicasMaisOuvidasViewModel(private val repositorio: Repositorio): ViewModel() {
    var listaMusica: MutableLiveData<MutableList<HistoricoMusica>> = MutableLiveData<MutableList<HistoricoMusica>>()

    fun listaMusicaMaisTocadas(){
        CoroutineScope(Dispatchers.IO).launch {
            val lista = repositorio.listarMaisOuvidas()
            lista.forEach {
                Log.d("ouvidas", "${it.nomeMusica}: ${it.qtdVezesTocadas}")
            }
        }
    }
}