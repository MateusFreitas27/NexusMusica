package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.databinding.AdapterListaAlbunsBinding
import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide


class AdapterListaAlbums(
    private val listaAlbums: List<Album>,
    private val abrirTelaDetalhamento: (Album) -> Unit
): RecyclerView.Adapter<AdapterListaAlbums.ViewHodel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        return ViewHodel(AdapterListaAlbunsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listaAlbums.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        val album = listaAlbums[position]
        holder.txtTituloAlbum.text = album.titulo
        holder.txtNomeArtista.text = album.artistaNome
        val imagemCapa = FuncoesUtil.carregarCapaMusica(album.obterPrimeiraMusicaSegura())
        Glide.with(holder.itemView).load(imagemCapa).into(holder.imagemCapaAlbum)

        holder.itemView.setOnClickListener {
            abrirTelaDetalhamento(album)
        }
    }

    inner class ViewHodel(binding: AdapterListaAlbunsBinding): RecyclerView.ViewHolder(binding.root){
        val imagemCapaAlbum: ImageView = binding.imgCapaAlbum
        val txtTituloAlbum: TextView = binding.txtNomeAlbum
        val txtNomeArtista: TextView = binding.txtNomeArtistaAlbum

    }

}