package cf.untitled.salend.adapter

import android.content.Intent
import android.graphics.Paint
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.ProductActivity
import cf.untitled.salend.R
import cf.untitled.salend.databinding.FragmentHomeBinding
import cf.untitled.salend.databinding.ItemNearbySaleBinding
import cf.untitled.salend.fragment.HomeFragment
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.retrofit.RetrofitClass
import com.bumptech.glide.Glide
import java.util.*

class NearbySaleRecyclerAdapter(private var data: ArrayList<ProductData>) :
    RecyclerView.Adapter<NearbySaleRecyclerAdapter.NearbySaleViewHolder>() {
    class NearbySaleViewHolder(binding: ItemNearbySaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val leftHolder = binding.leftItemView
        val rightHolder = binding.rightItemView
        val img = binding.productImg
        val store = binding.storeName
        val name = binding.productName
        val price = binding.productPrice
        val nowPrice = binding.productNowPrice

        val img2 = binding.productImg2
        val store2 = binding.storeName2
        val name2 = binding.productName2
        val price2 = binding.productPrice2
        val nowPrice2 = binding.productNowPrice2
    }

    override fun onBindViewHolder(holder: NearbySaleViewHolder, position: Int) {
        holder.apply {
            var baseUrl = RetrofitClass.retrofit.baseUrl().toString()
            val evenPosition = position * 2
            val oddPosition = position * 2 + 1

            if (evenPosition <= data.size - 1) {
                Glide.with(img.context)
                    .load(baseUrl + data[evenPosition].i_image) //TODO baseUrl 포함 image 처리
                    .error(R.drawable.ic_no_image_svgrepo_com)
                    .into(img)
                val productId = data[evenPosition]._id
                store.text = data[evenPosition].i_store_name
                name.text = data[evenPosition].i_name
                price.text = data[evenPosition].i_price.toString() + "￦"
                price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                nowPrice.text = data[evenPosition].i_now_price.toString() + "￦"
                leftHolder.setOnClickListener {
                    val intent = Intent(holder.itemView.context, ProductActivity::class.java)
                    intent.putExtra("product_id", "$productId")
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
            }
            if (data.size % 2 == 1 && evenPosition == data.size - 1) {
                rightHolder.visibility = View.INVISIBLE
            }

            if (oddPosition <= data.size - 1) {
                Glide.with(img2.context)
                    .load(baseUrl+ data[oddPosition].i_image)       //TODO baseUrl 포함 image 처리
                    .error(R.drawable.ic_no_image_svgrepo_com)
                    .into(img2)
                val product_id = data[oddPosition]._id
                store2.text = data[oddPosition].i_store_name
                name2.text = data[oddPosition].i_name
                price2.text = data[oddPosition].i_price.toString() + "￦"
                price2.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                nowPrice2.text = data[oddPosition].i_now_price.toString() + "￦"
                rightHolder.setOnClickListener {
                    val intent = Intent(holder.itemView.context, ProductActivity::class.java)
                    intent.putExtra("product_id", "$product_id")
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (data.size % 2 == 0)
            return data.size / 2
        else
            return (data.size / 2) + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NearbySaleRecyclerAdapter.NearbySaleViewHolder {
        return NearbySaleViewHolder(
            ItemNearbySaleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
