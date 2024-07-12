package br.com.nexus.nexusmusica.ui.fragments.listaReproducaoAtual

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.adapter.AdapterFilaReproducao
import br.com.nexus.nexusmusica.databinding.FragmentListaReproducaoAtualBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaReproducaoAtualFragment : Fragment() {
    private var _binding: FragmentListaReproducaoAtualBinding? = null
    private val binding get() = _binding!!
    private val listaReproducaoAtualViewModel: ListaReproducaoAtualViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaReproducaoAtualBinding.inflate(inflater, container, false)
        listaReproducaoAtualViewModel.carregarListaMusica()
        binding.toolbarListaReproducao.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listaReproducaoAtualViewModel.listaMusicas.observe(viewLifecycleOwner){
            Log.d("lista", "onViewCreated: ${it.size}")
            with(binding.recyclerListaFilaReproducao){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = AdapterFilaReproducao(it){
                    listaReproducaoAtualViewModel.reproduzirMusicaSelecionada(it.description.mediaId!!.toLong())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}