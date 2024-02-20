package br.com.nexus.nexusmusica.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.core.graphics.drawable.toBitmap
import br.com.nexus.nexusmusica.APP
import br.com.nexus.nexusmusica.R
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

    fun formatarDuracaoMusica(duracao: Long): String{
        val duracaoFormatada = SimpleDateFormat("mm:ss", Locale.getDefault())
        return  duracaoFormatada.format(duracao)
    }
}