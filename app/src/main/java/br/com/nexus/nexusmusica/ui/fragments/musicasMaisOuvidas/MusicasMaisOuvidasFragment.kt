package br.com.nexus.nexusmusica.ui.fragments.musicasMaisOuvidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.nexus.nexusmusica.databinding.FragmentMaisOuvidasBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicasMaisOuvidasFragment : Fragment() {
    private var _binding: FragmentMaisOuvidasBinding? = null
    private val binding get() = _binding!!
    private val musicasMaisOuvidasViewModel: MusicasMaisOuvidasViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaisOuvidasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarMaisOuvidas.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        musicasMaisOuvidasViewModel.listaMusicaMaisTocadas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}