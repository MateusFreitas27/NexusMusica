package br.com.nexus.nexusmusica.helper

import android.support.v4.media.MediaBrowserCompat

object EmbaralharHelper {
    fun embaralharLista(lista: MutableList<MediaBrowserCompat.MediaItem>, posicao: Int ){
        val musicaInicial = lista[posicao]
        lista.shuffle()
        val posicaoTroca = lista.indexOfFirst { it.description.mediaId == musicaInicial.description.mediaId }
        val musicaTroca = lista[posicaoTroca]
        lista[0] = musicaInicial
        lista[posicaoTroca] = musicaTroca
    }
}