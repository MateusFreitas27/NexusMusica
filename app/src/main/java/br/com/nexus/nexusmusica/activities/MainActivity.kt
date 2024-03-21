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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.ActivityMainBinding
import br.com.nexus.nexusmusica.util.VersaoUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissaoArmazenamento: ActivityResultLauncher<String>
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
        appBarConfiguration = AppBarConfiguration(setOf(R.id.menu_item_home, R.id.menu_item_musica, R.id.menu_item_album))
        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener{_,destino,_ ->
            when(destino.id){
                R.id.menu_item_home, R.id.menu_item_musica, R.id.menu_item_album -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.playerMusicaFragment ->{
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else ->{
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}