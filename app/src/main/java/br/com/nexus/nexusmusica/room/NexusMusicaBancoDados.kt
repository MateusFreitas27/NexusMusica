package br.com.nexus.nexusmusica.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [HistoricoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NexusMusicaBancoDados : RoomDatabase() {
    abstract fun historicoDao(): HistoricoDao

    companion object {

        @Volatile
        private var INSTANCE: NexusMusicaBancoDados? = null

        fun getInstance(context: Context): NexusMusicaBancoDados {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NexusMusicaBancoDados::class.java,
                        "passhash_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}