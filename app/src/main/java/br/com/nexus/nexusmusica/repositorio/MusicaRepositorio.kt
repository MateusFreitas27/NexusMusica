package br.com.nexus.nexusmusica.repositorio

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import androidx.core.database.getStringOrNull
import br.com.nexus.nexusmusica.E_MUSICA
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.baseProjecao
import br.com.nexus.nexusmusica.helper.OrdemOrdenacao
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import br.com.nexus.nexusmusica.util.VersaoUtil
import java.text.Collator


interface MusicaRepositorio{
    fun musicas(): List<Musica>
    fun musicas(cursor: Cursor?): List<Musica>
    fun musicasOrdenadas(cursor: Cursor?): List<Musica>
    fun musicas(query: String): List<Musica>
    fun musica(cursor: Cursor?): Musica
    fun musica(musicaId: Long): Musica
}

class RealMusicaRepositorio(private val context: Context): MusicaRepositorio {
    override fun musicas(): List<Musica> {
        return musicasOrdenadas(montarCursor(null, null))
    }

    override fun musicas(cursor: Cursor?): List<Musica> {
        val musicas = arrayListOf<Musica>()
        if (cursor != null && cursor.moveToFirst()){
            do {
                musicas.add(recuperarInformacaoMusica(cursor))
            }while (cursor.moveToNext())
        }
        cursor?.close()
        return musicas
    }

    override fun musicasOrdenadas(cursor: Cursor?): List<Musica> {
        val collator = Collator.getInstance()
        val musicas = musicas(cursor)
        return when(SharedPreferenceUtil.modoOrdenacaoMusica){
            OrdemOrdenacao.MUSICA_A_Z -> {
                musicas.sortedWith{m1,m2 -> collator.compare(m1.titulo,m2.titulo)}
            }
            OrdemOrdenacao.MUSICA_Z_A ->{
                musicas.sortedWith{m1,m2 -> collator.compare(m2.titulo, m1.titulo)}
            }
            OrdemOrdenacao.ALBUM_A_Z -> {
                musicas.sortedWith{m1,m2 -> collator.compare(m1.albumNome, m2.albumNome)}
            }
            OrdemOrdenacao.MUSICA_ARTISTA -> {
                musicas.sortedWith{m1,m2 -> collator.compare(m1.artistaNome, m2.artistaNome)}
            }
            OrdemOrdenacao.COMPOSICAO -> {
                musicas.sortedWith{m1, m2 -> collator.compare(m1.composicao, m2.composicao)}
            }
            else -> musicas
        }
    }


    override fun musica(cursor: Cursor?): Musica {
        val musica: Musica = if(cursor != null && cursor.moveToFirst()){
            recuperarInformacaoMusica(cursor)
        }else{
            MusicaVazia
        }
        cursor?.close()
        return musica
    }

    override fun musicas(query: String): List<Musica> {
        return musicas(montarCursor(AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%")))
    }

    override fun musica(musicaId: Long): Musica {
        return musica(montarCursor(AudioColumns._ID + " =? ", arrayOf(musicaId.toString())))
    }

    fun montarCursor(selecao: String?, selecaoValores: Array<String>?, ordemOrdenacao: String = MediaStore.Audio.Media.TITLE, ignoraBlackList: Boolean = false): Cursor?{
        var selecaoFinal = selecao
        val selecaoValoresFinal = selecaoValores
        if(!ignoraBlackList){
            selecaoFinal = if (selecao != null && selecao.trim{it <= ' '} != ""){
                "$E_MUSICA AND $selecaoFinal"
            }else{
                E_MUSICA
            }
        }
        selecaoFinal = selecaoFinal + " AND " + MediaStore.Audio.Media.DURATION + ">= " + (SharedPreferenceUtil.tamanhoMusicaFiltro * 1000)
        val uri = if (VersaoUtil.androidQ()){
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        }else{
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        return try {
            context.contentResolver.query(uri, baseProjecao, selecaoFinal, selecaoValoresFinal, ordemOrdenacao)
        }catch (ex: SecurityException){
            return null
        }
    }

    private fun recuperarInformacaoMusica(cursor: Cursor): Musica {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID))
        val titulo = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
        val numMusica = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK))
        val anoMusica = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR))
        val duracao = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION))
        val caminhoMusica = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))
        val dataModificacao = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED))
        val dataAdicao = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_ADDED))
        val albumid = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
        val nomeAlbum = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))
        val artistaId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID))
        val nomeArtista = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))
        val composicao = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER))
        val albumArtista = "album_artista"
        return Musica(id, titulo, numMusica, anoMusica, duracao, caminhoMusica, dataModificacao, dataAdicao, albumid, nomeAlbum?: "", artistaId, nomeArtista ?:"", composicao, albumArtista)
    }

}