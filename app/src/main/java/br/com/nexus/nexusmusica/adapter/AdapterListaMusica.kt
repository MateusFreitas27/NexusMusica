package br.com.nexus.nexusmusica.adapter

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.recyclerview.widget.RecyclerView
import br.com.nexus.nexusmusica.R
import br.com.nexus.nexusmusica.databinding.AdapterListaMusicaBinding
import br.com.nexus.nexusmusica.modelo.Musica
import br.com.nexus.nexusmusica.util.FuncoesUtil
import br.com.nexus.nexusmusica.util.VersaoUtil
import com.bumptech.glide.Glide

class AdapterListaMusica(
    private val intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val reproduzirMusica: (Musica) -> Unit
): RecyclerView.Adapter<AdapterListaMusica.MusicaViewHodel>() {
    private var posicao: Int = -1
    private var listaMusica: MutableList<Musica> = mutableListOf()

    init {
        setHasStableIds(true)
    }

    fun atualizarListaDados(lista: MutableList<Musica>){
        listaMusica.clear()
        listaMusica.addAll(lista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicaViewHodel {
        return MusicaViewHodel(AdapterListaMusicaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemId(position: Int): Long {
        val musica = listaMusica[position]
        return musica.id
    }
    override fun getItemCount(): Int = listaMusica.size

    override fun onBindViewHolder(holder: MusicaViewHodel, position: Int) {
        val musica = listaMusica[position]
        holder.textTitulo.text = musica.titulo
        holder.textAlbum.text = musica.albumNome

        val imagem = FuncoesUtil.carregarCapaMusica(musica)
        Glide.with(holder.itemView).load(imagem).centerCrop().dontAnimate().into(holder.imagemCapa)

        holder.itemView.setOnClickListener {
            reproduzirMusica(musica)
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

    inner class MusicaViewHodel(binding: AdapterListaMusicaBinding): RecyclerView.ViewHolder(binding.root){
        val imagemCapa: ImageView = binding.imgCapaMusica
        val textTitulo: TextView = binding.textNomeMusica
        val textAlbum: TextView = binding.textNomeAlbum
        val menuPopupListaMusica: ImageView = binding.menuListaMusica

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