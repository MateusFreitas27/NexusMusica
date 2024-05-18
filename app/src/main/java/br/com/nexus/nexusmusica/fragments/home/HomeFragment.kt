package br.com.nexus.nexusmusica.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventoToqueTela()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun eventoToqueTela() {
        binding.btnAdicoesRecentes.setOnClickListener {
            homeViewModel.abrirTelaMusicasRecentes(findNavController())
        }
    }
}