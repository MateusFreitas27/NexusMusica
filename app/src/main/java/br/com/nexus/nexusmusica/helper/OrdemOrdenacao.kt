package br.com.nexus.nexusmusica.helper

import android.provider.MediaStore

object OrdemOrdenacao {
    const val MUSICA_DEFAULT = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    const val MUSICA_A_Z = MediaStore.Audio.Media.TITLE
    const val MUSICA_Z_A = "$MUSICA_A_Z DESC"
    const val MUSICA_ARTISTA = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
    const val COMPOSICAO = MediaStore.Audio.Media.COMPOSER
    const val MUSICA_DURACAO = MediaStore.Audio.Media.DURATION + " DESC"

    const val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
    const val ALBUM_Z_A = "$ALBUM_A_Z DESC"
    const val ALBUM_NUMERO_MUSICAS = MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS + " DESC"
    const val ALBUM_ARTISTA = "case when lower(album_artist) is null then 1 else 0 end, lower(album_artist)"
    const val ALBUM_ANO = MediaStore.Audio.Media.YEAR + " DESC"
    const val LISTA_FAIXAS_MUSICAS = (MediaStore.Audio.Media.TRACK + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
}