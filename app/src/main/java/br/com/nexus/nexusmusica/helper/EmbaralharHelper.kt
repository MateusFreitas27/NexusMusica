package br.com.nexus.nexusmusica.helper

import android.support.v4.media.MediaBrowserCompat
import br.com.nexus.nexusmusica.modelo.Musica

object EmbaralharHelper {
    fun embaralharLista(lista: MutableList<Musica>, posicao: Int ){
        val musicaInicial = lista[posicao]
        lista.shuffle()
        val posicaoTroca = lista.indexOfFirst { it.id == musicaInicial.id }
        val musicaTroca = lista[posicaoTroca]
        lista[0] = musicaInicial
        lista[posicaoTroca] = musicaTroca
    }
}