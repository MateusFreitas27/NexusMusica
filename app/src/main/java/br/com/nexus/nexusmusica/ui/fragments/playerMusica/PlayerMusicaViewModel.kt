package br.com.nexus.nexusmusica.ui.fragments.playerMusica

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.APP
import br.com.nexus.nexusmusica.DELAY_INTERVALO_PLAYER_POSICAO
import br.com.nexus.nexusmusica.REPRODUCAO_ALEATORIO
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import br.com.nexus.nexusmusica.util.VersaoUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerMusicaViewModel(
   private val playerControle: PlayerControle,
    private val repositorio: Repositorio
) : ViewModel() {
    val plabackState = playerControle.playbackState
    val infoMusicaTocando = playerControle.musicaReproduzindo
    var media: MutableLiveData<Musica> = MutableLiveData()
    val progressoMusica: MutableLiveData<Long> = MutableLiveData()
    var modoRepeticao: MutableLiveData<Int> = MutableLiveData<Int>()
    var modoAleatorio: MutableLiveData<Int> = MutableLiveData<Int>()
    private var retomarReproducao: Boolean = false

    init {
        atualizaPosicaoMediaPlayer()
        inicializarModos()
        buscarDadosMusica()
    }

    private fun buscarDadosMusica(){
        if (SharedPreferenceUtil.musicaTocando!!.isNotEmpty()){
            val gson = Gson()
            val json = SharedPreferenceUtil.musicaTocando
            val musica = gson.fromJson(json, Musica::class.java)
            media.value = musica
            progressoMusica.value = SharedPreferenceUtil.tempoExecucaoMusica
        }
    }

    private fun inicializarModos() {
        modoRepeticao.value = SharedPreferenceUtil.modoRepeticaoMusica
        modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
    }

    private fun atualizaPosicaoMediaPlayer() {
        viewModelScope.launch {
            while (true) {
                val pos = playerControle.tempoExecucao()
                if (progressoMusica.value != pos){
                    progressoMusica.value = pos ?: SharedPreferenceUtil.tempoExecucaoMusica
                }
                delay(DELAY_INTERVALO_PLAYER_POSICAO)
            }
        }
    }

    fun carregarDadosMusica(infoMedia: MediaMetadataCompat?) {
        CoroutineScope(Dispatchers.IO).launch {
            val musica =
                repositorio.consultaMusica(infoMedia!!.description!!.mediaId!!.toLong())
            withContext(Dispatchers.Main) {
                media.value = musica
            }
        }
    }

    fun playPlause() {
        if (plabackState.value == null){
            playerControle.retomarReproducao(media.value!!)
            retomarReproducao = false
        } else {
            playerControle.playPause()
        }
    }

    fun proximaMusica() {
        playerControle.proxima()
    }

    fun musicaAnterior() {
        playerControle.anterior()
    }

    fun seekToMusica(posicao: Long) {
        playerControle.alterarTempo(posicao)
    }

    fun alterarVelocidadePlayer(valorSlider: Float) {
        playerControle.alterarVelocidadeReproducao(valorSlider)
    }

    fun removerMusicaListaReproducao(media: MediaMetadataCompat?) {
        playerControle.removerListaReproducao(media?.description)
    }

    fun trocarModorepetirMusica() {
        if (modoRepeticao.value == PlaybackStateCompat.REPEAT_MODE_ALL) {
            SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_NONE
            modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_NONE
            playerControle.modoRepeticao(PlaybackStateCompat.REPEAT_MODE_NONE)
        } else {
            SharedPreferenceUtil.modoRepeticaoMusica = PlaybackStateCompat.REPEAT_MODE_ALL
            modoRepeticao.value = PlaybackStateCompat.REPEAT_MODE_ALL
            playerControle.modoRepeticao(PlaybackStateCompat.REPEAT_MODE_ALL)
        }
    }

    fun modoAleatorio() {
        if (modoAleatorio.value == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
            SharedPreferenceUtil.modoAleatorio = PlaybackStateCompat.SHUFFLE_MODE_NONE
            modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
            playerControle.modoAleatorio(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        } else {
            SharedPreferenceUtil.modoAleatorio = PlaybackStateCompat.SHUFFLE_MODE_ALL
            SharedPreferenceUtil.modoReproducaoPlayer = REPRODUCAO_ALEATORIO
            modoAleatorio.value = SharedPreferenceUtil.modoAleatorio
            playerControle.modoAleatorio(PlaybackStateCompat.SHUFFLE_MODE_ALL)
        }
    }

    fun deletarMusicaDispositivo(
        intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val uri: Uri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media.value!!.id)
        val contentResolver = APP.getContext().contentResolver
        try {
            contentResolver.delete(uri, null, null)
        } catch (e: SecurityException) {
            val intentSender = when {
                VersaoUtil.androidR() -> {
                    MediaStore.createDeleteRequest(contentResolver, listOf(uri)).intentSender
                }

                VersaoUtil.androidQ() -> {
                    val recoverableSecurityException = e as? RecoverableSecurityException
                    recoverableSecurityException?.userAction?.actionIntent?.intentSender
                }

                else -> null
            }
            intentSender?.let { sender ->
                intentSenderLauncher.launch(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }

    fun abrirListaReproducaoAtual(findNavController: NavController) {
        val action = PlayerMusicaFragmentDirections.actionPlayerMusicaFragmentToListaReproducaoAtualFragment()
        findNavController.navigate(action)
    }
}