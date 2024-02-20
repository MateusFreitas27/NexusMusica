package br.com.nexus.nexusmusica.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.ActivityMainBinding
import br.com.nexus.nexusmusica.fragments.home.HomeFragment
import br.com.nexus.nexusmusica.fragments.listaAlbum.ListaAlbumFragment
import br.com.nexus.nexusmusica.fragments.listaMusica.ListaMusicaFragment
import br.com.nexus.nexusmusica.util.VersaoUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissaoArmazenamento: ActivityResultLauncher<String>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissaoArmazenamento = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            resultadoPermissao: Boolean ->
                if (!resultadoPermissao){
                    finish()
                }else{
                    configurarNavHost()
                }
        }
        supportFragmentManager.setFragmentResultListener("ocultarBottomView", this){ _, _ ->
            if (binding.bottomNavigationView.isVisible){
                binding.bottomNavigationView.visibility = View.GONE
            }else{
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
        verificarPermissoes()
        configurarNavHost()
    }

    private fun verificarPermissoes() {
        if (VersaoUtil.androidT()){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED){
                permissaoArmazenamento.launch(Manifest.permission.READ_MEDIA_AUDIO)
            }
        }else{
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissaoArmazenamento.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }


    private fun configurarNavHost(){
        val navHostFragement = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragement.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destino,_ ->
            when(destino.id){
                R.id.menu_item_home, R.id.menu_item_musica, R.id.menu_item_album -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }
}