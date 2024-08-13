package br.com.nexus.nexusmusica.di

import android.provider.BaseColumns
import android.provider.MediaStore
import br.com.nexus.nexusmusica.modelo.Musica

const val NOME_PARENT_ID_SERVICE = "PLAYER_MUSICA"
const val SERVICE_TAG = "MusicaService"
const val DELAY_INTERVALO_PLAYER_POSICAO = 100L
const val E_MUSICA = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"
const val ALBUM_DETALHE = "album_detalhe"
const val MUSICA_PLAYER = "musica_player"
const val REPRODUCAO_MUSICAS = "musicas"
const val REPRODUCAO_ALBUM = "album"
const val REPRODUCAO_ALEATORIO = "aleatorio"
const val REPRODUCAO_ADICOES_RECENTES = "adicoesRecentes"
const val REPRODUCAO_HISTORICO = "historico"
const val REPRODUCAO_MAIS_OUVIDAS = "maisOuvidas"
const val TAG_MODAL_LISTA_MUSICAS = "modal_lista_musica_player"
const val TAG_MODAL_VELOCIDADE_MEDIA = "modal_controle_velocidade_media"
val baseProjecao = arrayOf(
    BaseColumns._ID,
    MediaStore.Audio.AudioColumns.TITLE,
    MediaStore.Audio.AudioColumns.TRACK,
    MediaStore.Audio.AudioColumns.YEAR,
    MediaStore.Audio.AudioColumns.DURATION,
    MediaStore.Audio.AudioColumns.DATA,
    MediaStore.Audio.AudioColumns.DATE_MODIFIED,
    MediaStore.Audio.AudioColumns.ALBUM_ID,
    MediaStore.Audio.AudioColumns.ALBUM,
    MediaStore.Audio.AudioColumns.ARTIST_ID,
    MediaStore.Audio.AudioColumns.ARTIST,
    MediaStore.Audio.AudioColumns.COMPOSER,
    MediaStore.Audio.AudioColumns.DATE_ADDED
)
val MusicaVazia = Musica(
    -1,
    "",
    -1,
    -1,
    -1,
    "",
    -1,
    -1,
    -1,
    "",
    -1,
    "",
    "",
    ""
)

const val NOTIFICACAO_MUSICA_CANAL_ID = "dn12"
const val NOTIFICACAO_MUSICA_ID = 10011