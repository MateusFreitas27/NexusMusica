package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.databinding.AdapterListaHistoricoMusicaBinding
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterHistoricoMusicas(
    lista: MutableList<Musica>,
    private val reproduzirMusica: (Musica) -> Unit
): RecyclerView.Adapter<AdapterHistoricoMusicas.HistoricoMusicaViewHodel>() {
    private var listaMusica: MutableList<Musica> = lista

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoMusicaViewHodel {
        return HistoricoMusicaViewHodel(AdapterListaHistoricoMusicaBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemId(position: Int): Long {
        val musica = listaMusica[position]
        return musica.id
    }

    override fun getItemCount(): Int  = listaMusica.size

    override fun onBindViewHolder(holder: HistoricoMusicaViewHodel, position: Int) {
        val musica = listaMusica[position]
        holder.txtNomeMusicaHistorico.text = musica.nomeMusica
        holder.txtNomeAlbumHistorico.text = musica.nomeAlbum
        val imagem = FuncoesUtil.carregarCapaMusica(musica)
        Glide.with(holder.itemView).load(imagem).centerCrop().dontAnimate().into(holder.imagemCapaHistorico)

        holder.itemView.setOnClickListener {
            reproduzirMusica(musica)
        }
    }

    inner class HistoricoMusicaViewHodel(binding: AdapterListaHistoricoMusicaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imagemCapaHistorico: ImageView = binding.imgCapaAlbumHistorico
        val txtNomeMusicaHistorico: TextView = binding.txtNomeMusicaHistorico
        val txtNomeAlbumHistorico: TextView = binding.txtNomeAlbumHistorico
    }


}