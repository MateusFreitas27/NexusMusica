package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.AdapterListaAdicaoRecenteBinding
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterListaAdicaoRecente(
    listaMusica: List<Musica>,
    private val reproduzirMusica: (Musica) -> Unit
): RecyclerView.Adapter<AdapterListaAdicaoRecente.viewHodel>() {
    private var listaMusica: MutableList<Musica> = listaMusica.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHodel {
        return viewHodel(
            AdapterListaAdicaoRecenteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: viewHodel, position: Int) {
        holder.textNomeMusicaRecente.text = listaMusica[position].nomeMusica
        holder.textNomeAlbumMusicaRecente.text = listaMusica[position].nomeAlbum

        val imagem = FuncoesUtil.carregarCapaMusica(listaMusica[position])
        Glide.with(holder.itemView).load(imagem).error(R.drawable.sem_album).into(holder.imagemCapa)

        holder.itemView.setOnClickListener {
            reproduzirMusica(listaMusica[position])
        }
    }

    inner class viewHodel(binding: AdapterListaAdicaoRecenteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imagemCapa: ImageView = binding.imgCapaMusicaRecente
        val textNomeMusicaRecente: TextView = binding.textNomeMusicaRecente
        val textNomeAlbumMusicaRecente: TextView = binding.textNomeAlbumRecente
    }

}