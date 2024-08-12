package br.com.nexus.nexusmusica.repositorio

import br.com.nexus.nexusmusica.room.HistoricoDao
import br.com.nexus.nexusmusica.room.HistoricoEntity

interface RoomRepository {
    fun listaHistorico(): List<HistoricoEntity>
    fun SalvarHistorico(musica: HistoricoEntity)
    fun consultarMusica(idMusica: Long): HistoricoEntity?
    fun listarMaisOuvidas(): List<HistoricoEntity>
    fun apagarMediaHistorico(musica: HistoricoEntity)
}

class RealRoomRepositorio(
    private val historicoDao: HistoricoDao
): RoomRepository{
    override fun listaHistorico(): List<HistoricoEntity> {
        return historicoDao.recuperarHistorico()
    }

    override fun SalvarHistorico(musica: HistoricoEntity) {
        historicoDao.inserirAtualizarHistorico(musica)
    }

    override fun consultarMusica(idMusica: Long): HistoricoEntity? {
        return historicoDao.consultarMusicaHistorico(idMusica)
    }

    override fun listarMaisOuvidas(): List<HistoricoEntity> {
        return historicoDao.recuperarHistorico()
    }

    override fun apagarMediaHistorico(musica: HistoricoEntity) {
        historicoDao.deletarHistorico(musica)
    }
}