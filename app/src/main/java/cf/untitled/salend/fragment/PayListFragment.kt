package cf.untitled.salend.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cf.untitled.salend.R
import cf.untitled.salend.adapter.PayAdapter
import cf.untitled.salend.databinding.FragmentPayListBinding


class PayListFragment : Fragment() {

    lateinit var binding: FragmentPayListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this cf.untitled.salend.fragment
        binding = FragmentPayListBinding.inflate(inflater)

        val adpater = PayAdapter()
        binding.payListRv.adapter = adpater
        binding.payListRv.layoutManager = LinearLayoutManager(context)

        return binding.root
    }


}