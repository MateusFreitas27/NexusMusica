package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.databinding.AdapterListaMaisOuvidasBinding
import br.com.nexus.nexusmusica.modelo.HistoricoMusica
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterListaMaisOuvidas(
    listaMusica: List<Musica>,
    private val reproduzirMusica: (Musica) -> Unit
) : RecyclerView.Adapter<AdapterListaMaisOuvidas.ViewHodel>() {
    private var listaMaisOuvidas: MutableList<Musica> = listaMusica.toMutableList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        return ViewHodel(
            AdapterListaMaisOuvidasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        val musica = listaMaisOuvidas[position]
        return musica.id
    }

    override fun getItemCount(): Int = listaMaisOuvidas.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        val musica = listaMaisOuvidas[position]

        holder.txtNomeMusica.text = musica.nomeMusica
        holder.txtNomeAlbum.text = musica.nomeAlbum

        val imagem = FuncoesUtil.carregarCapaMusica(musica.data)
        Glide.with(holder.itemView).load(imagem).into(holder.imagemCapa)

        holder.itemView.setOnClickListener {
            reproduzirMusica(musica)
        }
    }

    inner class ViewHodel(binding: AdapterListaMaisOuvidasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imagemCapa: ImageView = binding.imgCapaMusicaMaisOuvida
        val txtNomeMusica: TextView = binding.txtNomeMusicaMaisOuvida
        val txtNomeAlbum: TextView = binding.txtNomeAlbumMaisOuvida
    }
}