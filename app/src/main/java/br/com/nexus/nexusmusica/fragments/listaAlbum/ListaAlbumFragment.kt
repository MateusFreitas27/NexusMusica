package br.com.nexus.nexusmusica.fragments.listaAlbum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.adapter.AdapterListaAlbums
import br.com.nexus.nexusmusica.databinding.FragmentListaAlbumBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListenerListaAlbum
import br.com.nexus.nexusmusica.modelo.Album
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListaAlbumFragment : Fragment(), InterfaceClickListenerListaAlbum {
    private var _binding: FragmentListaAlbumBinding? = null
    private val binding get() = _binding!!
    private val listaAlbumViewModel: ListaAlbumViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
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
            recyclerView.adapter = AdapterListaAlbums(it, this)
        }
    }

    override fun onItemClick(album: Album) {
        listaAlbumViewModel.abrirTelaDetalheAlbum(findNavController(), album)
    }
}