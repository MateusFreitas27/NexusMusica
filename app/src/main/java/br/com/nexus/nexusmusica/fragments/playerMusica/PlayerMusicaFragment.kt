package br.com.nexus.nexusmusica.fragments.playerMusica

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.TAG_MODAL_LISTA_MUSICAS
import br.com.nexus.nexusmusica.databinding.FragmentPlayerMusicaBinding
import br.com.nexus.nexusmusica.sheets.BottomFilaReproducao
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerMusicaFragment : Fragment() {
    private var _binding: FragmentPlayerMusicaBinding? = null
    private val binding get() = _binding!!
    private val playerMusicaViewModel: PlayerMusicaViewModel by viewModel()
    private val args: PlayerMusicaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerMusicaBinding.inflate(layoutInflater, container, false)
        playerMusicaViewModel.setMusica(args)
        configurarOnClicks()
        configurarObservers()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun configurarObservers() {
        playerMusicaViewModel.conectado.observe(viewLifecycleOwner){
            playerMusicaViewModel.iniciar()
        }
        playerMusicaViewModel.infoMusicaTocando.observe(viewLifecycleOwner){
            playerMusicaViewModel.carregarDadosMusica(it)
        }
        playerMusicaViewModel.duracaoMusica.observe(viewLifecycleOwner){
            binding.seekBarProgressoMusica.max = it
            val dataFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.txtPlayerDuracaoMusica.text = dataFormat.format(it)
        }
        playerMusicaViewModel.nomeMusica.observe(viewLifecycleOwner){
            binding.txtPlayerNomeMusica.text = it
        }
        playerMusicaViewModel.nomeAlbum.observe(viewLifecycleOwner){
            binding.txtPlayerNomeAlbum.text = it
        }
        playerMusicaViewModel.imgCapa.observe(viewLifecycleOwner){
            if (it != null){
                Glide.with(this).load(it).into(binding.imgPlayerCapaMusica)
            }else{
                Glide.with(this).load(R.drawable.sem_album).into(binding.imgPlayerCapaMusica)
            }
        }
        playerMusicaViewModel.progressoMusica.observe(viewLifecycleOwner){
            val dataFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.txtProgressoMusica.text = dataFormat.format(it)
            binding.seekBarProgressoMusica.progress = it.toInt()
        }
        playerMusicaViewModel.modoRepeticao.observe(viewLifecycleOwner){
            when(it){
                PlaybackStateCompat.REPEAT_MODE_NONE -> binding.imgBtnRepetir.setImageResource(R.drawable.icon_repetir_desativado)
                PlaybackStateCompat.REPEAT_MODE_ALL -> binding.imgBtnRepetir.setImageResource(R.drawable.icon_repetir_todas)
                PlaybackStateCompat.REPEAT_MODE_ONE -> binding.imgBtnRepetir.setImageResource(R.drawable.icon_repetir_um)
            }
        }
        playerMusicaViewModel.modoAleatorio.observe(viewLifecycleOwner){
            if (it == PlaybackStateCompat.SHUFFLE_MODE_ALL){
                binding.imgBtnAleatorio.setImageResource(R.drawable.icon_aleatorio_ativado)
            } else {
                binding.imgBtnAleatorio.setImageResource(R.drawable.icon_aleatorio_desativado)
            }
        }
        playerMusicaViewModel.tocandoMusica.observe(viewLifecycleOwner){
            if (it == 3){
                binding.fabPlayPause.setImageResource(R.drawable.icon_pause)
            }else{
                binding.fabPlayPause.setImageResource(R.drawable.icon_play)
            }
        }
    }

    private fun configurarOnClicks() {
        with(binding){
            fabPlayPause.setOnClickListener{
                playerMusicaViewModel.playPlause()
            }
            imgBtnProximaMusica.setOnClickListener{
                fabPlayPause.setImageResource(R.drawable.icon_pause)
                playerMusicaViewModel.proximaMusica()
            }
            imgBtnMusicaAnterior.setOnClickListener{
                fabPlayPause.setImageResource(R.drawable.icon_pause)
                playerMusicaViewModel.musicaAnterior()
            }
            imgBtnAleatorio.setOnClickListener {
                playerMusicaViewModel.modoAleatorio()
            }
            imgBtnRepetir.setOnClickListener{
                playerMusicaViewModel.trocarModorepetirMusica()
            }
            seekBarProgressoMusica.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
                    if (fromUser){
                        playerMusicaViewModel.seekToMusica(position.toLong())
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
            btnImageListaMusica.setOnClickListener {
                val modalBottomSheet = BottomFilaReproducao()
                modalBottomSheet.show(parentFragmentManager, TAG_MODAL_LISTA_MUSICAS)
            }
            btnPlayerVelocidade.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    var valorSlider: Float = SharedPreferenceUtil.velocidadeReproducaoMedia
                    val view = layoutInflater.inflate(R.layout.fragment_controle_velocidade_media, null)
                    val slide = view.findViewById<Slider>(R.id.slider_controle_velocidade)
                    slide.value = valorSlider
                    slide.addOnChangeListener { _, value, _ ->
                        valorSlider = value
                    }
                    setTitle(R.string.txt_titulo_dialog_velocidade_reproducao)
                    setView(view)
                    setCancelable(false)
                    setPositiveButton(R.string.btn_positivo_dialog_controle) { _, _ ->
                        playerMusicaViewModel.alterarVelocidadePlayer(valorSlider)
                    }
                    setNegativeButton(R.string.btn_negativo_dialog_controle){ _, _ ->}
                    setNeutralButton(R.string.btn_redefinir_dialog_controle){ _, _ ->
                        playerMusicaViewModel.alterarVelocidadePlayer(1.0f)
                    }
                    create()
                    show()
                }
            }
        }
    }

}