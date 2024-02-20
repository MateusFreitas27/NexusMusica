package br.com.nexus.nexusmusica.fragments.listaMusica

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
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.adapter.AdapterListaMusica
import br.com.nexus.nexusmusica.databinding.FragmentListaMusicaBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickeListenerListaMusica
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.VersaoUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaMusicaFragment : Fragment(), InterfaceClickeListenerListaMusica {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
        _binding = FragmentListaMusicaBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
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
    }

    private fun iniciarObservers() {
        listaMusicaViewModel.listaMusicas.observe(viewLifecycleOwner) {lista ->
            val adapterListaMusica = AdapterListaMusica(intentSLDeletarArquivo,  this)
            adapterListaMusica.atualizarListaDados(lista)
            val recycler = binding.recyclerViewListaMusica
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = adapterListaMusica
        }
    }

    override fun onClick(musica: Musica) {
        listaMusicaViewModel.abrirPlayerMusica(findNavController(), musica)
    }
}