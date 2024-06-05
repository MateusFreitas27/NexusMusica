package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.AdapterListaMusicaRecenteBinding
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListenerAdicoesRecentes
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterListaMusicaRecente(private val clickListenerAdicoesRecentes: InterfaceClickListenerAdicoesRecentes): RecyclerView.Adapter<AdapterListaMusicaRecente.viewHodel>() {
    private var listaMusica:MutableList<Musica> = ArrayList()

    fun atualizarDados(listaMusica: List<Musica>){
        this.listaMusica.clear()
        this.listaMusica.addAll(listaMusica)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHodel {
        return viewHodel(AdapterListaMusicaRecenteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: viewHodel, position: Int) {
        holder.textNomeMusicaRecente.text = listaMusica[position].nomeMusica
        holder.textNomeAlbumMusicaRecente.text = listaMusica[position].nomeAlbum

        val imagem = FuncoesUtil.carregarCapaMusica(listaMusica[position])
        Glide.with(holder.itemView).load(imagem).error(R.drawable.sem_album).into(holder.imagemCapa)
        
        holder.itemView.setOnClickListener {
            clickListenerAdicoesRecentes.onClick(listaMusica[position])
        }
    }

    inner class viewHodel(binding : AdapterListaMusicaRecenteBinding): RecyclerView.ViewHolder(binding.root){
        val imagemCapa: ImageView = binding.imgCapaMusicaRecente
        val textNomeMusicaRecente: TextView = binding.textNomeMusicaRecente
        val textNomeAlbumMusicaRecente: TextView = binding.textNomeAlbumRecente
    }

}