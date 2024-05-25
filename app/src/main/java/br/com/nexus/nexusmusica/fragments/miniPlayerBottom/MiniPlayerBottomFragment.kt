package br.com.nexus.nexusmusica.fragments.miniPlayerBottom

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.text.TextUtils.EllipsizeCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.FragmentMiniPlayerBottomBinding
import br.com.nexus.nexusmusica.fragments.home.HomeFragmentDirections
import br.com.nexus.nexusmusica.fragments.playerMusica.PlayerMusicaViewModel
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class MiniPlayerBottomFragment : Fragment() {
    private var _binding: FragmentMiniPlayerBottomBinding? = null
    private val binding get() = _binding!!
    private val playerMusicaViewModel: PlayerMusicaViewModel by viewModel()
    private var musica: Musica = MusicaVazia

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiniPlayerBottomBinding.inflate(inflater, container, false)
        exibirInfomacaoTela()
        configurarClicks()
        configurarObservers()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun exibirInfomacaoTela() {
        if (SharedPreferenceUtil.musicaTocando!!.isNotEmpty()){
            val gson = Gson()
            val json = SharedPreferenceUtil.musicaTocando
            musica = gson.fromJson(json, Musica::class.java)
            playerMusicaViewModel.media = musica
            binding.txtNomeMusicaMiniPlayer.text = musica.titulo
            binding.txtNomeAlbumMiniPlayer.text = musica.albumNome
            Glide.with(this).load(FuncoesUtil.carregarCapaMusica(musica.data)).centerCrop().dontAnimate().into(binding.imageCapaMusicaMiniPlayer)
        }
    }

    private fun configurarClicks() {
        binding.cardPlayer.setOnClickListener {
            val action = MiniPlayerBottomFragmentDirections.actionMiniPlayerBottomFragmentToPlayerMusicaFragment(musica, true)
            findNavController().navigate(action)
        }

        binding.imgBtnMiniPlayerPausePlay.setOnClickListener {
            playerMusicaViewModel.retomaReproducaoMusica()
        }
    }

    private fun configurarObservers() {
        playerMusicaViewModel.infoMusicaTocando.observe(viewLifecycleOwner){
            playerMusicaViewModel.carregarDadosMusica(it)
        }
        playerMusicaViewModel.tocandoMusica.observe(viewLifecycleOwner){
            if (it == PlaybackStateCompat.STATE_PLAYING){
                binding.imgBtnMiniPlayerPausePlay.setImageResource(R.drawable.icon_pause)
            }else{
                binding.imgBtnMiniPlayerPausePlay.setImageResource(R.drawable.icon_play)
            }
        }
        playerMusicaViewModel.nomeMusica.observe(viewLifecycleOwner){
            binding.txtNomeMusicaMiniPlayer.text = it
        }
        playerMusicaViewModel.nomeAlbum.observe(viewLifecycleOwner){
            binding.txtNomeAlbumMiniPlayer.text = it
        }
        playerMusicaViewModel.imgCapa.observe(viewLifecycleOwner){
            Glide.with(this).load(it).centerCrop().dontAnimate().into(binding.imageCapaMusicaMiniPlayer)
        }
    }
}