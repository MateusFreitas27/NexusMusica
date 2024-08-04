package br.com.nexus.nexusmusica.ui.fragments.historicoMusica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterHistoricoMusicas
import br.com.nexus.nexusmusica.databinding.FragmentHistoricoMusicaBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoricoMusicaFragment : Fragment() {
    private var _binding: FragmentHistoricoMusicaBinding? = null
    private val binding get() = _binding!!
    private val historicoMusicaViewModel: HistoricoMusicaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricoMusicaBinding.inflate(inflater, container, false)
        binding.toolbarHIstoricoMusicas.setNavigationOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarObservers()
    }

    override fun onResume() {
        super.onResume()
        historicoMusicaViewModel.carregarListaHistorico()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciarObservers(){
        historicoMusicaViewModel.listaMusica.observe(viewLifecycleOwner){
            with(binding.recyclerListaHistoricoMusicas){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = AdapterHistoricoMusicas(it){
                    historicoMusicaViewModel.abrirPlayerMusica(findNavController(), it)
                }
            }
        }
    }
}