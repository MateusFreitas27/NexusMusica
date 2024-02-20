package br.com.nexus.nexusmusica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.interfaces.InterfaceClickListernerDetalheAlbum
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil

class AdapterListaMusicasAlbumDetalhe(private val listaMusica: List<Musica>, private val clickListernerDetalheAlbum: InterfaceClickListernerDetalheAlbum): RecyclerView.Adapter<AdapterListaMusicasAlbumDetalhe.ViewHodel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_musicas_albuns_detalhe, parent, false )
        return ViewHodel(view)
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        val musica = listaMusica[position]
        val numMusica = musica.numeroFaixa.toString()
        holder.txtNumFaixaAlbum.text = if (numMusica.length >= 4) numMusica.slice(3..3) else numMusica
        holder.txtNomeMusicaAlbum.text = musica.titulo
        holder.txtDuracaoFaixaAlbum.text = FuncoesUtil.formatarDuracaoMusica(musica.duracao)

        holder.itemView.setOnClickListener {
            clickListernerDetalheAlbum.onClick(listaMusica[position])
        }
    }

    inner class ViewHodel(view: View): RecyclerView.ViewHolder(view){
        val txtNumFaixaAlbum: TextView
        val txtNomeMusicaAlbum: TextView
        val txtDuracaoFaixaAlbum: TextView

        init {
            txtNumFaixaAlbum = view.findViewById(R.id.txtNumFaixaMusicaAlbumDetalhe)
            txtNomeMusicaAlbum = view.findViewById(R.id.txtNomeMusicaAlbumDetalhe)
            txtDuracaoFaixaAlbum = view.findViewById(R.id.txtDuracaoFaixaDetalhe)
        }
    }
}