package br.com.nexus.nexusmusica.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface HistoricoDao {
    @Upsert
    fun inserirAtualizarHistorico(historicoEntity: HistoricoEntity)

    @Query("SELECT * FROM historico_musica")
    fun recuperarHistorico(): List<HistoricoEntity>

    @Query("SELECT * FROM historico_musica WHERE id = :idMusica")
    fun consultarMusicaHistorico(idMusica: Long): HistoricoEntity?

    @Delete
    fun deletarHistorico(historicoEntity: HistoricoEntity)
}