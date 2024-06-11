package br.com.nexus.nexusmusica.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.nexus.nexusmusica.modelo.HistoricoMusica
import br.com.nexus.nexusmusica.modelo.Musica

@Entity(tableName = "historico_musica")
data class HistoricoEntity (
    @PrimaryKey
    var id: Long = 0,
    var nomeMusica: String = "",
    var numeroFaixa: Int = 0,
    var ano: Int = 0,
    var duracao: Long = 0,
    var data: String = "",
    var dataModificacao: Long = 0,
    var dataAdicao: Long = 0,
    var idAlbum: Long = 0,
    var nomeAlbum: String = "",
    var idArtista: Long = 0,
    var nomeArtista: String = "",
    var composicao: String?,
    var albumArtista: String?,
    var qtdVezesTocadas: Int = 0
)

fun HistoricoMusica.toHistoricoEntity(): HistoricoEntity{
    return with(this){
        HistoricoEntity(
            id = this.id,
            nomeMusica = this.nomeMusica,
            numeroFaixa = this.numeroFaixa,
            ano = this.ano,
            duracao = this.duracao,
            data = this.data,
            dataModificacao = this.dataModificacao,
            dataAdicao = this.dataAdicao,
            idAlbum = this.idAlbum,
            nomeAlbum = this.nomeAlbum,
            idArtista = this.idArtista,
            nomeArtista = this.nomeArtista,
            composicao = this.composicao,
            albumArtista = this.albumArtista,
            qtdVezesTocadas = this.qtdVezesTocadas
        )
    }
}

fun HistoricoEntity.toHistoricoMusica(): HistoricoMusica{
    return HistoricoMusica (
        id = this.id,
        nomeMusica = this.nomeMusica,
        numeroFaixa = this.numeroFaixa,
        ano = this.ano,
        duracao = this.duracao,
        data = this.data,
        dataModificacao = this.dataModificacao,
        dataAdicao = this.dataAdicao,
        idAlbum = this.idAlbum,
        nomeAlbum = this.nomeAlbum,
        idArtista = this.idArtista,
        nomeArtista = this.nomeArtista,
        composicao = this.composicao,
        albumArtista = this.albumArtista,
        qtdVezesTocadas = this.qtdVezesTocadas
    )
}

fun HistoricoEntity.toMusica(): Musica{
    return Musica (
        id = this.id,
        nomeMusica = this.nomeMusica,
        numeroFaixa = this.numeroFaixa,
        ano = this.ano,
        duracao = this.duracao,
        data = this.data,
        dataModificacao = this.dataModificacao,
        dataAdicao = this.dataAdicao,
        idAlbum = this.idAlbum,
        nomeAlbum = this.nomeAlbum,
        idArtista = this.idArtista,
        nomeArtista = this.nomeArtista,
        composicao = this.composicao,
        albumArtista = this.albumArtista
    )
}

fun Musica.toHistoricoMusica(): HistoricoMusica{
    return HistoricoMusica(
        id = this.id,
        nomeMusica = this.nomeMusica,
        numeroFaixa = this.numeroFaixa,
        ano = this.ano,
        duracao = this.duracao,
        data = this.data,
        dataModificacao = this.dataModificacao,
        dataAdicao = this.dataAdicao,
        idAlbum = this.idAlbum,
        nomeAlbum = this.nomeAlbum,
        idArtista = this.idArtista,
        nomeArtista = this.nomeArtista,
        composicao = this.composicao,
        albumArtista = this.albumArtista,
        qtdVezesTocadas = 1
    )
}