package cf.untitled.salend.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClass {      //retrofit을 활용한 서버통신을 하기 위한 클래스
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.salend.tk/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(RetrofitService::class.java)
}