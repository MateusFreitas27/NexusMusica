package br.com.nexus.nexusmusica.repositorio

import android.provider.MediaStore
import br.com.nexus.nexusmusica.helper.OrdemOrdenacao
import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import java.text.Collator

interface AlbumRepositorio{
    fun albums(): List<Album>
    fun albums(query: String): List<Album>
    fun album(albumId: Long): Album
}

class RealAlbumRepositorio(private val musicaRepositorio: RealMusicaRepositorio): AlbumRepositorio {
    override fun albums(): List<Album> {
        val musicas = musicaRepositorio.musicas(
            musicaRepositorio.montarCursor(null,null, getMusicaOrdemAlbum())
        )
        return separarAlbums(musicas)
    }

    override fun albums(query: String): List<Album> {
        val musicas = musicaRepositorio.musicas(
            musicaRepositorio.montarCursor(
                MediaStore.Audio.AudioColumns.ALBUM + " LIKE ?",
                arrayOf("%$query%"),
                getMusicaOrdemAlbum()
            )
        )
        return separarAlbums(musicas)
    }

    override fun album(albumId: Long): Album {
        val cursor = musicaRepositorio.montarCursor(
            MediaStore.Audio.AudioColumns.ALBUM_ID + "=?",
            arrayOf(albumId.toString()),
            getMusicaOrdemAlbum()
        )
        val musicas = musicaRepositorio.musicas(cursor)
        val album = Album(albumId, musicas)
        return ordenarAlbumMusicas(album)
    }

    private fun separarAlbums(musicas: List<Musica>, ordem: Boolean = true): List<Album> {
        val agrupado = musicas.groupBy { it.albumId }.map { Album(it.key, it.value) }
        if(!ordem) return agrupado
        val collator = Collator.getInstance()
        return when(SharedPreferenceUtil.modoOrdenacaoAlbum){
            OrdemOrdenacao.ALBUM_A_Z ->{
                agrupado.sortedWith{a1,a2 -> collator.compare(a1.titulo,a2.titulo)}
            }
            OrdemOrdenacao.ALBUM_Z_A -> {
                agrupado.sortedWith{a1,a2 -> collator.compare(a2.titulo,a1.titulo)}
            }
            OrdemOrdenacao.ALBUM_ARTISTA -> {
                agrupado.sortedWith{a1,a2 -> collator.compare(a1.artistaNome,a2.artistaNome)}
            }
            OrdemOrdenacao.ALBUM_NUMERO_MUSICAS -> {
                agrupado.sortedByDescending { it.musicasContadas }
            }
            else -> agrupado
        }
    }

    private fun ordenarAlbumMusicas(album: Album): Album {
        val collator = Collator.getInstance()
        val musicas = when(SharedPreferenceUtil.albumDetalheMusicaOrdemOrdenacao){
            OrdemOrdenacao.LISTA_FAIXAS_MUSICAS -> album.musicas.sortedWith{ a1, a2 -> a1.numeroFaixa.compareTo(a2.numeroFaixa)}
            OrdemOrdenacao.ALBUM_A_Z -> {
                album.musicas.sortedWith{a1,a2 -> collator.compare(a1.titulo,a2.titulo)}
            }
            OrdemOrdenacao.ALBUM_Z_A ->{
                album.musicas.sortedWith{a1,a2 -> collator.compare(a2.titulo, a1.titulo)}
            }
            OrdemOrdenacao.MUSICA_DURACAO ->{
                album.musicas.sortedWith{a1,a2 -> a1.duracao.compareTo(a2.duracao)}
            }
            else ->throw IllegalStateException("Invalido ${SharedPreferenceUtil.albumDetalheMusicaOrdemOrdenacao}")
        }
        return album.copy(musicas = musicas)
    }

    private fun getMusicaOrdemAlbum(): String {
        var ordenacaoAlbum = SharedPreferenceUtil.modoOrdenacaoAlbum
        if (ordenacaoAlbum  == OrdemOrdenacao.ALBUM_NUMERO_MUSICAS){
            ordenacaoAlbum = OrdemOrdenacao.ALBUM_A_Z
        }
        return ordenacaoAlbum + ", " + SharedPreferenceUtil.modoOrdenacaoAlbum
    }
}