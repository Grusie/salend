package cf.untitled.salend.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.databinding.FragmentLikeBinding
import cf.untitled.salend.databinding.FragmentMyPageBinding
import cf.untitled.salend.model.StoreArray
import cf.untitled.salend.model.StoreData
import cf.untitled.salend.retrofit.RetrofitService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class LikeFragment : Fragment() {
    lateinit var binding: FragmentLikeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this cf.untitled.salend.fragment
        binding = FragmentLikeBinding.inflate(layoutInflater)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.salend.tk")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)



        return binding.root
    }


}