package br.com.nexus.nexusmusica.fragments.miniPlayerBottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import br.com.nexus.nexusmusica.MusicaVazia
import br.com.nexus.nexusmusica.databinding.FragmentMiniPlayerBottomBinding
import br.com.nexus.nexusmusica.fragments.home.HomeFragmentDirections

class MiniPlayerBottomFragment : Fragment() {
    private var _binding: FragmentMiniPlayerBottomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMiniPlayerBottomBinding.inflate(inflater, container, false)
        binding.cardPlayer.setOnClickListener {
            Toast.makeText(context, "Funcionando", Toast.LENGTH_SHORT).show()
            val musica = MusicaVazia
            val action = MiniPlayerBottomFragmentDirections.actionMiniPlayerBottomFragmentToPlayerMusicaFragment(musica)
            findNavController().navigate(action)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}