package br.com.nexus.nexusmusica.fragments.musicasRecentes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterListaMusicaRecente
import br.com.nexus.nexusmusica.databinding.FragmentListaMusicasRecentesBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListenerAdicoesRecentes
import br.com.nexus.nexusmusica.modelo.Musica
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListaMusicasRecentesFragment : Fragment(), InterfaceClickListenerAdicoesRecentes {
    private var _binding: FragmentListaMusicasRecentesBinding? = null
    private val binding get() = _binding!!
    private val musicasRecentesViewModel: ListaMusicasrecentesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaMusicasRecentesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarObservers()
    }

    override fun onResume() {
        super.onResume()
        musicasRecentesViewModel.carregarListaMusicasRecentes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciarObservers() {
        musicasRecentesViewModel.listaMusicarecente.observe(viewLifecycleOwner) {
            val adapter = AdapterListaMusicaRecente(this)
            val recycler = binding.recyclerViewListaMusicaRecentes
            adapter.atualizarDados(it)
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = adapter
        }
    }

    override fun onClick(musica: Musica) {
        musicasRecentesViewModel.irParaReproducaoMusica(findNavController(), musica)
    }
}