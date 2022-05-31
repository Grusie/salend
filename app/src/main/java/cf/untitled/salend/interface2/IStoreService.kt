package cf.untitled.salend.interface2

import cf.untitled.salend.model.KategoriStore
import retrofit2.Call
import retrofit2.http.GET

interface IStoreService {
    @GET("test3.html")
    fun stores() : Call<KategoriStore>
}