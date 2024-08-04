package br.com.nexus.nexusmusica.util

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.edit
import br.com.nexus.nexusmusica.di.ALBUM_DETALHE_MUSICA_ORDEM_ORDENACAO
import br.com.nexus.nexusmusica.di.ALBUM_ORDENADO
import br.com.nexus.nexusmusica.APP
import br.com.nexus.nexusmusica.di.FILTRO_TAMANHO
import br.com.nexus.nexusmusica.di.ID_ALBUM_FILTRO
import br.com.nexus.nexusmusica.di.INTERVALO_MUSICAS
import br.com.nexus.nexusmusica.di.LISTA_REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.di.MODO_ALEATORIO
import br.com.nexus.nexusmusica.di.MODO_REPETIR_MUSICA
import br.com.nexus.nexusmusica.di.MODO_REPRO_ANTERIOR_ALEATORIO
import br.com.nexus.nexusmusica.di.MODO_REPRO_PLAYER
import br.com.nexus.nexusmusica.di.MUSICA_ORDENADO
import br.com.nexus.nexusmusica.di.MUSICA_TOCANDO
import br.com.nexus.nexusmusica.di.POSICAO_MUSICA_LISTA_PLAYER
import br.com.nexus.nexusmusica.di.REPRODUCAO_MUSICAS
import br.com.nexus.nexusmusica.di.TEMPO_EXECUCAO_MUSICA
import br.com.nexus.nexusmusica.di.VELOCIDADE_MEDIA
import br.com.nexus.nexusmusica.helper.OrdemOrdenacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            MODO_ALEATORIO, PlaybackStateCompat.SHUFFLE_MODE_ALL
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

    var musicaTocando
        get() =  contextShared.getSharedPreferences(MUSICA_TOCANDO,Context.MODE_PRIVATE).getString(
        MUSICA_TOCANDO, "")
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(MUSICA_TOCANDO, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(MUSICA_TOCANDO, value) }
        }

    var posicaoReproducaoLista
        get() = contextShared.getSharedPreferences(POSICAO_MUSICA_LISTA_PLAYER,Context.MODE_PRIVATE).getInt(
            POSICAO_MUSICA_LISTA_PLAYER, -1)
        set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(POSICAO_MUSICA_LISTA_PLAYER, Context.MODE_PRIVATE)
            sharedPreferences.edit { putInt(POSICAO_MUSICA_LISTA_PLAYER, value) }
        }


    var tempoExecucaoMusica
        get() = contextShared.getSharedPreferences(TEMPO_EXECUCAO_MUSICA,Context.MODE_PRIVATE).getLong(
            TEMPO_EXECUCAO_MUSICA, -1)
        private set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(TEMPO_EXECUCAO_MUSICA, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putLong(TEMPO_EXECUCAO_MUSICA, value)
            }
        }

    var listaReproducao
        get() = contextShared.getSharedPreferences(LISTA_REPRODUCAO_MUSICAS,Context.MODE_PRIVATE).getString(
            LISTA_REPRODUCAO_MUSICAS, "")
        private set(value) {
            val sharedPreferences = contextShared.getSharedPreferences(LISTA_REPRODUCAO_MUSICAS, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putString(LISTA_REPRODUCAO_MUSICAS, value)
            }
        }

    suspend fun salvarTempoExecucao(tempo: Long){
        withContext(Dispatchers.IO){
            tempoExecucaoMusica = tempo
        }
    }

    suspend fun salvarListaReproducao(lista: String){
        withContext(Dispatchers.IO){
            listaReproducao = lista
        }
    }
}