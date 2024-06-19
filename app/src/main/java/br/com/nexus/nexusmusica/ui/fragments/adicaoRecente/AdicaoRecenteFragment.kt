package br.com.nexus.nexusmusica.ui.fragments.adicaoRecente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterListaMusicaRecente
import br.com.nexus.nexusmusica.databinding.FragmentAdicaoRecenteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AdicaoRecenteFragment : Fragment() {
    private var _binding: FragmentAdicaoRecenteBinding? = null
    private val binding get() = _binding!!
    private val musicasRecentesViewModel: AdicaoRecenteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdicaoRecenteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarListaAdicoesRecentes.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
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
            val adapter = AdapterListaMusicaRecente(it){ musica ->
                musicasRecentesViewModel.irParaReproducaoMusica(findNavController(), musica)
            }
            val recycler = binding.recyclerViewListaMusicaRecentes
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = adapter
        }
    }
}