package br.com.nexus.nexusmusica.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.FragmentControleVelocidadeMediaBinding
import br.com.nexus.nexusmusica.ui.fragments.playerMusica.PlayerMusicaViewModel
import br.com.nexus.nexusmusica.util.SharedPreferenceUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class VelocidadePlaybackDialog: DialogFragment() {
    private var _binding: FragmentControleVelocidadeMediaBinding? = null
    private val binding get() = _binding!!
    private val playerMusicaViewModel: PlayerMusicaViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentControleVelocidadeMediaBinding.inflate(layoutInflater)
        val alertDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            var valorSlider: Float = SharedPreferenceUtil.velocidadeReproducaoMedia
            binding.sliderControleVelocidade.value = valorSlider
            binding.sliderControleVelocidade.addOnChangeListener { _, value, _ ->
                valorSlider = value
            }
            setTitle(R.string.txt_titulo_dialog_velocidade_reproducao)
            setCancelable(false)
            setPositiveButton(R.string.btn_positivo_dialog_controle) { _, _ ->
                playerMusicaViewModel.alterarVelocidadePlayer(valorSlider)
            }
            setNegativeButton(R.string.btn_negativo_dialog_controle){ _, _ ->}
            setNeutralButton(R.string.btn_redefinir_dialog_controle){ _, _ ->
               playerMusicaViewModel.alterarVelocidadePlayer(1.0f)
            }
            setView(binding.root)
        }
        return alertDialog.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}