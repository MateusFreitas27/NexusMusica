package br.com.nexus.nexusmusica

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class APP: Application() {
    override fun onCreate() {
        super.onCreate()
        instancia = this@APP

        startKoin {
            androidContext(this@APP)
            modules(appModulos)
        }

        val nome = "Nexus Musica"
        val desc = "Notificação de musica"
        val importancia = NotificationManager.IMPORTANCE_LOW
        val canal = NotificationChannel(NOTIFICACAO_MUSICA_CANAL_ID, nome, importancia).apply {
            description = desc
        }

        val notificacaoManeger: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificacaoManeger.createNotificationChannel(canal)
    }

    companion object{
        private var instancia: APP? = null
        fun getContext(): APP {
            return instancia!!
        }
    }
}