package br.com.nexus.nexusmusica.services

import android.content.pm.ServiceInfo
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import br.com.nexus.nexusmusica.NOTIFICACAO_MUSICA_CANAL_ID
import br.com.nexus.nexusmusica.NOTIFICACAO_MUSICA_ID
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.util.VersaoUtil

object Notificacao {
    fun criarNotificacao(musicaService: MusicaService, mediaSession: MediaSessionCompat.Token){
        val notification = NotificationCompat.Builder(musicaService, NOTIFICACAO_MUSICA_CANAL_ID)
            .setSmallIcon(R.drawable.icon_play)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession))
            .build()
        musicaService.apply {
            if (VersaoUtil.androidQ()) {
                startForeground(
                    NOTIFICACAO_MUSICA_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(NOTIFICACAO_MUSICA_ID, notification)
            }
        }
    }
}