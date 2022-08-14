package cf.untitled.salend.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
import com.bumptech.glide.Glide

class FavoriteStoreAdapter : RecyclerView.Adapter<FavoriteStoreAdapter.FavoriteStoreViewHoder>() {

    lateinit var favorStoreList : ArrayList<StoreData>

    override fun getItemCount(): Int {
        return favorStoreList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStoreViewHoder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteStoreViewHoder(binding).apply {
            itemView.setOnClickListener{
                val curPos = adapterPosition
                val store = favorStoreList.get(curPos)
                val intent = Intent(parent.context, StoreChoiceActivity::class.java)
                intent.apply {
                    putExtra("id", store._id)
                    putExtra("image", store.s_image)
                }
                ContextCompat.startActivity(parent.context, intent, null)
            }
        }
    }


    override fun onBindViewHolder(holder: FavoriteStoreViewHoder, position: Int) {
        val store = favorStoreList.get(position)
        if (store != null) {
            holder.setStore(store)
        }

    }


    inner class FavoriteStoreViewHoder(val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setStore(store : StoreData) {
            binding.categoryStoreName.text = store.s_name
            Glide.with(binding.root)
                .load(store.s_image)
                .into(binding.categoryStoreImage)
        }
    }
}

