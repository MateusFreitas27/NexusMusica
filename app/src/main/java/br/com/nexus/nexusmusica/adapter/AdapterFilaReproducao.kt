package br.com.nexus.nexusmusica.adapter

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.AdapterListaFilaReproducaoBinding
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterFilaReproducao(lista: MutableList<MediaBrowserCompat.MediaItem>, private val reproduzirMusica: (MediaBrowserCompat.MediaItem) -> Unit) :
    RecyclerView.Adapter<AdapterFilaReproducao.ViewHodel>() {
    private var listaMusica: MutableList<MediaBrowserCompat.MediaItem> = lista

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        return ViewHodel(
            AdapterListaFilaReproducaoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        val musica = listaMusica[position]
        return musica.mediaId!!.toLong()
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.txtNomeMusica.text = listaMusica[position].description.title
        holder.txtNomeAlbum.text = listaMusica[position].description.subtitle

        val imagem: ByteArray? =
            FuncoesUtil.carregarCapaMusica(listaMusica[position].description.mediaUri.toString())
        if (imagem != null) {
            Glide.with(holder.itemView).asBitmap().load(imagem).into(holder.imagemCapa)
        } else {
            Glide.with(holder.itemView).asDrawable().load(R.drawable.sem_album)
                .into(holder.imagemCapa)
        }

        holder.itemView.setOnClickListener {
            reproduzirMusica(listaMusica[position])
        }
    }

    inner class ViewHodel(binding: AdapterListaFilaReproducaoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imagemCapa: ImageView = binding.imgLstFilaCapaMusica
        val txtNomeMusica: TextView = binding.txtFilaNomeMusica
        val txtNomeAlbum: TextView = binding.txtFilaNomeAlbum

    }
}