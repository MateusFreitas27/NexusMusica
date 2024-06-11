package br.com.nexus.nexusmusica.repositorio

import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.modelo.HistoricoMusica
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.room.toHistoricoEntity
import br.com.nexus.nexusmusica.room.toHistoricoMusica
import br.com.nexus.nexusmusica.room.toMusica

interface Repositorio {
    suspend fun listaMusicas(): List<Musica>
    suspend fun listaMusicasRecentes(): List<Musica>
    suspend fun listaTodosAlbums(): List<Album>
    suspend fun consultaMusica(id:Long): Musica
    suspend fun listaHistorico(): List<Musica>
    suspend fun salvarHistorico(musica: HistoricoMusica)
}

class RealRepositorio(
    private val musicaRepositorio: MusicaRepositorio,
    private val albumRepositorio: AlbumRepositorio,
    private val musicasRecentesRepositorio: MusicasRecentesRepositorio,
    private val roomRepositorio: RoomRepository
) : Repositorio {
    override suspend fun listaMusicas(): List<Musica> {
        return musicaRepositorio.musicas()
    }

    override suspend fun listaMusicasRecentes(): List<Musica> {
        return musicasRecentesRepositorio.musicasRecentes()
    }

    override suspend fun listaTodosAlbums(): List<Album> {
        return albumRepositorio.albums()
    }

    override suspend fun consultaMusica(id: Long): Musica {
        return musicaRepositorio.musica(id)
    }

    override suspend fun listaHistorico(): List<Musica> {
        val listaHistorico = roomRepositorio.listaHistorico()
        val listaMusica: MutableList<Musica> = arrayListOf()
        listaHistorico.forEach { historico ->
            listaMusica.add(historico.toMusica())
        }
        return listaMusica.toList()
    }

    override suspend fun salvarHistorico(musica: HistoricoMusica) {
        val musicaSalva = roomRepositorio.consultarMusica(musica.id)
        if (musicaSalva != null ) musica.qtdVezesTocadas += musicaSalva.qtdVezesTocadas
        else musica.qtdVezesTocadas = 1
        roomRepositorio.SalvarHistorico(musica.toHistoricoEntity())
    }
}