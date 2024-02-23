package br.com.nexus.nexusmusica.util

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.edit
import br.com.nexus.nexusmusica.ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO
import br.com.nexus.nexusmusica.ALBUM_ORDENADO
import br.com.nexus.nexusmusica.APP
import br.com.nexus.nexusmusica.FILTRO_TAMANHO
import br.com.nexus.nexusmusica.ID_ALBUM_FILTRO
import br.com.nexus.nexusmusica.INTERVALO_MUSICAS
import br.com.nexus.nexusmusica.MODO_ALEATORIO
import br.com.nexus.nexusmusica.MODO_REPETIR_MUSICA
import br.com.nexus.nexusmusica.MODO_REPRO_ANTERIOR_ALEATORIO
import br.com.nexus.nexusmusica.MODO_REPRO_PLAYER
import br.com.nexus.nexusmusica.MUSICA_ORDENADO
import br.com.nexus.nexusmusica.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.VELOCIDADE_MEDIA
import br.com.nexus.nexusmusica.helper.OrdemOrdenacao

object SharedPreferenceUtil {
    private var contextShared = APP.getContext()

    var modoOrdenacaoAlbum
        get() = contextShared.getSharedPreferences(ALBUM_ORDENADO, Context.MODE_PRIVATE).getString(
            ALBUM_ORDENADO, OrdemOrdenacao.ALBUM_A_Z)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(ALBUM_ORDENADO, Context.MODE_PRIVATE)
            sharedPreferences.edit{putString(ALBUM_ORDENADO, value)}
        }

    var albumDetalheMusicaOrdemOrdenacao
        get() = contextShared.getSharedPreferences(ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO,Context.MODE_PRIVATE).getString(
            ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO, OrdemOrdenacao.LISTA_FAIXAS_MUSICAS)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(
                ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO, value) }
        }

    var tamanhoMusicaFiltro
        get() = contextShared.getSharedPreferences(FILTRO_TAMANHO,Context.MODE_PRIVATE).getInt(
            FILTRO_TAMANHO, 20)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(FILTRO_TAMANHO, Context.MODE_PRIVATE)
            sharedPreferences.edit { putInt(FILTRO_TAMANHO, value) }
        }

    var modoOrdenacaoMusica
        get() = contextShared.getSharedPreferences(MUSICA_ORDENADO, Context.MODE_PRIVATE).getString(
            MUSICA_ORDENADO, OrdemOrdenacao.MUSICA_A_Z)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MUSICA_ORDENADO, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(MUSICA_ORDENADO, value) }
        }

    val intervaloAdicao: Long
        get() {
            val calendario = CalendarioUtil()
            val intervalo =
                when(contextShared.getSharedPreferences(INTERVALO_MUSICAS, Context.MODE_PRIVATE).getString(
                    INTERVALO_MUSICAS, "este_ano")){
                    "hoje" -> calendario.tempoDecorrido
                    "esta_semana" -> calendario.semanaDecorrido
                    "tres_mes_anterior" -> calendario.tempoDecorridoMes(3)
                    "este_ano" -> calendario.anoDecorrido
                    "este_mes" -> calendario.mesDecorrido
                    else -> calendario.mesDecorrido
                }
            return (System.currentTimeMillis() - intervalo) / 1000
        }
    var modoRepeticaoMusica
        get() = contextShared.getSharedPreferences(MODO_REPETIR_MUSICA, Context.MODE_PRIVATE).getInt(
            MODO_REPETIR_MUSICA, PlaybackStateCompat.REPEAT_MODE_NONE
        )
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MODO_REPETIR_MUSICA, Context.MODE_PRIVATE)
            sharedPreferences.edit { putInt(MODO_REPETIR_MUSICA, value) }
        }
    var modoAleatorio: Int
        get() = contextShared.getSharedPreferences(MODO_ALEATORIO, Context.MODE_PRIVATE).getInt(
            MODO_ALEATORIO, 0
        )
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MODO_ALEATORIO, Context.MODE_PRIVATE)
            sharedPreferences.edit{putInt(MODO_ALEATORIO, value)}
        }
    var modoReproducaoPlayer
        get() = contextShared.getSharedPreferences(MODO_REPRO_PLAYER, Context.MODE_PRIVATE).getString(
            MODO_REPRO_PLAYER, REPRODUCAO_MUSICAS
        )
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MODO_REPRO_PLAYER, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(MODO_REPRO_PLAYER, value) }
        }
    var modoReproducaoPlayerAnterior
        get() = contextShared.getSharedPreferences(MODO_REPRO_ANTERIOR_ALEATORIO, Context.MODE_PRIVATE).getString(
            MODO_REPRO_ANTERIOR_ALEATORIO, REPRODUCAO_MUSICAS
        )
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MODO_REPRO_ANTERIOR_ALEATORIO, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(MODO_REPRO_ANTERIOR_ALEATORIO, value) }
        }

    var idAlbumMusica
        get() = contextShared.getSharedPreferences(ID_ALBUM_FILTRO, Context.MODE_PRIVATE).getLong(
            ID_ALBUM_FILTRO, 0
        )
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(ID_ALBUM_FILTRO, Context.MODE_PRIVATE)
            sharedPreferences.edit{putLong(ID_ALBUM_FILTRO, value)}
        }
    var velocidadeReproducaoMedia
        get() = contextShared.getSharedPreferences(VELOCIDADE_MEDIA, Context.MODE_PRIVATE).getFloat(
            VELOCIDADE_MEDIA, 1.0f)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(VELOCIDADE_MEDIA, Context.MODE_PRIVATE)
            sharedPreferences.edit { putFloat(VELOCIDADE_MEDIA, value) }
        }
}