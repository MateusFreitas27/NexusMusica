package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListenerAdicoesRecentes
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterListaMusicaRecente(private val listaMusica: List<Musica>, private val clickListenerAdicoesRecentes: InterfaceClickListenerAdicoesRecentes): RecyclerView.Adapter<AdapterListaMusicaRecente.viewHodel>() {
    //private var listaMusica:List<Musica> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_musica_recente,parent,false)
        return viewHodel(view)
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: viewHodel, position: Int) {
        holder.textNomeMusicaRecente.text = listaMusica[position].titulo
        holder.textNomeAlbumMusicaRecente.text = listaMusica[position].albumNome

        val imagem = FuncoesUtil.carregarCapaMusica(listaMusica[position])
        Glide.with(holder.itemView).load(imagem).error(R.drawable.sem_album).into(holder.imagemCapa)
        
        holder.itemView.setOnClickListener {
            clickListenerAdicoesRecentes.onClick(listaMusica[position])
        }
    }

    inner class viewHodel(itemView : View): RecyclerView.ViewHolder(itemView){
        val imagemCapa: ImageView
        val textNomeMusicaRecente: TextView
        val textNomeAlbumMusicaRecente: TextView


        init {
            imagemCapa = itemView.findViewById(R.id.imgCapaMusicaRecente)
            textNomeMusicaRecente = itemView.findViewById(R.id.textNomeMusicaRecente)
            textNomeAlbumMusicaRecente = itemView.findViewById(R.id.textNomeAlbumRecente)
        }
    }

}