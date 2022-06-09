package cf.untitled.salend.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemStoreItemBinding
import cf.untitled.salend.model.StoreItemData
import com.bumptech.glide.Glide

class StoreChoiceAdapter : RecyclerView.Adapter<StoreChoiceAdapterHolder>(){

    lateinit var itemList : StoreItemData
    lateinit var binding: ItemStoreItemBinding

    override fun getItemCount(): Int {
        return itemList.items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreChoiceAdapterHolder {
        binding = ItemStoreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoreChoiceAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreChoiceAdapterHolder, position: Int) {
        val item = itemList.items?.get(position)
        with(binding) {
            val price : Int? = itemList.items?.get(position)?.iPrice
            val nowPrice : Int? = itemList.items?.get(position)?.iNowPrice
            val discount : Int?  = ((price!! - nowPrice!!).toDouble()/price*100).toInt()
            itemStoreItemName.text = item?.iName
            itemStoreItemPrice.text = item?.iPrice.toString()
            itemStoreItemPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            itemStoreItemNowPrice.text = item?.iNowPrice.toString()
            itemStoreItemDiscount.text = "${discount}% 할인!"
            Glide.with(binding.root)
                .load(item?.iImage)
                .into(binding.itemStoreItemImage)
        }
    }
}

class StoreChoiceAdapterHolder(binding : ItemStoreItemBinding) : RecyclerView.ViewHolder(binding.root)

