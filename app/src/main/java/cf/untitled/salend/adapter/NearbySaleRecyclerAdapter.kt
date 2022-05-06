package cf.untitled.salend.adapter

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.R
import cf.untitled.salend.databinding.ItemNearbySaleBinding
import cf.untitled.salend.model.ProductData
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class NearbySaleRecyclerAdapter(private var data: Array<ProductData>) :
    RecyclerView.Adapter<NearbySaleRecyclerAdapter.NearbySaleViewHolder>() {
    class NearbySaleViewHolder(binding: ItemNearbySaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val holder2 = binding.rightItemView
        val img = binding.productImg
        val shop = binding.shopName
        val name = binding.productName
        val price = binding.productPrice
        val nowPrice = binding.productNowPrice

        val img2 = binding.productImg2
        val shop2 = binding.shopName2
        val name2 = binding.productName2
        val price2 = binding.productPrice2
        val nowPrice2 = binding.productNowPrice2
    }

    override fun onBindViewHolder(holder: NearbySaleViewHolder, position: Int) {

        holder.apply {
            val evenPosition = position * 2
            val oddPosition = position * 2 + 1
            Glide.with(img.context)
                .load(data[evenPosition].i_image)
                .into(img)
            shop.text = data[evenPosition].i_store_name
            name.text = data[evenPosition].i_name
            price.text = data[evenPosition].i_price.toString()
            price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            nowPrice.text = data[evenPosition].i_now_price.toString()

            try{
                Glide.with(img2.context)
                    .load(data[oddPosition].i_image)
                    .into(img2)
                shop2.text = data[oddPosition].i_store_name
                name2.text = data[oddPosition].i_name
                price2.text = data[oddPosition].i_price.toString()
                price2.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                nowPrice2.text = data[oddPosition].i_now_price.toString()
            }catch (e:Exception) {
                holder2.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return Math.round((data.size / 2).toDouble()).toInt() + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NearbySaleRecyclerAdapter.NearbySaleViewHolder {
        return NearbySaleRecyclerAdapter.NearbySaleViewHolder(
            ItemNearbySaleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}