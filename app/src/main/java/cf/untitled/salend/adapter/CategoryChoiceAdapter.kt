package cf.untitled.salend.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.model.CategoryStore
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.URL

class CategoryChoiceAdapter : RecyclerView.Adapter<CategoryChoiceAdapterHolder>() {

    var storeList: CategoryStore? = null
    lateinit var save : String
    lateinit var binding: ItemCategoryBinding

    override fun getItemCount(): Int {
        return storeList?.stores?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChoiceAdapterHolder {
        binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryChoiceAdapterHolder(binding).apply {
            itemView.setOnClickListener {
                val storeName = storeList?.stores?.get(adapterPosition)?.s_name ?: ""
                val storeTime = storeList?.stores?.get(adapterPosition)?.s_time ?: ""
                val storeImage = storeList?.stores?.get(adapterPosition)?.s_image ?: ""
                val storeId = storeList?.stores?.get(adapterPosition)?._id ?: ""
                val intent = Intent(parent.context, StoreChoiceActivity::class.java)
                intent.putExtra("category", save)
                intent.putExtra("name", storeName)
                intent.putExtra("time",storeTime)
                intent.putExtra("image",storeImage)
                intent.putExtra("id",  storeId)
                parent.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryChoiceAdapterHolder, position: Int) {
        val store = storeList?.stores?.get(position)
        if (store != null) {
            binding.categoryStoreName.text = store.s_name
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val url = store.s_image
                    val bitmap = withContext(Dispatchers.IO) {
                        loadImage(url)
                    }

                    binding.categoryStoreImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

}

class CategoryChoiceAdapterHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

suspend fun loadImage(imageUrl: String): Bitmap {
    val url = URL(imageUrl)
    val stream = url.openStream()
    return BitmapFactory.decodeStream(stream)
}


