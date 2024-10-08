package br.com.nexus.nexusmusica.modelo

import android.os.Parcel
import android.os.Parcelable

class Musica(
    val id: Long,
    val nomeMusica: String,
    val numeroFaixa: Int,
    val ano: Int,
    val duracao: Long,
    val data: String,
    val dataModificacao: Long,
    val dataAdicao: Long,
    val idAlbum: Long,
    val nomeAlbum: String,
    val idArtista: Long,
    val nomeArtista: String,
    val composicao: String?,
    val albumArtista: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(nomeMusica)
        parcel.writeInt(numeroFaixa)
        parcel.writeInt(ano)
        parcel.writeLong(duracao)
        parcel.writeString(data)
        parcel.writeLong(dataModificacao)
        parcel.writeLong(dataAdicao)
        parcel.writeLong(idAlbum)
        parcel.writeString(nomeAlbum)
        parcel.writeLong(idArtista)
        parcel.writeString(nomeArtista)
        parcel.writeString(composicao)
        parcel.writeString(albumArtista)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Musica> {
        override fun createFromParcel(parcel: Parcel): Musica {
            return Musica(parcel)
        }

        override fun newArray(size: Int): Array<Musica?> {
            return arrayOfNulls(size)
        }
    }
}