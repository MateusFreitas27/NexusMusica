package br.com.nexus.nexusmusica.fragments.historicoMusica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.nexus.nexusmusica.databinding.FragmentHistoricoMusicaBinding

class HistoricoMusicaFragment : Fragment() {
    private var _binding: FragmentHistoricoMusicaBinding? = null
    private val binding get() = _binding!!

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
}