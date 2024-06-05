package br.com.nexus.nexusmusica.modelo

import android.os.Parcel
import android.os.Parcelable

class HistoricoMusica(
    var id: Long,
    var nomeMusica: String,
    var numeroFaixa: Int,
    var ano: Int,
    var duracao: Long,
    var data: String,
    var dataModificacao: Long,
    var dataAdicao: Long,
    var idAlbum: Long,
    var nomeAlbum: String,
    var idArtista: Long,
    var nomeArtista: String,
    var composicao: String?,
    var albumArtista: String?,
    var qtdVezesTocadas: Int
) : Parcelable{
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
        parcel.readString(),
        parcel.readInt()
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
        parcel.writeInt(qtdVezesTocadas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HistoricoMusica> {
        override fun createFromParcel(parcel: Parcel): HistoricoMusica {
            return HistoricoMusica(parcel)
        }

        override fun newArray(size: Int): Array<HistoricoMusica?> {
            return arrayOfNulls(size)
        }
    }

}