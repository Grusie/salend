package cf.untitled.salend.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.StoreChoiceActivity
import cf.untitled.salend.databinding.ItemStoreItemBinding
import cf.untitled.salend.model.ProductArray
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.net.URL
import kotlin.coroutines.coroutineContext

class StoreChoiceAdapter : RecyclerView.Adapter<StoreChoiceAdapterHolder>(){

    lateinit var itemList : ProductArray
    lateinit var binding: ItemStoreItemBinding

    override fun getItemCount(): Int {
        return itemList.near_by?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreChoiceAdapterHolder {
        binding = ItemStoreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoreChoiceAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreChoiceAdapterHolder, position: Int) {
        val item = itemList.near_by?.get(position)
        with(binding) {
            val price : Int? = itemList.near_by?.get(position)?.i_price
            val nowPrice : Int? = itemList.near_by?.get(position)?.i_now_price
            val discount : Int?  = price?.div((price- nowPrice!!))
            itemStoreItemName.text = item?.i_name
            itemStoreItemPrice.text = item?.i_price.toString()
            itemStoreItemPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            itemStoreItemNowPrice.text = item?.i_now_price.toString()
            itemStoreItemDiscount.text = "${discount}% 할인!"
            Glide.with(binding.root)
                .load(item?.i_image)
                .into(binding.itemStoreItemImage)
        }
    }
}

class StoreChoiceAdapterHolder(binding : ItemStoreItemBinding) : RecyclerView.ViewHolder(binding.root)

