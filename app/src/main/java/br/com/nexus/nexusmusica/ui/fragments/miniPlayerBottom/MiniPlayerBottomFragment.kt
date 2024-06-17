package br.com.nexus.nexusmusica.ui.fragments.miniPlayerBottom

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.FragmentMiniPlayerBottomBinding
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class MiniPlayerBottomFragment : Fragment() {
    private var _binding: FragmentMiniPlayerBottomBinding? = null
    private val binding get() = _binding!!
    private val miniPlayerBottomFragmentViewModel: MiniPlayerBottomFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        miniPlayerBottomFragmentViewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiniPlayerBottomBinding.inflate(inflater, container, false)
        configurarClicks()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configurarClicks() {
        binding.cardPlayer.setOnClickListener {
            miniPlayerBottomFragmentViewModel.recomecaReproducao = false
            val action =
                MiniPlayerBottomFragmentDirections.actionMiniPlayerBottomFragmentToPlayerMusicaFragment(
                    miniPlayerBottomFragmentViewModel.musica,
                    false
                )
            findNavController().navigate(action)
        }

        binding.imgBtnMiniPlayerPausePlay.setOnClickListener {
            miniPlayerBottomFragmentViewModel.retomarReproducao()
        }
    }

    private fun configurarObservers() {
        miniPlayerBottomFragmentViewModel.infoMusicaTocando.observe(viewLifecycleOwner){
            miniPlayerBottomFragmentViewModel.buscarMusica(it)
        }
        miniPlayerBottomFragmentViewModel.plabackState.observe(viewLifecycleOwner){
            if (it?.state == PlaybackStateCompat.STATE_PAUSED)
                binding.imgBtnMiniPlayerPausePlay.setImageResource(R.drawable.icon_play)
            else
                binding.imgBtnMiniPlayerPausePlay.setImageResource(R.drawable.icon_pause)
        }
        miniPlayerBottomFragmentViewModel.nomeMusica.observe(viewLifecycleOwner){nomeMusica ->
            binding.txtNomeMusicaMiniPlayer.text = nomeMusica
        }
        miniPlayerBottomFragmentViewModel.nomeAlbum.observe(viewLifecycleOwner){nomeAlbum ->
            binding.txtNomeAlbumMiniPlayer.text = nomeAlbum
        }
        miniPlayerBottomFragmentViewModel.capaMusica.observe(viewLifecycleOwner){capaMusica ->
            Glide.with(this).load(FuncoesUtil.carregarCapaMusica(capaMusica)).centerCrop().dontAnimate().into(binding.imageCapaMusicaMiniPlayer)
        }
    }
}