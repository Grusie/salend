package cf.untitled.salend.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.KategorieActivity
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemKategorieBinding
import cf.untitled.salend.databinding.KategorieChoiceBinding
import cf.untitled.salend.model.KategoriStore
import cf.untitled.salend.model.KategoriStoreData
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.URL

class KategorieChoiceAdapter : RecyclerView.Adapter<KategorieChoiceAdapterHolder>() {

    var storeList: KategoriStore? = null
    lateinit var save : String
    lateinit var binding: ItemKategorieBinding

    override fun getItemCount(): Int {
        return storeList?.stores?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategorieChoiceAdapterHolder {
        binding = ItemKategorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KategorieChoiceAdapterHolder(binding).apply {
            itemView.setOnClickListener {
                val storeName = storeList?.stores?.get(adapterPosition)?.s_name ?: ""
                val storeTime = storeList?.stores?.get(adapterPosition)?.s_time ?: ""
                val storeImage = storeList?.stores?.get(adapterPosition)?.s_image ?: ""
                val intent = Intent(parent.context, StoreChoiceActivity::class.java)
                intent.putExtra("kategori", save)
                intent.putExtra("name", storeName)
                intent.putExtra("time",storeTime)
                intent.putExtra("image",storeImage)
                parent.context.startActivity(intent)
            }

        }
    }

    override fun onBindViewHolder(holder: KategorieChoiceAdapterHolder, position: Int) {
        val store = storeList?.stores?.get(position)
        if (store != null) {
            binding.kategorieStoreName.text = store.s_name
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val url = store.s_image
                    val bitmap = withContext(Dispatchers.IO) {
                        loadImage(url)
                    }

                    binding.kategorieStoreImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

}

class KategorieChoiceAdapterHolder(binding: ItemKategorieBinding) : RecyclerView.ViewHolder(binding.root)

suspend fun loadImage(imageUrl: String): Bitmap {
    val url = URL(imageUrl)
    val stream = url.openStream()
    return BitmapFactory.decodeStream(stream)
}


