package cf.untitled.salend.retrofit

import cf.untitled.salend.model.ProductArray
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClass {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.salend.tk/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(RetrofitService::class.java)
}