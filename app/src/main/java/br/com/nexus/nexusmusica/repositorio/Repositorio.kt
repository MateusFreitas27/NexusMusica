package br.com.nexus.nexusmusica.repositorio

import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.modelo.Musica

interface Repositorio {
    suspend fun listaMusicas(): List<Musica>
    suspend fun listaMusicasRecentes(): List<Musica>
    suspend fun listaTodosAlbums(): List<Album>
    suspend fun consultaMusica(id:Long): Musica
}

class RealRepositorio(
    private val musicaRepositorio: MusicaRepositorio,
    private val albumRepositorio: AlbumRepositorio,
    private val musicasRecentesRepositorio: MusicasRecentesRepositorio
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
}