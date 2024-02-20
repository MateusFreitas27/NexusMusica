package br.com.nexus.nexusmusica.adapter

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.util.FuncoesUtil
import com.bumptech.glide.Glide

class AdapterFilaReproducao(private val listaMusica: MutableList<MediaBrowserCompat.MediaItem>):
    RecyclerView.Adapter<AdapterFilaReproducao.ViewHodel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_fila_reproducao,parent, false)
        return ViewHodel(view)
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.txtNomeMusica.text = listaMusica[position].description.title
        holder.txtNomeAlbum.text = listaMusica[position].description.subtitle

        val imagem: ByteArray? = FuncoesUtil.carregarCapaMusica(listaMusica[position].description.mediaUri.toString())
        if (imagem != null){
            Glide.with(holder.itemView).asBitmap().load(imagem).into(holder.imagemCapa)
        }else{
            Glide.with(holder.itemView).asDrawable().load(R.drawable.sem_album).into(holder.imagemCapa)
        }
    }
    inner class ViewHodel(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagemCapa: ImageView
        val txtNomeMusica: TextView
        val txtNomeAlbum: TextView
        init {
            imagemCapa = itemView.findViewById(R.id.imgLstFilaCapaMusica)
            txtNomeMusica = itemView.findViewById(R.id.txtFilaNomeMusica)
            txtNomeAlbum = itemView.findViewById(R.id.txtFilaNomeAlbum)
        }
    }
}