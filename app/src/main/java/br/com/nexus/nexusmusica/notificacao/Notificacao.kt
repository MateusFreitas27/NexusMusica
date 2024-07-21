package br.com.nexus.nexusmusica.notificacao

import android.content.pm.ServiceInfo
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import br.com.nexus.nexusmusica.NOTIFICACAO_MUSICA_CANAL_ID
import br.com.nexus.nexusmusica.NOTIFICACAO_MUSICA_ID
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.services.MusicaService
import br.com.nexus.nexusmusica.util.VersaoUtil

object Notificacao {
    fun criarNotificacao(musicaService: MusicaService, mediaSession: MediaSessionCompat.Token){
        val intent = NavDeepLinkBuilder(musicaService).apply {
            setGraph(R.navigation.nav_graph)
            setDestination(R.id.playerMusicaFragment)
        }
        val notification = NotificationCompat.Builder(musicaService, NOTIFICACAO_MUSICA_CANAL_ID)
            .setSmallIcon(R.drawable.icon_play)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession))
            .setContentIntent(intent.createPendingIntent())
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