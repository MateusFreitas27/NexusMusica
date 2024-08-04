package br.com.nexus.nexusmusica.modelo

import android.os.Parcel
import android.os.Parcelable
import br.com.nexus.nexusmusica.di.MusicaVazia


class Album(val id: Long, val musicas: List<Musica>) : Parcelable {
    val titulo: String = obterPrimeiraMusicaSegura().nomeAlbum

    val artistaNome: String get() = obterPrimeiraMusicaSegura().nomeArtista

    val musicasContadas: Int get() = musicas.size

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.createTypedArrayList(Musica)!!
    ) {
    }

    fun obterPrimeiraMusicaSegura(): Musica {
        return musicas.firstOrNull() ?: MusicaVazia
    }

    fun copy(id: Long = this.id, musicas: List<Musica> = this.musicas) = Album(id,musicas)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeTypedList(musicas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}