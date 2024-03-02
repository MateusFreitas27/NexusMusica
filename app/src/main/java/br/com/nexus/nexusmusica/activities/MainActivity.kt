package br.com.nexus.nexusmusica.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.ActivityMainBinding
import br.com.nexus.nexusmusica.util.VersaoUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissaoArmazenamento: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        permissaoArmazenamento = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                resultadoPermissao: Boolean ->
            if (!resultadoPermissao){
                finish()
            }else{
                configurarNavHost()
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
        val navController = navHostFragement.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destino,_ ->
            when(destino.id){
                R.id.menu_item_home, R.id.menu_item_musica, R.id.menu_item_album -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }
}