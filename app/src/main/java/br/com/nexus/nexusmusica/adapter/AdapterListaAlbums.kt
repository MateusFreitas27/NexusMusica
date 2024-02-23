package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import com.bumptech.glide.Glide
import br.com.nexus.nexusmusica.modelo.Album
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListenerListaAlbum


class AdapterListaAlbums(private val listaAlbums: List<Album>, private val clickListenerListaAlbum: InterfaceClickListenerListaAlbum): RecyclerView.Adapter<AdapterListaAlbums.ViewHodel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_albuns,parent,false)
        return ViewHodel(view)
    }

    override fun getItemCount(): Int = listaAlbums.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        val album = listaAlbums[position]
        holder.txtTituloAlbum.text = album.titulo
        holder.txtNomeArtista.text = album.artistaNome
        val imagemCapa = FuncoesUtil.carregarCapaMusica(album.obterPrimeiraMusicaSegura())
        Glide.with(holder.itemView).load(imagemCapa).error(R.drawable.sem_album).into(holder.imagemCapaAlbum)

        holder.itemView.setOnClickListener {
            clickListenerListaAlbum.onItemClick(listaAlbums[position])
        }
    }

    inner class ViewHodel(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagemCapaAlbum: ImageView
        val txtTituloAlbum: TextView
        val txtNomeArtista: TextView

        init {
            imagemCapaAlbum = itemView.findViewById(R.id.imgCapaAlbum)
            txtTituloAlbum = itemView.findViewById(R.id.txtNomeAlbum)
            txtNomeArtista = itemView.findViewById(R.id.txtNomeArtistaAlbum)
        }
    }

}