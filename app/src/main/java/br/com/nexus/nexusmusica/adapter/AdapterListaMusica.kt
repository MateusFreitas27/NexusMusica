package br.com.nexus.nexusmusica.adapter

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.interfaces.InterfaceClickeListenerListaMusica
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.util.VersaoUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class AdapterListaMusica(private val intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>, private val clickeListenerListaMusica: InterfaceClickeListenerListaMusica): RecyclerView.Adapter<AdapterListaMusica.ViewHodel>() {
    private var posicao: Int = -1
    private var listaMusica: MutableList<Musica> = mutableListOf()

    fun atualizarListaDados(lista: MutableList<Musica>){
        listaMusica.clear()
        listaMusica.addAll(lista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lista_musica,parent,false)
        return ViewHodel(view)
    }

    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.textTitulo.text = listaMusica[position].titulo
        holder.textAlbum.text = listaMusica[position].albumNome

        val imagem: ByteArray? = FuncoesUtil.carregarCapaMusica(listaMusica[position].data)
        if (imagem != null){
            Glide.with(holder.itemView).load(imagem).into(holder.imagemCapa)
        }else{
            Glide.with(holder.itemView).load(R.drawable.sem_album).into(holder.imagemCapa)
        }

        holder.itemView.setOnClickListener {
            clickeListenerListaMusica.onClick(listaMusica[position])
        }

        holder.menuPopupListaMusica.setOnClickListener {
            val menupopup = PopupMenu(holder.itemView.context, it)
            menupopup.inflate(R.menu.menu_lista_musica)
            menupopup.setOnMenuItemClickListener{
                item -> when(item.itemId){
                    R.id.menu_lista_musica_excluir -> {
                        posicao = holder.adapterPosition
                        holder.deletarMusicaDispositivo(listaMusica[position])
                        true
                    }
                    else -> false
                }
            }
            menupopup.show()
        }
    }

    inner class ViewHodel(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagemCapa: ImageView
        val textTitulo: TextView
        val textAlbum: TextView
        val menuPopupListaMusica: ImageView

        init {
            imagemCapa = itemView.findViewById(R.id.imgCapaMusica)
            textTitulo = itemView.findViewById(R.id.textNomeMusica)
            textAlbum = itemView.findViewById(R.id.textNomeAlbum)
            menuPopupListaMusica = itemView.findViewById(R.id.menuListaMusica)
        }

        fun deletarMusicaDispositivo(musica: Musica){
            val uri: Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musica.id)
            val contentResolver = itemView.context.contentResolver
            try {
                contentResolver.delete(uri, null, null)
                listaMusica.removeAt(posicao)
                notifyItemRemoved(posicao)
            }catch (e: SecurityException){
                val intentSender = when{
                    VersaoUtil.androidR() -> {
                        MediaStore.createDeleteRequest(contentResolver, listOf(uri)).intentSender
                    }
                    VersaoUtil.androidQ() -> {
                        val recoverableSecurityException = e as? RecoverableSecurityException
                        recoverableSecurityException?.userAction?.actionIntent?.intentSender
                    }
                    else -> null
                }
                intentSender?.let {sender ->
                    intentSenderLauncher.launch(
                        IntentSenderRequest.Builder(sender).build()

                    )
                }
            }
        }
    }

}