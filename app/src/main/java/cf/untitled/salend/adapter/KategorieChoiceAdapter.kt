package cf.untitled.salend.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.databinding.ItemKategorieBinding
import cf.untitled.salend.model.KategoriStore
import cf.untitled.salend.model.KategoriStoreData
import kotlinx.coroutines.*
import java.net.URL

class KategorieChoiceAdapter : RecyclerView.Adapter<Holder>() {

    var storeList : KategoriStore? = null
    lateinit var binding: ItemKategorieBinding

    override fun getItemCount(): Int {
        return storeList?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemKategorieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val store = storeList?.get(position)
        if (store != null) {
            binding.kategorieStoreName.text = store.sName
            CoroutineScope(Dispatchers.Main).launch {
                val url = store.sImage
                val bitmap = withContext(Dispatchers.IO) {
                    loadImage(url)
               }

               binding.kategorieStoreImage.setImageBitmap(bitmap)
            }
        }

    }
}



class Holder(binding : ItemKategorieBinding) : RecyclerView.ViewHolder(binding.root)

suspend fun loadImage(imageUrl : String) : Bitmap {
    val url = URL(imageUrl)
    val stream = url.openStream()
    return BitmapFactory.decodeStream(stream)
}
