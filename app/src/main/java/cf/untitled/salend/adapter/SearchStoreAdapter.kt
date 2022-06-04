package cf.untitled.salend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.ProductActivity
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.databinding.ItemSearchStoreBinding
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
import com.bumptech.glide.Glide

class SearchStoreAdapter(private var data: ArrayList<StoreData>) :
    RecyclerView.Adapter<SearchStoreAdapter.SearchStoreViewHolder>() {
    class SearchStoreViewHolder(binding: ItemSearchStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var storeName = binding.itemSearchStoreName
        var storeImg = binding.itemSearchStoreImage
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchStoreViewHolder {
        val view = ItemSearchStoreBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchStoreViewHolder, position: Int) {
        Glide.with(holder.storeImg.context)
            .load(data[position].s_image)
            .error(R.drawable.ic_map_svgrepo_com)
            .into(holder.storeImg)
        holder.storeName.text = data[position].s_name
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}