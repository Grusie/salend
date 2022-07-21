package cf.untitled.salend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.untitled.salend.databinding.ItemPayBinding
import cf.untitled.salend.model.StoreData

class PayAdapter : RecyclerView.Adapter<PayAdapter.Holder>() {

    lateinit var payList : ArrayList<StoreData>

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.Holder {
        val binding = ItemPayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: PayAdapter.Holder, position: Int) {
        holder.setData()
    }

    inner class Holder(val binding: ItemPayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData() {

        }
    }
}