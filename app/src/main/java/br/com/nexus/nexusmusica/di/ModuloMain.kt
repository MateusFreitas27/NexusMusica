package br.com.nexus.nexusmusica.di

import androidx.room.Room
import br.com.nexus.nexusmusica.ui.fragments.detalheAlbum.DetalheAlbumFragmentViewModel
import br.com.nexus.nexusmusica.ui.fragments.historicoMusica.HistoricoMusicaViewModel
import br.com.nexus.nexusmusica.ui.fragments.home.HomeViewModel
import br.com.nexus.nexusmusica.ui.fragments.listaAlbum.ListaAlbumViewModel
import br.com.nexus.nexusmusica.ui.fragments.listaMusica.ListaMusicaViewModel
import br.com.nexus.nexusmusica.ui.fragments.miniPlayerBottom.MiniPlayerBottomFragmentViewModel
import br.com.nexus.nexusmusica.ui.fragments.adicaoRecente.AdicaoRecenteViewModel
import br.com.nexus.nexusmusica.ui.fragments.playerMusica.PlayerMusicaViewModel
import br.com.nexus.nexusmusica.repositorio.AlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.MusicasRecentesRepositorio
import br.com.nexus.nexusmusica.repositorio.RealAlbumRepositorio
import br.com.nexus.nexusmusica.repositorio.RealMusicaRepositorio
import br.com.nexus.nexusmusica.repositorio.RealMusicasRecentesRepositorio
import br.com.nexus.nexusmusica.repositorio.RealRepositorio
import br.com.nexus.nexusmusica.repositorio.RealRoomRepositorio
import br.com.nexus.nexusmusica.repositorio.Repositorio
import br.com.nexus.nexusmusica.repositorio.RoomRepository
import br.com.nexus.nexusmusica.room.NexusMusicaBancoDados
import br.com.nexus.nexusmusica.services.MusicaConector
import br.com.nexus.nexusmusica.services.PlayerControle
import br.com.nexus.nexusmusica.ui.fragments.listaReproducaoAtual.ListaReproducaoAtualViewModel
import br.com.nexus.nexusmusica.ui.fragments.musicasMaisOuvidas.MusicasMaisOuvidasViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val mainModulo = module {
    single {
        androidContext().contentResolver
    }
}

private val roomModulo = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NexusMusicaBancoDados::class.java,
            "nexus_musica_db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    factory{
        get<NexusMusicaBancoDados>().historicoDao()
    }
}

private val dataModulo = module {
    single {
        RealRepositorio(get(), get(), get(), get())
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

    single {
        RealRoomRepositorio(get())
    } bind RoomRepository::class

    single {
        MusicaConector(get())
    } bind MusicaConector::class

    single{
        PlayerControle(get())
    } bind PlayerControle::class
}

private val viewModelModulo = module {
    viewModel {
        ListaMusicaViewModel(get(), get())
    }

    viewModel {
        ListaAlbumViewModel(get())
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        AdicaoRecenteViewModel(get())
    }

    viewModel {
        DetalheAlbumFragmentViewModel()
    }

    viewModel {
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

    viewModel {
        HistoricoMusicaViewModel(
            get(),
            get()
        )
    }

    viewModel{
        MusicasMaisOuvidasViewModel(
            get(),
            get()
        )
    }

    viewModel{
        ListaReproducaoAtualViewModel(get())
    }
}


val appModulos = listOf(mainModulo, dataModulo, viewModelModulo, roomModulo)