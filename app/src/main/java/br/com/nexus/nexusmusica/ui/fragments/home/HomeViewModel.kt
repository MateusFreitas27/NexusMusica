package br.com.nexus.nexusmusica.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import br.com.nexus.nexusmusica.R


class HomeViewModel: ViewModel() {

    fun abrirTelaMusicasRecentes(findNavController: NavController) {
        findNavController.navigate(R.id.action_ir_Tela_Musicas_Adicionadas_Recentes)
    }

    fun abrirTelaHistoricoMusicas(findNavController: NavController){
        findNavController.navigate(R.id.action_menu_item_home_to_historicoMusicaFragment)
    }

    fun abrirTelaMaisOuvidas(findNavController: NavController) {
        findNavController.navigate(R.id.action_menu_item_home_to_maisOuvidasFragment)
    }
}