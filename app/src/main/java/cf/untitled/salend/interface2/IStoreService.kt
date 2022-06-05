package cf.untitled.salend.interface2

import cf.untitled.salend.model.KategoriStore
import retrofit2.Call
import retrofit2.http.GET

interface IStoreService {
    @GET("store")
    fun stores() : Call<KategoriStore>
}