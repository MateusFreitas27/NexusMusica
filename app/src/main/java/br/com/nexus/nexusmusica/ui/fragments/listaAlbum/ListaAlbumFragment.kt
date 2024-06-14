package br.com.nexus.nexusmusica.ui.fragments.listaAlbum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterListaAlbums
import br.com.nexus.nexusmusica.databinding.FragmentListaAlbumBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListaAlbumFragment : Fragment() {
    private var _binding: FragmentListaAlbumBinding? = null
    private val binding get() = _binding!!
    private val listaAlbumViewModel: ListaAlbumViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaAlbumBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarObservers()
    }

    override fun onResume() {
        super.onResume()
        listaAlbumViewModel.carregarAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciarObservers() {
        listaAlbumViewModel.listaAlbums.observe(viewLifecycleOwner) {
            val recyclerView = binding.recyclerViewListaAlbums
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = AdapterListaAlbums(it){album ->
                listaAlbumViewModel.abrirTelaDetalheAlbum(findNavController(), album)
            }
        }
    }
}