package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.databinding.AdapterListaMusicasAlbunsDetalheBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListernerDetalheAlbum
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil

class AdapterListaMusicasAlbumDetalhe(private val listaMusica: List<Musica>, private val clickListernerDetalheAlbum: InterfaceClickListernerDetalheAlbum): RecyclerView.Adapter<AdapterListaMusicasAlbumDetalhe.ViewHodel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        return ViewHodel(AdapterListaMusicasAlbunsDetalheBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        val musica = listaMusica[position]
        val numMusica = musica.numeroFaixa.toString()
        holder.txtNumFaixaAlbum.text = if (numMusica.length >= 4) numMusica.slice(3..3) else numMusica
        holder.txtNomeMusicaAlbum.text = musica.nomeMusica
        holder.txtDuracaoFaixaAlbum.text = FuncoesUtil.formatarDuracaoMusica(musica.duracao)

        holder.itemView.setOnClickListener {
            clickListernerDetalheAlbum.onClick(listaMusica[position])
        }
    }

    inner class ViewHodel(binding: AdapterListaMusicasAlbunsDetalheBinding): RecyclerView.ViewHolder(binding.root){
        val txtNumFaixaAlbum: TextView = binding.txtNumFaixaMusicaAlbumDetalhe
        val txtNomeMusicaAlbum: TextView = binding.txtNomeMusicaAlbumDetalhe
        val txtDuracaoFaixaAlbum: TextView = binding.txtDuracaoFaixaDetalhe

    }
}