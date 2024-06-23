package br.com.nexus.nexusmusica.sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterFilaReproducao
import br.com.nexus.nexusmusica.databinding.FragmentBottomFilaReproducaoBinding
import br.com.nexus.nexusmusica.ui.fragments.playerMusica.PlayerMusicaViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomFilaReproducao : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomFilaReproducaoBinding? = null
    private val binding get() = _binding!!
    private val playerMusicaViewModel: PlayerMusicaViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomFilaReproducaoBinding.inflate(inflater, container, false)
        val behavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        playerMusicaViewModel.carregarListaMusica()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerMusicaViewModel.listaMusica.observe(viewLifecycleOwner){
            with(binding.recyclerListaFilaReproducao){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = AdapterFilaReproducao(it){
                    playerMusicaViewModel.reproduzirMusicaSelecionada(it.description.mediaId!!.toLong())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}