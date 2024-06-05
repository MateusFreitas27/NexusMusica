package br.com.nexus.nexusmusica.util

import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
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
        return ContentUris.withAppendedId(artWork, musica.idAlbum)
    }

    fun formatarDuracaoMusica(duracao: Long): String{
        val duracaoFormatada = SimpleDateFormat("mm:ss", Locale.getDefault())
        return  duracaoFormatada.format(duracao)
    }
    fun formatarListaMusica(lista: List<Musica>): MutableList<MediaBrowserCompat.MediaItem>{
        val musicas: MutableList<MediaBrowserCompat.MediaItem> = arrayListOf()
        lista.forEach {
            val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
                .setMediaId(it.id.toString())
                .setMediaUri(it.data.toUri())
                .setTitle(it.nomeMusica)
                .setSubtitle(it.nomeArtista)
            musicas.add(MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE))
        }
        return musicas
    }
}