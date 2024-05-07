package br.com.nexus.nexusmusica.fragments.detalheAlbum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.adapter.AdapterListaMusicasAlbumDetalhe
import br.com.nexus.nexusmusica.databinding.FragmentDetalheAlbumBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListernerDetalheAlbum
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalheAlbumFragment : Fragment(), InterfaceClickListernerDetalheAlbum {
    private var _binding: FragmentDetalheAlbumBinding? = null
    private val binding get() = _binding!!
    private val detalheViewModel: DetalheAlbumFragmentViewModel by viewModel()
    private val args: DetalheAlbumFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalheAlbumBinding.inflate(layoutInflater, container, false)
        detalheViewModel.setAlbum(args)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagem: ByteArray? = FuncoesUtil.carregarCapaMusica(detalheViewModel.capaAlbum)
        if (imagem != null){
            Glide.with(this).asBitmap().load(imagem).into(binding.imgDetalheAlbum)
        }else{
            Glide.with(this).asDrawable().load(R.drawable.sem_album).into(binding.imgDetalheAlbum)
        }
        binding.txtNomeAlbumDetalhe.text = detalheViewModel.infoalbum.titulo
        binding.txtNomeArtistaAlbumDetalhe.text = detalheViewModel.infoalbum.artistaNome
        binding.txtDuracaoTotalAlbumDetalhe.text = FuncoesUtil.formatarDuracaoMusica(detalheViewModel.totalDuracaoAlbum)
        binding.toolbarDetalhamentoAlbum.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        iniciarObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciarObservers() {
        detalheViewModel.listaMusica.observe(viewLifecycleOwner) {
            val recyclerView = binding.recyclerViewListaMusicaDetalhe
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = AdapterListaMusicasAlbumDetalhe(it, this)
        }
    }

    override fun onClick(musica: Musica) {
        detalheViewModel.abrirTelaPlayer(findNavController(),musica)
    }
}