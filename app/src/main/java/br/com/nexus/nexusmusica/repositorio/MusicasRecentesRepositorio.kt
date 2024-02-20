package br.com.nexus.nexusmusica.repositorio

import android.database.Cursor
import android.provider.MediaStore
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil

interface MusicasRecentesRepositorio {
    fun musicasRecentes(): List<Musica>
}

class RealMusicasRecentesRepositorio(private val musicaRepositorio: RealMusicaRepositorio):
    MusicasRecentesRepositorio {
    override fun musicasRecentes(): List<Musica> {
        return musicaRepositorio.musicas(montarCursor())
    }

    private fun montarCursor(): Cursor? {
        val intervalo = SharedPreferenceUtil.intervaloAdicao
        return musicaRepositorio.montarCursor(MediaStore.Audio.Media.DATE_ADDED + ">?",
            arrayOf(intervalo.toString()),
            MediaStore.Audio.Media.DATE_ADDED + " DESC")
    }

}