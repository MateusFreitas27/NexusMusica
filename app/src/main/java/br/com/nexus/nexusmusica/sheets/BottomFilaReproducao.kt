package br.com.nexus.nexusmusica.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.nexus.nexusmusica.adapter.AdapterFilaReproducao
import br.com.nexus.nexusmusica.databinding.FragmentBottomFilaReproducaoBinding
import br.com.nexus.nexusmusica.fragments.playerMusica.PlayerMusicaViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomFilaReproducao : BottomSheetDialogFragment() {
    private var binding: FragmentBottomFilaReproducaoBinding? = null
    private val playerMusicaViewModel: PlayerMusicaViewModel by viewModel();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomFilaReproducaoBinding.inflate(inflater, container, false)

        playerMusicaViewModel.carregarListaMusica()
        playerMusicaViewModel.listaMusica.observe(viewLifecycleOwner){
            val adapterFilaReproducao = AdapterFilaReproducao(){
                playerMusicaViewModel.reproduzirMusicaSelecionada(it.description.mediaId)
            }
            with(binding?.recyclerListaFilaReproducao){
                this?.setHasFixedSize(true)
                this?.setItemViewCacheSize(20)
                adapterFilaReproducao.atualizarListaDados(it)
                this?.layoutManager = LinearLayoutManager(context)
                this?.adapter = adapterFilaReproducao
            }

        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet: FrameLayout = dialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.apply {
            peekHeight = resources.displayMetrics.heightPixels
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}