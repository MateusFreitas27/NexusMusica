package br.com.nexus.nexusmusica.ui.fragments.listaMusica

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterListaMusica
import br.com.nexus.nexusmusica.databinding.FragmentListaMusicaBinding
import br.com.nexus.nexusmusica.util.VersaoUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaMusicaFragment : Fragment() {
    private var _binding: FragmentListaMusicaBinding? = null
    private val binding get() = _binding!!
    private val listaMusicaViewModel: ListaMusicaViewModel by viewModel()
    private val intentSLDeletarArquivo: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            if (VersaoUtil.androidQ()){
                listaMusicaViewModel.carregarListaMusica()
            }
        }
    }
    private var adapterListaMusica: AdapterListaMusica? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaMusicaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciarObservers()
    }

    override fun onResume() {
        super.onResume()
        listaMusicaViewModel.carregarListaMusica()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapterListaMusica = null
    }

    private fun iniciarObservers() {
        listaMusicaViewModel.listaMusicas.observe(viewLifecycleOwner) {lista ->
            with(binding.recyclerViewListaMusica){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter= AdapterListaMusica(lista, intentSLDeletarArquivo) {musica ->
                    listaMusicaViewModel.abrirPlayerMusica(findNavController(), musica)
                }
            }
        }
    }
}