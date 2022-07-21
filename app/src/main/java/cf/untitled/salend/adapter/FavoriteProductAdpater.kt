package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemCategoryBinding
import cf.untitled.salend.databinding.ItemStoreItemBinding
import cf.untitled.salend.model.ProductArray
import cf.untitled.salend.model.ProductData
import com.bumptech.glide.Glide

class FavoriteProductAdpater : RecyclerView.Adapter<FavoriteProductAdpater.Holder>() {

    var productArray : ArrayList<ProductData> = ArrayList()

    override fun getItemCount(): Int {
        return productArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductAdpater.Holder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductAdpater.Holder, position: Int) {
        val item = productArray.get(position)
        holder.setData(item)
    }

    inner class Holder(val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data : ProductData) {
            binding.categoryStoreName.text = data.i_name
            Glide.with(binding.root)
                .load(data.i_image)
                .into(binding.categoryStoreImage)
        }
    }
}