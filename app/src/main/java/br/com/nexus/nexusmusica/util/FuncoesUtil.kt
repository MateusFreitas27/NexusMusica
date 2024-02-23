package br.com.nexus.nexusmusica.util

import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import br.com.nexus.nexusmusica.modelo.Musica
import java.text.SimpleDateFormat
import java.util.Locale

object FuncoesUtil {
    fun carregarCapaMusica(uri: String): ByteArray?{
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val byte = retriever.embeddedPicture
        retriever.release()
        return byte
    }

    fun carregarCapaMusica(musica: Musica): Uri {
        val artWork = "content://media/external/audio/albumart".toUri()
        return ContentUris.withAppendedId(artWork, musica.albumId)
    }

    fun formatarDuracaoMusica(duracao: Long): String{
        val duracaoFormatada = SimpleDateFormat("mm:ss", Locale.getDefault())
        return  duracaoFormatada.format(duracao)
    }
}