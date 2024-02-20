package br.com.nexus.nexusmusica.modelo

import android.os.Parcel
import android.os.Parcelable

class Musica(
    val id: Long,
    val titulo: String,
    val numeroFaixa: Int,
    val ano: Int,
    val duracao: Long,
    val data: String,
    val dataModificacao: Long,
    val dataAdicao: Long,
    val albumId: Long,
    val albumNome: String,
    val artistaId: Long,
    val artistaNome: String,
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
        parcel.writeString(titulo)
        parcel.writeInt(numeroFaixa)
        parcel.writeInt(ano)
        parcel.writeLong(duracao)
        parcel.writeString(data)
        parcel.writeLong(dataModificacao)
        parcel.writeLong(dataAdicao)
        parcel.writeLong(albumId)
        parcel.writeString(albumNome)
        parcel.writeLong(artistaId)
        parcel.writeString(artistaNome)
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