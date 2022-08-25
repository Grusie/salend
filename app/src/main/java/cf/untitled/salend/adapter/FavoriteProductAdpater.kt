package cf.untitled.salend.adapter

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.ProductActivity
import cf.untitled.salend.R
import cf.untitled.salend.databinding.LikeItemPageBinding
import cf.untitled.salend.model.ProductData
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class FavoriteProductAdpater : RecyclerView.Adapter<FavoriteProductAdpater.Holder>() {

    var productArray: ArrayList<ProductData> = ArrayList()
    var windowSize by Delegates.notNull<Int>()

    override fun getItemCount(): Int {
        return productArray.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteProductAdpater.Holder {
        val binding =
            LikeItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.likeItemPageView.layoutParams.width = windowSize / 2

        return Holder(binding).apply {
            itemView.setOnClickListener {
                val curPos = adapterPosition
                val product = productArray.get(curPos)
                val intent = Intent(parent.context, ProductActivity::class.java)
                intent.apply {
                    putExtra("product_id", product._id)
                }
                ContextCompat.startActivity(parent.context, intent, null)
            }
        }
    }

    override fun onBindViewHolder(holder: FavoriteProductAdpater.Holder, position: Int) {
        val item = productArray.get(position)
        holder.setData(item)
    }

    inner class Holder(val binding: LikeItemPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ProductData) {
            Glide.with(binding.root)
                .load(data.i_image)
                .error(R.drawable.ic_no_image_svgrepo_com)
                .into(binding.likeItemPageImg)
            binding.likeItemPageStoreName.text = data.i_store_name
            binding.likeItemPageProductName.text = data.i_name
            binding.likeItemPageProductPrice.text = "${data.i_price.toString()}￦"
            binding.likeItemPageProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            binding.likeItemPageProductNowPrice.text = "${data.i_now_price.toString()}￦"
        }
    }
}