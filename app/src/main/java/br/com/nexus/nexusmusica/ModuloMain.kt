package br.com.nexus.nexusmusica

import br.com.nexus.nexusmusica.fragments.detalheAlbum.DetalheAlbumFragmentViewModel
import br.com.nexus.nexusmusica.fragments.home.HomeViewModel
import br.com.nexus.nexusmusica.fragments.listaAlbum.ListaAlbumViewModel
import br.com.nexus.nexusmusica.fragments.listaMusica.ListaMusicaViewModel
import br.com.nexus.nexusmusica.fragments.miniPlayerBottom.MiniPlayerBottomFragmentViewModel
import br.com.nexus.nexusmusica.fragments.musicasRecentes.ListaMusicasrecentesViewModel
import br.com.nexus.nexusmusica.fragments.playerMusica.PlayerMusicaViewModel
import br.com.nexus.nexusmusica.repositorio.AlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicasRecentesRepositorio
import br.com.nexus.nexusmusica.repositorio.RealAlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.RealMusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.RealMusicasRecentesRepositorio
import br.com.nexus.nexusmusica.repositorio.RealRepositorio
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.services.MusicaConector
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val mainModulo = module {
    single{
        androidContext().contentResolver
    }
}

private val dataModulo = module {
    single {
        RealRepositorio(get(), get(), get())
    } bind Repositorio::class

    single {
        RealMusicaRepositorio(get())
    } bind MusicaRepositorio::class

    single {
        RealAlbumRepositorio(get())
    } bind AlbumRepositorio::class

    single {
        RealMusicasRecentesRepositorio(get())
    } bind MusicasRecentesRepositorio::class
    single <MusicaConector> {
        MusicaConector(get())
    }
}

private val viewModelModulo = module {
    viewModel{
        ListaMusicaViewModel(get())
    }

    viewModel{
        ListaAlbumViewModel(get())
    }

    viewModel {
        HomeViewModel()
    }

    viewModel{
        ListaMusicasrecentesViewModel(get())
    }

    viewModel{
        DetalheAlbumFragmentViewModel()
    }

    viewModel{
        PlayerMusicaViewModel(
            get(),
            get()
        )
    }

    viewModel {
        MiniPlayerBottomFragmentViewModel(
            get(),
            get()
        )
    }

}


val appModulos = listOf(mainModulo, dataModulo, viewModelModulo)