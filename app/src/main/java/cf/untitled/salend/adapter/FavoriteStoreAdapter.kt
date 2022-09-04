package cf.untitled.salend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemStoreBinding
import cf.untitled.salend.model.StoreData
import com.bumptech.glide.Glide
import kotlin.properties.Delegates
//삭제 예정
class FavoriteStoreAdapter : RecyclerView.Adapter<FavoriteStoreAdapter.FavoriteStoreViewHoder>() {

    lateinit var favorStoreList: ArrayList<StoreData>
    var windowSize by Delegates.notNull<Int>()

    override fun getItemCount(): Int {
        return favorStoreList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStoreViewHoder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemStoreLayout.layoutParams.width = windowSize / 2

        return FavoriteStoreViewHoder(binding).apply {
            itemView.setOnClickListener {
                val curPos = adapterPosition
                val store = favorStoreList.get(curPos)
                val intent = Intent(parent.context, StoreChoiceActivity::class.java)
                intent.apply {
                    putExtra("id", store._id)
                }
                ContextCompat.startActivity(parent.context, intent, null)
            }
        }
    }


    override fun onBindViewHolder(holder: FavoriteStoreViewHoder, position: Int) {
        val store = favorStoreList[position]
        holder.setStore(store)

    }


    inner class FavoriteStoreViewHoder(val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setStore(store: StoreData) {
            binding.categoryStoreName.text = store.s_name
            Glide.with(binding.root)
                .load(store.s_image)
                .into(binding.categoryStoreImage)
        }
    }
}

